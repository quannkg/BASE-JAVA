package webapi.application.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webapi.application.dtos.responses.AuthPayload;
import webapi.application.dtos.responses.LoginResponse;
import webapi.application.dtos.responses.LoginResponseData;
import webapi.domain.*;
import webapi.infrastructure.config.PoolProperties;
import webapi.infrastructure.config.jwt.TokenProvider;
import webapi.infrastructure.helper.ModelTransformUtils;
import webapi.infrastructure.repositories.AuthGroupRepository;
import webapi.infrastructure.repositories.AuthPermissionRepository;
import webapi.infrastructure.repositories.AuthUserGroupRepository;
import webapi.infrastructure.repositories.AuthUserUserPermissionRepository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LoginUserService {
  private final PoolProperties poolProperties;
  private final TokenProvider tokenProvider;
  private final AuthUserGroupRepository authUserGroupRepository;
  private final AuthUserUserPermissionRepository authUserPermissionRepository;
  private final AuthPermissionRepository authPermissionRepository;
  private final AuthGroupRepository authGroupRepository;


  public String generateToken(AuthUser authUser) throws IllegalAccessException {
    // tạo token
    AuthPayload authPayload = buildAuthPayload(authUser);
    Map<String, Object> map = convertUsingReflection(authPayload);
    return tokenProvider.generateToken(map, authUser.getUsername());
  }

  public String generateRefreshToken(AuthUser authUser) throws IllegalAccessException {
    // tạo token
    AuthPayload authPayload = buildAuthPayload(authUser);
    Map<String, Object> map = convertUsingReflection(authPayload);
    return tokenProvider.generateRefreshToken(map, authUser.getUsername());
  }
  private AuthPayload buildAuthPayload(AuthUser authUser) {
    List<AuthUserGroup> authUserGroupList =
            authUserGroupRepository.findAllByUserIdIn(Collections.singletonList(authUser.getId()));

    List<Integer> groupIds = ModelTransformUtils.getAttribute(
            authUserGroupList,
            a -> a.getGroup().getId()
    );

    List<AuthGroup> roles = authGroupRepository.findAllByIdIn(groupIds);

    AuthPayload authPayload =
            AuthPayload.builder()
                    .id(authUser.getId().intValue())
                    .email(authUser.getEmail())
                    .name(authUser.getUsername())
                    .isSuperUser(authUser.getIsSuperuser())
                    .roles(ModelTransformUtils.getAttribute(roles, AuthGroup::getName))
                    .build();

    AuthUserUserPermission authUserPermission =
            authUserPermissionRepository.findFirstByUserId(authUser.getId());

    if (Objects.nonNull(authUserPermission)) {
      AuthPermission permission = authUserPermission.getPermission();
      if (Objects.nonNull(permission)) {
        authPayload.setPosition(permission.getCodename());
      }
    }
    return authPayload;
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

  public LoginResponse buildResponse(String accessToken, String refreshToken) {
    return LoginResponse.builder()
            .data(
                    LoginResponseData.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .expiresIn(
                                    Instant.now()
                                            .plusSeconds(
                                                    poolProperties
                                                            .getSecurity()
                                                            .getAuthentication()
                                                            .getJwt()
                                                            .getTokenValidityInSeconds()))
                            .refreshTokenExpiredIn(
                                    Instant.now()
                                            .plusSeconds(
                                                    poolProperties
                                                            .getSecurity()
                                                            .getAuthentication()
                                                            .getJwt()
                                                            .getRefreshTokenValidityInSeconds()))
                            .build())
            .build();
  }
}