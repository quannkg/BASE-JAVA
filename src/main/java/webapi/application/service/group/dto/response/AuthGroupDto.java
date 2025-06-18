package webapi.application.service.group.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link webapi.domain.AuthGroup}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthGroupDto implements Serializable {
    Integer id;
    String name;
    String createdBy;
    Instant createdDate;
    String description;
    Boolean status;
    Boolean isDeleted;
}