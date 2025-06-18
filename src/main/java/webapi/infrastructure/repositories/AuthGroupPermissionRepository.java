package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import webapi.domain.AuthGroupPermission;

public interface AuthGroupPermissionRepository extends JpaRepository<AuthGroupPermission, Integer> {
    AuthGroupPermission findFirstByGroupId(Integer groupId);

    @Query(value = """
            SELECT COUNT(*) > 0
            FROM auth_group_permissions agp
            JOIN auth_group ag on ag.id = agp.group_id
            WHERE agp.permission_id = :permissionId and ag.is_deleted = 0
""", nativeQuery = true)
    boolean existsByGroupIdAndPermissionId(Integer permissionId);

    void deleteByGroupId(Integer groupId);
}