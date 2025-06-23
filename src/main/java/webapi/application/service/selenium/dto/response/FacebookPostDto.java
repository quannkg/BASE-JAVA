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
public class FacebookPostDto implements Serializable {
    Long id;
    String postUrl;
    String content;
    String imageUrl;
    Instant createdAt;
}