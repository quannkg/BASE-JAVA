package webapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "auth_userprofile", schema = "poolmanagement")
public class AuthUserprofile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "language", nullable = false)
    private String language;

    @Size(max = 255)
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Size(max = 6)
    @Column(name = "gender", length = 6)
    private String gender;

    @Lob
    @Column(name = "mailing_address")
    private String mailingAddress;

    @Lob
    @Column(name = "city")
    private String city;

    @Size(max = 2)
    @Column(name = "country", length = 2)
    private String country;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUser user;


    @Size(max = 50)
    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Size(max = 2)
    @Column(name = "state", length = 2)
    private String state;

    @Lob
    @Column(name = "image_path")
    private String imagePath;

    @Size(max = 255)
    @Column(name = "position")
    private String position;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Size(max = 45)
    @Column(name = "country_id", length = 45)
    private String countryId;

    @Size(max = 10)
    @Column(name = "district_id", length = 10)
    private String districtId;

    @Size(max = 10)
    @Column(name = "provice_id", length = 10)
    private String proviceId;

    @Size(max = 10)
    @Column(name = "ward_id", length = 10)
    private String wardId;

    @Size(max = 30)
    @Column(name = "card_id", length = 30)
    private String cardId;

    @Size(max = 500)
    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_date")
    private Instant approvedDate;

}