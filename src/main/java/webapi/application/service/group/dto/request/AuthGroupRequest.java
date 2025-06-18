package webapi.application.service.group.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webapi.application.dtos.requests.PageableRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthGroupRequest  extends PageableRequest {
    Boolean status;
}
