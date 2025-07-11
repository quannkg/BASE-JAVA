package webapi.application.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webapi.application.dtos.requests.RegisterRequest;
import webapi.application.service.auth.dto.UpdateProfileRequest;
import webapi.application.service.auth.interfaces.IUserService;
import webapi.application.service.auth.dto.AuthUserprofileDto;
import webapi.domain.AuthUser;
import webapi.domain.AuthUserGroup;
import webapi.domain.AuthUserUserPermission;
import webapi.domain.AuthUserprofile;
import webapi.infrastructure.exception.AppException;
import webapi.infrastructure.helper.ModelMapperUtils;
import webapi.infrastructure.helper.ModelTransformUtils;
import webapi.infrastructure.helper.PasswordUtils;
import webapi.infrastructure.helper.UserUtils;
import webapi.infrastructure.repositories.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final AuthUserRepository authUserRepository;
    private final AuthUserprofileRepository authUserprofileRepository;
    private final AuthUserUserPermissionRepository authUserUserPermissionRepository;
    private final AuthPermissionRepository authPermissionRepository;
    private final AuthUserGroupRepository authUserGroupRepository;
    private final PasswordUtils passwordUtils;
    @Value("${authentication.password.default}")
    private String PASSWORD_DEFAULT;

    public List<String> getPermissionOfUsers(Integer userId) {
        List<AuthUserUserPermission> authUserUserPermission =  authUserUserPermissionRepository.findByUserId(userId);
        return authUserUserPermission.stream()
                .map(permission -> permission.getPermission().getName())
                .toList();
    }
    public List<String> getCurrentPermissionOfUser() {
        List<AuthUserGroup> userGroups = authUserGroupRepository.findByUserId(UserUtils.getUserId());
        List<Integer> groupIds = ModelTransformUtils.getAttribute(userGroups, x-> x.getGroup().getId());
        return authPermissionRepository.findPermissionCodesInGroupIds(groupIds);
    }

    public AuthUser findUserById(Integer userId) {
        return authUserRepository.findById(userId).orElse(null);
    }

    @Override
    @Transactional
    public String registerUser(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Validate request
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new AppException("Username is required", HttpStatus.BAD_REQUEST);
        }

        // Check for existing user
        if (authUserRepository.getAuthUserByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (authUserRepository.getAuthUserByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
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

    @Override
    public AuthUserprofileDto getUserProfileById(Integer id) {
        Optional<AuthUser> authUser = authUserRepository.findById(id);
        if (authUser.isEmpty()) {
            throw new AppException("Không tìm thấy người dùng với Id: " + id, HttpStatus.NOT_FOUND);
        }
        AuthUserprofileDto userprofileDto  = ModelMapperUtils.mapper(authUser, AuthUserprofileDto.class);
        enrichUserProfile(userprofileDto);
        return userprofileDto;
    }

    @Override
    @Transactional
    public void updateUserProfile(Integer id, UpdateProfileRequest userProfileDto) {
        Optional<AuthUser> authUser = authUserRepository.findById(id);
        if (authUser.isEmpty()) {
            throw new AppException("Không tìm thấy người dùng với Id: " + id, HttpStatus.NOT_FOUND);
        }
        AuthUser user = authUser.get();
        user.setUsername(userProfileDto.getUsername() == null ? user.getUsername() : userProfileDto.getUsername());
        user.setFirstName(userProfileDto.getFirstName() == null ? user.getFirstName() : userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName() == null ? user.getLastName() : userProfileDto.getLastName());
        user.setEmail(userProfileDto.getEmail() == null ? user.getEmail() : userProfileDto.getEmail());
        authUserRepository.save(user);
        Optional<AuthUserprofile> profileOptional = authUserprofileRepository.findFirstByUserId(Long.valueOf(id));
        if (profileOptional.isPresent()) {
            AuthUserprofile profile = profileOptional.get();
            profile.setUser(user);
            profile.setName(userProfileDto.getFirstName() + " " + userProfileDto.getLastName());
            profile.setMailingAddress(userProfileDto.getMailingAddress() == null ? profile.getMailingAddress() : userProfileDto.getMailingAddress());
            profile.setPhoneNumber(userProfileDto.getPhoneNumber() == null ? profile.getPhoneNumber() : userProfileDto.getPhoneNumber());
            profile.setYearOfBirth(userProfileDto.getYearOfBirth() == null ? profile.getYearOfBirth() : userProfileDto.getYearOfBirth());
            profile.setGender(userProfileDto.getGender() == null ? profile.getGender() : userProfileDto.getGender());
            profile.setLocation(userProfileDto.getLocation() == null ? profile.getLocation() : userProfileDto.getLocation());
            profile.setLanguage(userProfileDto.getLanguage() == null ? profile.getLanguage() : userProfileDto.getLanguage());
            profile.setImagePath(userProfileDto.getImagePath()  == null ? profile.getImagePath() : userProfileDto.getImagePath());
            profile.setAddress(userProfileDto.getAddress() == null ? profile.getAddress() : userProfileDto.getAddress());
            profile.setCity(userProfileDto.getCity() == null ? profile.getCity() : userProfileDto.getCity());
            profile.setCardId(userProfileDto.getCardId() == null ? profile.getCardId() : userProfileDto.getCardId());
            profile.setDistrictId(userProfileDto.getDistrictId() == null ? profile.getDistrictId() : userProfileDto.getDistrictId());
            profile.setWardId(userProfileDto.getWardId() == null ? profile.getWardId() : userProfileDto.getWardId());
            profile.setCountryId(userProfileDto.getCountryId() == null ? profile.getCountryId() : userProfileDto.getCountryId());
            profile.setPosition(userProfileDto.getPosition() == null ? profile.getPosition() : userProfileDto.getPosition());
            authUserprofileRepository.save(profile);
        }
    }

    @Override
    public AuthUserprofileDto getCurrentUserProfile() {
        return getUserProfileById(UserUtils.getUserId());
    }

    private void enrichUserProfile(AuthUserprofileDto userprofileDto) {
        Optional<AuthUserprofile> userprofileEntity = authUserprofileRepository.findFirstByUserId(Long.valueOf(userprofileDto.getId()));
        if (userprofileEntity.isEmpty()) {
            throw new AppException("Không tìm thấy người dùng với Id: " + userprofileEntity.get().getUser().getId(), HttpStatus.NOT_FOUND);
        }
        AuthUserprofile userprofile = userprofileEntity.get();
        userprofileDto.setName(userprofile.getName());
        userprofileDto.setMailingAddress(userprofile.getMailingAddress());
        userprofileDto.setPhoneNumber(userprofile.getPhoneNumber());
        userprofileDto.setYearOfBirth(userprofile.getYearOfBirth());
        userprofileDto.setGender(userprofile.getGender());
        userprofileDto.setLocation(userprofile.getLocation());
        userprofileDto.setLanguage(userprofile.getLanguage());
        userprofileDto.setImagePath(userprofile.getImagePath());
        userprofileDto.setAddress(userprofile.getAddress());
        userprofileDto.setCity(userprofile.getCity());
        userprofileDto.setCountry(userprofile.getCountry());
        userprofileDto.setCardId(userprofile.getCardId());
        userprofileDto.setDistrictId(userprofile.getDistrictId());
        userprofileDto.setWardId(userprofile.getWardId());
        userprofileDto.setCountryId(userprofile.getCountryId());
    }

}