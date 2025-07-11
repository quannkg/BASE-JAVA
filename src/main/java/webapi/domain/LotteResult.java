package webapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "lotte_result", schema = "poolmanagement")
public class LotteResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 2000)
    @Column(name = "loai_ve_so", length = 2000)
    private String loaiVeSo;

    @Size(max = 100)
    @Column(name = "ky_quay", length = 100)
    private String kyQuay;

    @Column(name = "ngay_mo_thuong")
    private Instant ngayMoThuong;

    @Size(max = 2000)
    @Column(name = "day_so_trung_thuong", length = 2000)
    private String daySoTrungThuong;

    @Size(max = 3000)
    @Column(name = "jackpot", length = 3000)
    private String jackpot;

    @Column(name = "create_date")
    private Instant createDate;

}