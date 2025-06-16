package webapi.application.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import webapi.application.dtos.requests.LoginRequest;
import webapi.application.dtos.responses.AccountConfigDTO;
import webapi.application.dtos.responses.LoginResponse;
import webapi.application.service.PoolGlobalConfigService;
import webapi.application.service.auth.interfaces.IAuthentication;
import webapi.domain.AuthUser;
import webapi.domain.AuthUserRefreshToken;
import webapi.domain.AuthUserprofile;
import webapi.infrastructure.common.Constants;
import webapi.infrastructure.config.PoolProperties;
import webapi.infrastructure.config.UserDetails;
import webapi.infrastructure.config.jwt.TokenProvider;
import webapi.infrastructure.exception.LoginFailException;
import webapi.infrastructure.helper.PasswordUtils;
import webapi.infrastructure.repositories.AuthUserRefreshTokenRepository;
import webapi.infrastructure.repositories.AuthUserRepository;
import webapi.infrastructure.repositories.AuthUserprofileRepository;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService implements IAuthentication {
  private final AuthUserRepository authUserRepository;
  private final PasswordUtils passwordUtils;
  private final MessageSource messageSource;
  private final AuthUserRefreshTokenRepository authUserRefreshTokenRepository;
  private final AuthUserprofileRepository authUserProfileRepository;

  @Autowired
  @Lazy
  private UserService userService;
  private final RsaService rsaService;
  private final LoginUserService loginUserService;
  private static final String SV_SOURCE = "sv";

  @Override
  public LoginResponse login(
          LoginRequest request, String userAgent, HttpServletResponse httpServletResponse)
          throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalAccessException {
    Optional<AuthUser> optionalAuthUser = getAuthUserByRequest(request, request.getPoolSource());
    if (optionalAuthUser.isEmpty() || !optionalAuthUser.get().getIsActive()) {
      throw new LoginFailException(
              messageSource.getMessage("account_not_exist", null, Locale.getDefault()),
              0, 0, 0);
    }

    AuthUser authUser = optionalAuthUser.get();
    validLogin(request, authUser, 0);
    validateUserPermissionToLogin(authUser, request.getPoolSource(), 0);

    request.setPassword(rsaService.rsaDecrypt(request.getPassword()));

    if (authUser.getPasswordRetryCount() != 0) {
      authUser.setPasswordRetryCount(0);
      authUserRepository.save(authUser);
    }

    String accessToken = loginUserService.generateToken(authUser);
    String refreshToken = loginUserService.generateRefreshToken(authUser);

    saveRefreshToken(authUser, refreshToken);
    processChangeToken(
            isFromMobile(userAgent) ? "MobileToken:%s" : "Token:%s", authUser, accessToken);

    Boolean isNotifyToChangePassword = isNeedToNotification(authUser, null, request.getPoolSource());

    setCookieForResponse(httpServletResponse, accessToken);
    return loginUserService.buildResponse(accessToken, refreshToken);
  }

  @Override
  public void logout(String userAgent, UserDetails userDetails) {
    Optional<AuthUser> optionalAuthUser = authUserRepository.findById(userDetails.getUserId());
    if (optionalAuthUser.isEmpty()) {
      return;
    }
    AuthUser authUser = optionalAuthUser.get();
    String key =
            isFromMobile(userAgent)
                    ? String.format("MobileToken:%s", authUser.getEmail())
                    : String.format("Token:%s", authUser.getEmail());
  }

  private void setCookieForResponse(HttpServletResponse httpServletResponse, String token) {
    httpServletResponse.addHeader("Set-Cookie", buildCookie(token).toString());
  }

  private ResponseCookie buildCookie(String token) {
    return ResponseCookie.from("access_token", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .sameSite("None")
            .maxAge(2 * 60 * 60)
            .build();
  }

  private void validLogin(LoginRequest request, AuthUser authUser, Integer maxRetryCount)
          throws NoSuchAlgorithmException, InvalidKeySpecException {
    String hashPassword = passwordUtils.encode(request.getPassword());
    if (!hashPassword.equals(authUser.getPassword())) {
      processWrongPassword(authUser);
    }
  }

  private void processWrongPassword(AuthUser authUser) {
    String msg;
    int retrySeconds = 0;
    int retryCount = authUser.getPasswordRetryCount() + 1;
    log.info("Số lần đăng nhập hiện tại là {}", retryCount);
    msg = messageSource.getMessage("login_wrong_too_many_time", null, Locale.getDefault());

    if (retryCount >= 9000) {
      retryCount = 0;
      retrySeconds = 1 * 60;
      log.info(
              "Tài khoản {} đã đăng nhập sai quá số lần quy định {}",
              authUser.getUsername(),
              retryCount);
      authUser.setPasswordRetryCount(authUser.getPasswordRetryCount() + 1);
    } else {
      msg = messageSource.getMessage("password_not_correct", null, Locale.getDefault());
      authUser.setPasswordRetryCount(authUser.getPasswordRetryCount() + 1);
    }
    authUserRepository.save(authUser);
    throw new LoginFailException(msg, retryCount, retrySeconds, 9999);
  }

  private Optional<AuthUser> getAuthUserByRequest(LoginRequest request, String source) {
    Optional<AuthUser> optionalAuthUser;
    switch (request.getLoginType(source)) {
      case EMAIL -> optionalAuthUser = authUserRepository.getAuthUserByUsername(request.getUsername()).isPresent() ?
              authUserRepository.getAuthUserByUsername(request.getUsername()) : authUserRepository.getAuthUserByEmail(request.getUsername());
      case PHONE_NUMBER -> optionalAuthUser = getAuthUserIfPhoneNumber(request.getUsername());
      default -> optionalAuthUser = authUserRepository.getAuthUserByUsername(request.getUsername());
    }
    return optionalAuthUser;
  }

  private Optional<AuthUser> getAuthUserIfPhoneNumber(String phoneNumber) {
    Optional<AuthUserprofile> optionalAuthUserProfile =
            authUserProfileRepository.findByPhoneNumber(phoneNumber);
    if (optionalAuthUserProfile.isEmpty())
      return authUserRepository.getAuthUserByUsername(phoneNumber);

    AuthUserprofile authUserProfile = optionalAuthUserProfile.get();
    return authUserRepository.findById(authUserProfile.getUser().getId());
  }

  private void saveRefreshToken(AuthUser authUser, String token) {
    List<AuthUserRefreshToken> allNotInvokeToken =
            authUserRefreshTokenRepository.findAllByUserIdAndIsRevoked(authUser.getId(), false);
    allNotInvokeToken.forEach(e -> e.setIsRevoked(true));
    authUserRefreshTokenRepository.saveAll(allNotInvokeToken);
    authUserRefreshTokenRepository.save(
            AuthUserRefreshToken.builder()
                    .token(hashRefreshToken(token))
                    .user(authUser)
                    .isRevoked(false)
                    .createdDate(Instant.now())
                    .build());
  }

  private String hashRefreshToken(String token) {
    Argon2 argon2 = Argon2Factory.create();
    char[] tokenChars = token.toCharArray();
    int iterations = 3;
    int memory = 65536;
    int parallelism = 4;
    try {
      return argon2.hash(iterations, memory, parallelism, tokenChars);
    } finally {
      argon2.wipeArray(tokenChars);
    }
  }

  public Boolean isFromMobile(String userAgent) {
    return Objects.nonNull(userAgent) && userAgent.startsWith("Mooc/1.0.0");
  }

  public void processChangeToken(String prefix, AuthUser authUser, String accessToken) {
    String tokenKey = String.format(prefix, authUser.getEmail());
  }

  private void validateUserPermissionToLogin(
          AuthUser authUser, String source, Integer maxRetryCount) {
    List<String> userRoles = userService.getPermissionOfUsers(authUser.getId().intValue());
    if (SV_SOURCE.equalsIgnoreCase(source)) {
      if (userRoles.contains(Constants.MoocPosition.CAP_BO)
              || userRoles.contains(Constants.MoocPosition.QTHT)
              || userRoles.contains(Constants.MoocPosition.GIANG_VIEN)
              || userRoles.contains(Constants.MoocPosition.QTCS)
              || userRoles.contains(Constants.MoocPosition.QTDH)
              || userRoles.contains("ROLE_USER")) {
        throw new LoginFailException(
                messageSource.getMessage(
                        "you_dont_have_permission_to_access_this_site", null, Locale.getDefault()),
                0, 0, maxRetryCount);
      }
    } else {
      if (userRoles.isEmpty() || userRoles.contains(Constants.MoocPosition.SV)) {
        throw new LoginFailException(
                messageSource.getMessage(
                        "you_dont_have_permission_to_access_this_site", null, Locale.getDefault()),
                0, 0, maxRetryCount);
      }
    }
  }

  private Boolean isNeedToNotification(
          AuthUser authUser, Map<String, AccountConfigDTO> configMap, String moocSource) {
    return false; // Reimplement if needed
  }
  private Map<String, Object> convertUsingReflection(Object object) throws IllegalAccessException {
    Map<String, Object> map = new HashMap<>();
    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      map.put(field.getName(), field.get(object));
    }

    return map;
  }
}