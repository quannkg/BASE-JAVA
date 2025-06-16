package webapi.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private List<RoleDto> roles;
}
