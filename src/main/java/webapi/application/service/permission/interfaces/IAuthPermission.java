package webapi.application.service.permission.interfaces;

import webapi.application.service.permission.dto.request.AuthPermissionRequest;
import webapi.application.service.permission.dto.request.CreateOrUpdateAuthPermissionRequest;
import webapi.application.service.permission.dto.response.AuthPermissionDto;
import webapi.application.service.permission.dto.response.AuthPermissionResponse;

public interface IAuthPermission {
    void createAuthPermission(CreateOrUpdateAuthPermissionRequest request);
    void updateAuthPermission(Integer id, CreateOrUpdateAuthPermissionRequest request);
    void deleteAuthPermission(Integer id);
    AuthPermissionDto findPermissionById(Integer id);
    AuthPermissionResponse search(AuthPermissionRequest request);
}
