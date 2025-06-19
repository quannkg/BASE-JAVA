package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.FacebookPost;

public interface FacebookPostRepository extends JpaRepository<FacebookPost, Long> {
}