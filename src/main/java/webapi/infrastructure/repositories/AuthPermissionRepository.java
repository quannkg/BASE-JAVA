package webapi.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import webapi.application.service.permission.dto.request.AuthPermissionRequest;
import webapi.domain.AuthPermission;

import java.util.List;

public interface AuthPermissionRepository extends JpaRepository<AuthPermission, Integer> {
    @Query(
            value =
                    """
                        SELECT ap.* FROM auth_permission ap
                        where (:#{#request.keyword} is null or ap.name like concat('%', :#{#request.keyword} , '%'))
                        and (:#{#request.contentTypeId} is null or ap.content_type_id = :#{#request.contentTypeId} )
                        and (:#{#request.codename} is null or ap.codename like concat('%', :#{#request.codename} , '%'))
               """,
            nativeQuery = true,
            countProjection = "ap.id")
    Page<AuthPermission> search(AuthPermissionRequest request, Pageable pageable);

    @Query(
            value =
                    """
              select ap.codename
              from auth_permission ap
              join auth_group_permissions agp on ap.id = agp.permission_id
              where agp.group_id in :groupIds
              """, nativeQuery = true)
    List<String> findPermissionCodesInGroupIds(List<Integer> groupIds);
}