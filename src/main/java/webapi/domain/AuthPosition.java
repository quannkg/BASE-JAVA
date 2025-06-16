package webapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "auth_position", schema = "poolmanagement")
public class AuthPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("0")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

}