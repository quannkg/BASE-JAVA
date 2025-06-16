package webapi.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthPayload {
  private Integer id;
  private String name;
  private String email;
  private Boolean isSuperUser;
  private String position;
  private List<String> roles;
  public List<String> getRoles() {
    return Objects.nonNull(roles) ? roles : new ArrayList<>();
  }
}
