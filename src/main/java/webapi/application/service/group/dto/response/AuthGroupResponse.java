package webapi.application.service.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link webapi.domain.AuthGroup}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthGroupResponse {
    @Builder.Default private long count = 0;
    @Builder.Default private long total = 0;
    @Builder.Default private List<AuthGroupDto> data = new ArrayList<>();
}