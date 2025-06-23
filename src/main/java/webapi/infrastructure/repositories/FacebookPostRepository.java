package webapi.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import webapi.application.service.selenium.dto.request.FacebookPostRequest;
import webapi.domain.FacebookPost;

public interface FacebookPostRepository extends JpaRepository<FacebookPost, Long> {
    @Query(
            value =
                    """
                        SELECT fp.* FROM facebook_post fp
                        where (:#{#request.keyword} is null or fp.content like concat('%', :#{#request.keyword} , '%'))
               """,
            nativeQuery = true,
            countProjection = "fp.id")
    Page<FacebookPost> search(FacebookPostRequest request, Pageable pageable);
}