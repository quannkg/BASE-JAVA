package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthUserRefreshToken;

import java.util.List;

public interface AuthUserRefreshTokenRepository extends JpaRepository<AuthUserRefreshToken, Integer> {
    List<AuthUserRefreshToken> findAllByUserIdAndIsRevoked(Integer userId, Boolean isRevoke);

    AuthUserRefreshToken findFirstByUserIdAndIsRevoked(Integer userId, Boolean isRevoke);
}