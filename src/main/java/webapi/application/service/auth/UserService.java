package webapi.application.service.auth;

import com.amazonaws.services.iot.model.RegistrationCodeValidationException;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import webapi.application.dtos.requests.RegisterRequest;
import webapi.application.service.auth.interfaces.IUserService;
import webapi.domain.AuthUser;
import webapi.infrastructure.exception.AppException;
import webapi.infrastructure.helper.PasswordUtils;
import webapi.infrastructure.repositories.AuthUserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final AuthUserRepository authUserRepository;
    private final RsaService rsaService; // Add dependency
    private final PasswordUtils passwordUtils;
    @Value("${authentication.password.default}")
    private String PASSWORD_DEFAULT;

    public List<String> getPermissionOfUsers(Integer userId) {
        return List.of("ROLE_USER"); // Replace with actual logic
    }

    public AuthUser findUserById(Integer userId) {
        return authUserRepository.findById(userId).orElse(null);
    }

    @Override
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
        AuthUser authUser = new AuthUser();
        authUser.setUsername(request.getUsername());
        authUser.setEmail(request.getEmail());
        authUser.setPassword(Objects.nonNull(request.getPassword()) ? passwordUtils.encode(request.getPassword()) : passwordUtils.encode(PASSWORD_DEFAULT));
        authUser.setIsActive(true); // Activate user by default
        authUser.setPasswordRetryCount(0);
        authUser.setIsSuperuser(false); // Default to non-superuser

        // Save user
        authUserRepository.save(authUser);


        return "User registered successfully";
    }

}