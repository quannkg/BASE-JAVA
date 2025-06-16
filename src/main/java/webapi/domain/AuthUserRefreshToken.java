package webapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_user_refresh_token", schema = "poolmanagement")
public class AuthUserRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUser user;

    @Lob
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "is_revoked", nullable = false)
    private Boolean isRevoked;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "revoked_at")
    private Instant revokedAt;

}