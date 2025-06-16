package webapi.presentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import webapi.application.dtos.responses.LoginResponse;
import webapi.application.service.auth.interfaces.IAuthentication;
import webapi.application.service.auth.interfaces.IUserService;
import webapi.application.dtos.requests.LoginRequest;
import webapi.application.dtos.requests.RegisterRequest;
import webapi.infrastructure.config.UserDetails;
import webapi.infrastructure.factory.ResponseFactory;
import webapi.infrastructure.factory.BaseResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final IAuthentication authenticationInterface;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String result = userService.registerUser(request);
        return ResponseFactory.success(result);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody @Valid LoginRequest request,
            @RequestHeader(value = "user-agent", required = false) String userAgent,
            HttpServletResponse httpServletResponse)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            JsonProcessingException,
            IllegalAccessException {
        return authenticationInterface.login(request, userAgent, httpServletResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(
            @RequestHeader(value = "user-agent", required = false) String userAgent,
            @AuthenticationPrincipal UserDetails userDetails) {
        authenticationInterface.logout(userAgent, userDetails);
        return ResponseFactory.success();
    }
}
