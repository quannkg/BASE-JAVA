package webapi.application.service.auth.interfaces;

import webapi.application.dtos.requests.RegisterRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IUserService {
    String registerUser(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
