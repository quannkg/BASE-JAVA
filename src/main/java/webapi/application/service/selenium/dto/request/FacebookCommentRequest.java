package webapi.application.service.selenium.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webapi.application.dtos.requests.PageableRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacebookCommentRequest  extends PageableRequest {
    private Integer postId;
    private Boolean positive;
    private Boolean negative;
    private Boolean neutral;
}
