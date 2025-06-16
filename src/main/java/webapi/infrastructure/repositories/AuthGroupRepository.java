package webapi.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webapi.domain.AuthGroup;

import java.util.List;

public interface AuthGroupRepository extends JpaRepository<AuthGroup, Integer> {
    @Query(value = "select ag.* from auth_group ag" +
            " where (ag.name like concat('%', :keyword, '%') and IsNull(:keyword) = 0) or (IsNull(:keyword) = 1)"
            , nativeQuery = true
            , countProjection = "ag.id")
    Page<AuthGroup> search(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
        SELECT ap.codename FROM auth_permission ap
        JOIN auth_group_permissions agp ON agp.permission_id = ap.id
        JOIN auth_group ag ON ag.id = agp.group_id
        WHERE ag.is_deleted = 0 AND ag.status = 1 AND ag.name IN (:roleNames)
    """, nativeQuery = true)
    List<String> getPermissionsByRoleNames(@Param("roleNames") List<String> roleNames);

    List<AuthGroup> findAllByIdIn(List<Integer> ids);
}