package webapi.application.service.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for {@link webapi.domain.AuthUserprofile}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {
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
    String phoneNumber;
    String imagePath;
    String position;
    Instant dateOfBirth;
    String countryId;
    String districtId;
    String proviceId;
    String wardId;
    String cardId;
    String address;
}