package webapi.application.service.auth.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import webapi.application.dtos.requests.LoginRequest;
import webapi.application.dtos.responses.LoginResponse;
import webapi.infrastructure.config.UserDetails;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IAuthentication {
  LoginResponse login(
          LoginRequest request, String userAgent, HttpServletResponse httpServletResponse)
      throws NoSuchAlgorithmException,
          InvalidKeySpecException,
          JsonProcessingException,
          IllegalAccessException;

  void logout(String userAgent , UserDetails userDetails);
}
