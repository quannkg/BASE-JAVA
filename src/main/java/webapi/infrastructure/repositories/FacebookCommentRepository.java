package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.FacebookComment;

public interface FacebookCommentRepository extends JpaRepository<FacebookComment, Long> {
}