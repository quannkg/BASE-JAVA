package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import webapi.domain.AuthUserUserPermission;

import java.util.List;

public interface AuthUserUserPermissionRepository extends JpaRepository<AuthUserUserPermission, Integer> {
    AuthUserUserPermission findFirstByUserId(Integer userId);

    List<AuthUserUserPermission> findAllByPermissionId(Integer permissionId);

    boolean existsByUserIdAndPermissionId(Integer userId, Integer permissionId);

    @Modifying
    @Transactional
    void deleteByUserId(Integer userId);

    List<AuthUserUserPermission> findByUserId(Integer userId);
    List<AuthUserUserPermission> findByUniversityId(Integer universityId);
}