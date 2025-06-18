package webapi.application.service.permission.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrUpdateAuthPermissionRequest {
    String name;
    Integer contentTypeId;
    String codeName;
}
