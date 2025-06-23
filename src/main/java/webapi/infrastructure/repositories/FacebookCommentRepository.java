package webapi.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import webapi.application.service.selenium.dto.request.FacebookCommentRequest;
import webapi.domain.FacebookComment;

public interface FacebookCommentRepository extends JpaRepository<FacebookComment, Long> {
    @Query(
            value =
                    """
                        SELECT fc.* FROM facebook_comment fc
                        where (:#{#request.keyword} is null or fc.comment_text like concat('%', :#{#request.keyword} , '%'))
                        and (:#{#request.postId} is null or fc.post_id = :#{#request.postId} )
                        and (:#{#request.positive} is null or fc.positive = :#{#request.positive} )
                        and (:#{#request.negative} is null or fc.negative = :#{#request.negative} )
                        and (:#{#request.neutral} is null or fc.neutral = :#{#request.neutral} )
               """,
            nativeQuery = true,
            countProjection = "fc.id")
    Page<FacebookComment> search(FacebookCommentRequest request, Pageable pageable);
}