package webapi.application.service.group.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrUpdateAuthGroupRequest {
    String name;
    String description;
    Boolean status;
}
