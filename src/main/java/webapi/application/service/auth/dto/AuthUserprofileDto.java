package webapi.application.service.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link webapi.domain.AuthUserprofile}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserprofileDto {
    Integer id;
    String name;
    String username;
    String firstName;
    String lastName;
    String email;
    String language;
    String location;
    Integer yearOfBirth;
    String gender;
    String mailingAddress;
    String city;
    String country;
    String phoneNumber;
    String state;
    String imagePath;
    String position;
    Instant dateOfBirth;
    String countryId;
    String districtId;
    String proviceId;
    String wardId;
    String cardId;
    String address;
    Integer approvedBy;
    Instant approvedDate;
    Instant lastLogin;
    Boolean isSuperuser;
    Boolean isStaff;
    Boolean isActive;
    Instant dateJoined;
}