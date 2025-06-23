package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.ApiIntegrated;

public interface ApiIntegratedRepository extends JpaRepository<ApiIntegrated, Integer> {
    ApiIntegrated findFirstByCodeAndIsDeleted(String code, Boolean isDeleted);
}