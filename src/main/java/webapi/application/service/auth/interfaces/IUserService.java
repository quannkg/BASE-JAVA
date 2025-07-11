package webapi.application.service.auth.interfaces;

import webapi.application.dtos.requests.RegisterRequest;
import webapi.application.service.auth.dto.AuthUserprofileDto;
import webapi.application.service.auth.dto.UpdateProfileRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IUserService {
    String registerUser(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException;
    AuthUserprofileDto getUserProfileById(Integer id);
    void updateUserProfile(Integer id, UpdateProfileRequest userProfileDto);
    AuthUserprofileDto getCurrentUserProfile();
}
