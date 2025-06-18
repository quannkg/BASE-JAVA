package webapi.application.service.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link webapi.domain.AuthUser}
 */
@Value
@Data
@Builder
@AllArgsConstructor
public class AuthUserDto implements Serializable {
    Integer id;
    Instant lastLogin;
    Boolean isSuperuser;
    @Size(max = 150)
    String username;
    @Size(max = 150)
    String firstName;
    @Size(max = 150)
    String lastName;
    @Size(max = 254)
    String email;
    Boolean isStaff;
    Boolean isActive;
    Instant dateJoined;
    Integer refreshToken;
    Boolean isDeleted;
    Integer passwordRetryCount;
    Instant lastChangePasswordTime;
}