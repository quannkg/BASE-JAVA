package webapi.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
  @Builder.Default
  private Integer statusCode = 200;
  @Builder.Default
  private String message = "Success";
  private LoginResponseData data;
}
