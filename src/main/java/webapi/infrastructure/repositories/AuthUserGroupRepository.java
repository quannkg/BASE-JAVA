package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.AuthUserGroup;

import java.util.List;

public interface AuthUserGroupRepository extends JpaRepository<AuthUserGroup, Integer> {
    List<AuthUserGroup> findAllByUserIdIn(List<Integer> userIds);

    List<AuthUserGroup> findByUserId(Integer id);
}