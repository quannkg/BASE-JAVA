package webapi.application.service.permission.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webapi.application.dtos.requests.PageableRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthPermissionRequest extends PageableRequest {
    private String codename;
    private Integer contentTypeId;
}
