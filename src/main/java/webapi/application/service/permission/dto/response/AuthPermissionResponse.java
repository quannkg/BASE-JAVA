package webapi.application.service.permission.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link webapi.domain.AuthGroup}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthPermissionResponse {
    @Builder.Default private long count = 0;
    @Builder.Default private long total = 0;
    @Builder.Default private List<AuthPermissionDto> data = new ArrayList<>();
}