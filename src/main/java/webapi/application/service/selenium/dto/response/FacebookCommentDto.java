package webapi.application.service.selenium.dto.response;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link webapi.domain.AuthPermission}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacebookCommentDto implements Serializable {
     Long id;
     String commenterName;
     String commentText;
     Instant createdAt;
     Boolean positive;
     Boolean neutral;
     Boolean negative;
}