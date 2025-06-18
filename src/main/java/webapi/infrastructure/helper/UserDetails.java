package webapi.infrastructure.helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import webapi.infrastructure.common.Constants;

import java.util.Collection;
import java.util.Objects;

import static webapi.infrastructure.common.Constants.MoocPosition.*;


@Getter
@Setter
public class UserDetails extends User {
  private Long userId;
  private String position;

  public UserDetails(
      String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  public Boolean isUser() {
    return Objects.nonNull(position) && position.equalsIgnoreCase(USER);
  }

  public Boolean isAdmin() {
    return Objects.nonNull(position) && position.equalsIgnoreCase(ADMIN);
  }

  public String getRole() {
    if (Boolean.TRUE.equals(isAdmin())) return ADMIN;
    if (Boolean.TRUE.equals(isUser())) return USER;
    return null;
  }
}
