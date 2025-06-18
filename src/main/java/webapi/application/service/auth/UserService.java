package webapi.application.service.auth;

import com.amazonaws.services.iot.model.RegistrationCodeValidationException;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webapi.application.dtos.requests.RegisterRequest;
import webapi.application.service.auth.interfaces.IUserService;
import webapi.domain.AuthUser;
import webapi.domain.AuthUserUserPermission;
import webapi.domain.AuthUserprofile;
import webapi.infrastructure.exception.AppException;
import webapi.infrastructure.helper.PasswordUtils;
import webapi.infrastructure.repositories.AuthUserRepository;
import webapi.infrastructure.repositories.AuthUserUserPermissionRepository;
import webapi.infrastructure.repositories.AuthUserprofileRepository;

import javax.swing.text.html.Option;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final AuthUserRepository authUserRepository;
    private final AuthUserprofileRepository authUserprofileRepository;
    private final AuthUserUserPermissionRepository authUserUserPermissionRepository;
    private final RsaService rsaService; // Add dependency
    private final PasswordUtils passwordUtils;
    @Value("${authentication.password.default}")
    private String PASSWORD_DEFAULT;

    public List<String> getPermissionOfUsers(Integer userId) {
        List<AuthUserUserPermission> authUserUserPermission =  authUserUserPermissionRepository.findByUserId(userId);
        return authUserUserPermission.stream()
                .map(permission -> permission.getPermission().getName())
                .toList();
    }

    public AuthUser findUserById(Integer userId) {
        return authUserRepository.findById(userId).orElse(null);
    }

    @Override
    @Transactional
    public String registerUser(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Validate request
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new AppException("Username is required");
        }

        // Check for existing user
        if (authUserRepository.getAuthUserByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists");
        }
        if (authUserRepository.getAuthUserByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Email already exists");
        }

        // Create new user
        AuthUser authUser = authUserRepository.save(AuthUser.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(Objects.nonNull(request.getPassword()) ? passwordUtils.encode(request.getPassword()) : passwordUtils.encode(PASSWORD_DEFAULT))
                .isSuperuser(false) // Default to non-superuser
                .isActive(true) // Activate user by default
                .passwordRetryCount(0)
                .build());

        AuthUserprofile authUserProfile =  AuthUserprofile.builder()
                .name(request.getFirstName() + " " + request.getLastName())
                .user(authUser)
                .mailingAddress(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
        authUserprofileRepository.save(authUserProfile);

        return "Đăng ký thành công tài khoản: " + request.getUsername();
    }

}