package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthUser;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> getAuthUserByUsername(String username);
    Optional<AuthUser> getAuthUserByEmail(String email);
}