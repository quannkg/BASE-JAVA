package webapi.application.service.group.interfaces;

import webapi.application.service.group.dto.request.AuthGroupRequest;
import webapi.application.service.group.dto.request.CreateOrUpdateAuthGroupRequest;
import webapi.application.service.group.dto.response.AuthGroupDto;
import webapi.application.service.group.dto.response.AuthGroupResponse;

public interface IAuthGroup {
    void createAuthGroup(CreateOrUpdateAuthGroupRequest request);
    void updateAuthGroup(Integer id, CreateOrUpdateAuthGroupRequest request);
    void deleteAuthGroup(Integer id);
    AuthGroupDto findGroupById(Integer id);
    AuthGroupResponse search(AuthGroupRequest request);
}
