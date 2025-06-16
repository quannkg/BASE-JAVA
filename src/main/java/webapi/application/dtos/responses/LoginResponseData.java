package webapi.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseData {
  private String accessToken;
  private Instant expiresIn;
  private String refreshToken;
  private Instant refreshTokenExpiredIn;
  private Boolean isNeedChangePassword;
  private Integer maxTimeRetry;
  private List<AccountConfigDTO> configs;
  private Boolean isShowPopUpChangePassword;
}
