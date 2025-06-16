package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.PoolGlobalConfig;

public interface PoolGlobalConfigRepository extends JpaRepository<PoolGlobalConfig, Integer> {
    PoolGlobalConfig findPoolGlobalConfigByCode(String code);
}