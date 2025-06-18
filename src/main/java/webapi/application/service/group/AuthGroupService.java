package webapi.application.service.group;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webapi.application.service.group.dto.request.AuthGroupRequest;
import webapi.application.service.group.dto.request.CreateOrUpdateAuthGroupRequest;
import webapi.application.service.group.dto.response.AuthGroupDto;
import webapi.application.service.group.dto.response.AuthGroupResponse;
import webapi.application.service.group.interfaces.IAuthGroup;
import webapi.domain.AuthGroup;
import webapi.infrastructure.exception.AppException;
import webapi.infrastructure.helper.ModelMapperUtils;
import webapi.infrastructure.repositories.AuthGroupRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthGroupService implements IAuthGroup {
    private final AuthGroupRepository authGroupRepository;


    @Override
    @Transactional
    public void createAuthGroup(CreateOrUpdateAuthGroupRequest request) {
        AuthGroup authGroup = AuthGroup.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .isDeleted(Boolean.FALSE)
                .createdDate(Instant.now())
                .build();
        authGroupRepository.save(authGroup);
    }

    @Override
    @Transactional
    public void updateAuthGroup(Integer id, CreateOrUpdateAuthGroupRequest request) {
        AuthGroup authGroup = findById(id);
        authGroup.setName(request.getName());
        authGroup.setDescription(request.getDescription());
        authGroup.setStatus(request.getStatus());
        authGroupRepository.save(authGroup);
    }

    @Override
    @Transactional
    public void deleteAuthGroup(Integer id) {
        AuthGroup authGroup = findById(id);
        authGroup.setIsDeleted(Boolean.TRUE);
        authGroupRepository.save(authGroup);
    }

    private AuthGroup findById(Integer id) {
       AuthGroup authGroup =  authGroupRepository.findByIdAndIsDeleted(id, Boolean.FALSE);
       if(authGroup == null){
           throw new AppException("Không tìm thấy Id: " + id, HttpStatus.NOT_FOUND);
       }
        return authGroup;
    }

    @Override
    public AuthGroupDto findGroupById(Integer id) {
        AuthGroup authGroup = findById(id);
        return ModelMapperUtils.mapper(authGroup, AuthGroupDto.class);
    }


    @Override
    public AuthGroupResponse search(AuthGroupRequest request) {
        Page<AuthGroup> entity =  authGroupRepository.search(request, request.getPageable());
        List<AuthGroupDto> data = ModelMapperUtils.mapList(entity.getContent().stream().filter(Objects::nonNull).toList(), AuthGroupDto.class);
        return AuthGroupResponse.builder()
                .data(data)
                .count(entity.getNumberOfElements())
                .total(entity.getTotalElements())
                .build();
    }
}
