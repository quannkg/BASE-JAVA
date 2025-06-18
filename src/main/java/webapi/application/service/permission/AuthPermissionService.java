package webapi.application.service.permission;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webapi.application.service.permission.interfaces.IAuthPermission;
import webapi.application.service.permission.dto.request.AuthPermissionRequest;
import webapi.application.service.permission.dto.request.CreateOrUpdateAuthPermissionRequest;
import webapi.application.service.permission.dto.response.AuthPermissionDto;
import webapi.application.service.permission.dto.response.AuthPermissionResponse;
import webapi.domain.AuthPermission;
import webapi.infrastructure.exception.AppException;
import webapi.infrastructure.helper.ModelMapperUtils;
import webapi.infrastructure.repositories.AuthGroupPermissionRepository;
import webapi.infrastructure.repositories.AuthPermissionRepository;
import webapi.infrastructure.repositories.AuthUserUserPermissionRepository;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthPermissionService implements IAuthPermission {
    private final AuthPermissionRepository authPermissionRepository;
    private final AuthUserUserPermissionRepository authUserUserPermissionRepository;
    private final AuthGroupPermissionRepository authGroupPermissionRepository;

    @Override
    @Transactional
    public void createAuthPermission(CreateOrUpdateAuthPermissionRequest request) {
        AuthPermission authPermission = AuthPermission.builder()
                .name(request.getName())
                .contentTypeId(request.getContentTypeId())
                .codename(request.getCodeName())
                .build();
        authPermissionRepository.save(authPermission);
    }

    @Override
    public void updateAuthPermission(Integer id, CreateOrUpdateAuthPermissionRequest request) {
        AuthPermission authPermission = authPermissionRepository.findById(id)
                .orElseThrow(() -> new AppException("Không tìm thấy quyền với id: " + id, HttpStatus.NOT_FOUND));
        authPermission.setName(request.getName());
        authPermission.setContentTypeId(request.getContentTypeId());
        authPermission.setCodename(request.getCodeName());
        authPermissionRepository.save(authPermission);
    }

    @Override
    public void deleteAuthPermission(Integer id) {
        AuthPermission authPermission = authPermissionRepository.findById(id)
                .orElseThrow(() -> new AppException("Không tìm thấy quyền với id: " + id, HttpStatus.NOT_FOUND));
        if (authUserUserPermissionRepository.existsByAuthPermissionId(id) && authGroupPermissionRepository.existsByGroupIdAndPermissionId(id)) {
            throw new AppException("Không thể xóa quyền này vì nó đang được sử dụng.", HttpStatus.BAD_REQUEST);
        }
        authPermissionRepository.delete(authPermission);
    }

    @Override
    public AuthPermissionDto findPermissionById(Integer id) {
        AuthPermission authPermission = authPermissionRepository.findById(id)
                .orElseThrow(() -> new AppException("Không tìm thấy quyền với id: " + id, HttpStatus.NOT_FOUND));
        return AuthPermissionDto.builder()
                .id(authPermission.getId())
                .name(authPermission.getName())
                .contentTypeId(authPermission.getContentTypeId())
                .codename(authPermission.getCodename())
                .build();
    }

    @Override
    public AuthPermissionResponse search(AuthPermissionRequest request) {
        Page<AuthPermission> entity =  authPermissionRepository.search(request, request.getPageable());
        List<AuthPermissionDto> data = ModelMapperUtils.mapList(entity.getContent().stream().filter(Objects::nonNull).toList(), AuthPermissionDto.class);
        return AuthPermissionResponse.builder()
                .data(data)
                .count(entity.getNumberOfElements())
                .total(entity.getTotalElements())
                .build();
    }
}
