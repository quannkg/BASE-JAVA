package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthPosition;

public interface AuthPositionRepository extends JpaRepository<AuthPosition, Integer> {
}