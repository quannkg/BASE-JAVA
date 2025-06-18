package webapi.application.service.permission.dto.response;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link webapi.domain.AuthPermission}
 */
@Value
@Builder
@AllArgsConstructor
public class AuthPermissionDto implements Serializable {
    Integer id;
    @Size(max = 255)
    String name;
    Integer contentTypeId;
    @Size(max = 100)
    String codename;
}