package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthPermission;

public interface AuthPermissionRepository extends JpaRepository<AuthPermission, Integer> {
}