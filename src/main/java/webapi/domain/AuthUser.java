package webapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "auth_user", schema = "poolmanagement")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 128)
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "is_superuser", nullable = false)
    private Boolean isSuperuser = false;

    @Size(max = 150)
    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Size(max = 150)
    @Column(name = "first_name", nullable = false, length = 150)
    private String firstName;

    @Size(max = 150)
    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Size(max = 254)
    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Column(name = "is_staff", nullable = false)
    private Boolean isStaff = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "date_joined", nullable = false)
    private Instant dateJoined;

    @Column(name = "refresh_token")
    private Integer refreshToken;

    @ColumnDefault("0")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ColumnDefault("0")
    @Column(name = "password_retry_count")
    private Integer passwordRetryCount;

    @Column(name = "last_change_password_time")
    private Instant lastChangePasswordTime;

}