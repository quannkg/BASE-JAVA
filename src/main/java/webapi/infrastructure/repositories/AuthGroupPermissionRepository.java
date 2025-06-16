package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthGroupPermission;

public interface AuthGroupPermissionRepository extends JpaRepository<AuthGroupPermission, Integer> {
}