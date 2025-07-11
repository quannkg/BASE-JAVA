package webapi.application.service.vietlott;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webapi.domain.LotteResult;
import webapi.infrastructure.repositories.LotteResultRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LotteResultService {
    private final LotteResultRepository lotteResultRepository;

    public void saveResult(LotteResult result) {
        boolean exists = lotteResultRepository
                .existsByKyQuayAndLoaiVeSo(result.getKyQuay(), result.getLoaiVeSo());

        if (!exists) {
            lotteResultRepository.save(result);
            System.out.println("✅ Đã lưu kết quả kỳ: " + result.getKyQuay());
        } else {
            System.out.println("⚠️ Đã tồn tại kết quả kỳ: " + result.getKyQuay());
        }
    }
    public void saveOrUpdateResult(LotteResult result) {
        Optional<LotteResult> existing = lotteResultRepository
                .findByKyQuayAndLoaiVeSo(result.getKyQuay(), result.getLoaiVeSo());

        if (existing.isPresent()) {
            LotteResult old = existing.get();
            old.setDaySoTrungThuong(result.getDaySoTrungThuong());
            old.setNgayMoThuong(result.getNgayMoThuong());
            old.setJackpot(result.getJackpot());
            lotteResultRepository.save(old);
            System.out.println("🔁 Cập nhật kết quả kỳ: " + result.getKyQuay());
        } else {
            lotteResultRepository.save(result);
            System.out.println("✅ Đã lưu mới kết quả kỳ: " + result.getKyQuay());
        }
    }

}
