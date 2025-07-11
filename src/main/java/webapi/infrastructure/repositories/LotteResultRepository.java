package webapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapi.domain.LotteResult;

import java.util.Optional;

public interface LotteResultRepository extends JpaRepository<LotteResult, Integer> {
    Optional<LotteResult> findByKyQuayAndLoaiVeSo(String kyQuay, String loaiVeSo);
    boolean existsByKyQuayAndLoaiVeSo(String kyQuay, String loaiVeSo);
}