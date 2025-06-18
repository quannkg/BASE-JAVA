package webapi.presentation;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapi.application.service.permission.interfaces.IAuthPermission;
import webapi.application.service.permission.dto.request.AuthPermissionRequest;
import webapi.application.service.permission.dto.request.CreateOrUpdateAuthPermissionRequest;
import webapi.application.service.permission.dto.response.AuthPermissionDto;
import webapi.application.service.permission.dto.response.AuthPermissionResponse;
import webapi.infrastructure.factory.BaseResponse;
import webapi.infrastructure.factory.ResponseFactory;

@RestController
@RequestMapping("api/auth-permission")
@AllArgsConstructor
public class AuthPermissionController {
    private final IAuthPermission iAuthPermission;

    @PostMapping("search")
    public ResponseEntity<BaseResponse<AuthPermissionResponse>> search(@RequestBody AuthPermissionRequest request) {
        return ResponseFactory.success(iAuthPermission.search(request));
    }
    @GetMapping("{id}")
    public ResponseEntity<BaseResponse<AuthPermissionDto>> findPermissionById(@PathVariable Integer id) {
        return ResponseFactory.success(iAuthPermission.findPermissionById(id));
    }
    @PostMapping("create")
    public ResponseEntity<BaseResponse<String>> create(@RequestBody CreateOrUpdateAuthPermissionRequest request) {
        iAuthPermission.createAuthPermission(request);
        return ResponseFactory.success("Tạo mới quuyền thành công");
    }
    @PutMapping("update/{id}")
    public ResponseEntity<BaseResponse<String>> update(@PathVariable Integer id, @RequestBody CreateOrUpdateAuthPermissionRequest request) {
        iAuthPermission.updateAuthPermission(id, request);
        return ResponseFactory.success("Chỉnh sửa quuyền thành công");
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Integer id) {
        iAuthPermission.deleteAuthPermission(id);
        return ResponseFactory.success("Xóa quuyền thành công");
    }
}
