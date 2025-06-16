package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthUserprofile;

import java.util.Optional;

public interface AuthUserprofileRepository extends JpaRepository<AuthUserprofile, Integer> {
    Optional<AuthUserprofile> findByPhoneNumber(String phoneNumber);

    Optional<AuthUserprofile> findFirstByUserId(Long userId);
}