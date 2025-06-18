package webapi.presentation;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapi.application.service.group.dto.request.AuthGroupRequest;
import webapi.application.service.group.dto.request.CreateOrUpdateAuthGroupRequest;
import webapi.application.service.group.dto.response.AuthGroupDto;
import webapi.application.service.group.dto.response.AuthGroupResponse;
import webapi.application.service.group.interfaces.IAuthGroup;
import webapi.infrastructure.factory.BaseResponse;
import webapi.infrastructure.factory.ResponseFactory;

@RestController
@RequestMapping("api/auth-group")
@AllArgsConstructor
public class AuthGroupController {
    private final IAuthGroup iAuthGroup;

    @PostMapping("search")
    public ResponseEntity<BaseResponse<AuthGroupResponse>> search(@RequestBody  AuthGroupRequest request) {
        return ResponseFactory.success(iAuthGroup.search(request));
    }
    @GetMapping("{id}")
    public ResponseEntity<BaseResponse<AuthGroupDto>> findGroupById(@PathVariable Integer id) {
        return ResponseFactory.success(iAuthGroup.findGroupById(id));
    }
    @PostMapping("create")
    public ResponseEntity<BaseResponse<String>> create(@RequestBody CreateOrUpdateAuthGroupRequest request) {
        iAuthGroup.createAuthGroup(request);
        return ResponseFactory.success("Tạo mới vai trò thành công");
    }
    @PostMapping("update/{id}")
    public ResponseEntity<BaseResponse<String>> update(@PathVariable Integer id, @RequestBody CreateOrUpdateAuthGroupRequest request) {
        iAuthGroup.updateAuthGroup(id, request);
        return ResponseFactory.success("Chỉnh sửa vai trò thành công");
    }
    @PostMapping("delete/{id}")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Integer id) {
        iAuthGroup.deleteAuthGroup(id);
        return ResponseFactory.success("Xóa vai trò thành công");
    }
}
