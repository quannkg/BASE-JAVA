package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthUserPosition;

public interface AuthUserPositionRepository extends JpaRepository<AuthUserPosition, Integer> {
}