package webapi.application.service.auth.interfaces;

import webapi.application.dtos.requests.LoginRequest;
import webapi.application.dtos.requests.RegisterRequest;
import webapi.application.dtos.responses.AuthResponse;
import webapi.application.dtos.responses.UserDto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IUserService {
    String registerUser(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
