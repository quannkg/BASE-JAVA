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
public class FacebookPostRequest extends PageableRequest {
    private String postUrl;
}
