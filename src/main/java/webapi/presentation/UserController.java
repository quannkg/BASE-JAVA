package webapi.presentation;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapi.application.service.auth.dto.UpdateProfileRequest;
import webapi.application.service.auth.interfaces.IUserService;
import webapi.application.service.auth.dto.AuthUserprofileDto;
import webapi.infrastructure.config.PermissionsAllowed;
import webapi.infrastructure.factory.BaseResponse;
import webapi.infrastructure.factory.ResponseFactory;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {
    private final IUserService iUser;


    @GetMapping("{id}")
    @PermissionsAllowed({"view.user_information"})
    public ResponseEntity<BaseResponse<AuthUserprofileDto>> getUserProfileById(@PathVariable Integer id) {
        return ResponseFactory.success(iUser.getUserProfileById(id));
    }
    @PutMapping("{id}")
    public ResponseEntity<BaseResponse<String>> updateUserProfile(@PathVariable Integer id, @RequestBody UpdateProfileRequest userProfileDto) {
        iUser.updateUserProfile(id, userProfileDto);
        return ResponseFactory.success("Cập nhật thông tin người dùng thành công");
    }
}
