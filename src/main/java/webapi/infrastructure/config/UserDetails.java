package webapi.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserDetails extends User {
  private Integer userId;
  private String position;

  public UserDetails(
      String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }
}
