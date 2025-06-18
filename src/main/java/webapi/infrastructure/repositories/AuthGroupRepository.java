package webapi.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webapi.application.service.group.dto.request.AuthGroupRequest;
import webapi.domain.AuthGroup;

import java.util.List;

public interface AuthGroupRepository extends JpaRepository<AuthGroup, Integer> {
    @Query(
            value =
                    """
                        SELECT ag.* FROM auth_group ag
                        where (:#{#request.keyword} is null or ag.name like concat('%', :#{#request.keyword} , '%'))
                        and (:#{#request.status} is null or ag.status = :#{#request.status} )
                        and ag.is_deleted = 0
               """,
            nativeQuery = true,
            countProjection = "ag.id")
    Page<AuthGroup> search(AuthGroupRequest request, Pageable pageable);
//                        and (:#{#request.createdDateFrom } is null or ag.created_date  >= :#{#request.createdDateFrom})
//                        and (:#{#request.createdDateTo} is null or ag.created_date  <= :#{#request.createdDateTo})


    AuthGroup findByIdAndIsDeleted(Integer id, Boolean isDeleted);

    List<AuthGroup> findAllByIdIn(List<Integer> ids);
}