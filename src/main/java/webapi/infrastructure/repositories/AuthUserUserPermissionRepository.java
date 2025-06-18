package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = """
            SELECT COUNT(*) > 0
            FROM auth_user_user_permissions aup
            JOIN auth_user au on au.id = aup.user_id
            WHERE aup.permission_id = :permissionId and au.is_deleted = 0
    """, nativeQuery = true)
    boolean existsByAuthPermissionId(Integer permissionId);

}