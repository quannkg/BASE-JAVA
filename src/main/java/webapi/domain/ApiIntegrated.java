package webapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "api_integrated", schema = "poolmanagement")
public class ApiIntegrated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 5000)
    @Column(name = "name", length = 5000)
    private String name;

    @Size(max = 45)
    @Column(name = "code", length = 45)
    private String code;

    @Lob
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

}