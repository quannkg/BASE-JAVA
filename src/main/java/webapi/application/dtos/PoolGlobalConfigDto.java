package webapi.application.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link webapi.domain.PoolGlobalConfig}
 */
@Value
@Builder
@Data
@AllArgsConstructor
public class PoolGlobalConfigDto implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 100)
    String code;
    @NotNull
    String payload;
    Instant createdDate;
    Instant modifiedDate;
}