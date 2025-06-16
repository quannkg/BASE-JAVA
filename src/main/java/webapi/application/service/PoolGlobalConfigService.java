package webapi.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import webapi.application.dtos.PoolGlobalConfigDto;
import webapi.application.dtos.responses.AccountConfigDTO;
import webapi.domain.PoolGlobalConfig;
import webapi.infrastructure.helper.JsonUtils;
import webapi.infrastructure.helper.ModelMapperUtils;
import webapi.infrastructure.helper.ModelTransformUtils;
import webapi.infrastructure.repositories.PoolGlobalConfigRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class PoolGlobalConfigService {
  private final PoolGlobalConfigRepository repository;

  private static final String ENABLE = "1";

  public PoolGlobalConfigDto getConfigByCode(String code) {



    return ModelMapperUtils.mapper(repository.findPoolGlobalConfigByCode(code), PoolGlobalConfigDto.class);
  }

  public PoolGlobalConfigDto saveConfig(PoolGlobalConfigDto dto) {
    return ModelMapperUtils.mapper(repository.save(ModelMapperUtils.mapper(dto, PoolGlobalConfig.class)), PoolGlobalConfigDto.class);
  }

  @Cacheable(
      cacheNames = "getGlobalAccountConfig",
      unless = "#result == null")
  public List<AccountConfigDTO> getGlobalAccountConfig() {
    PoolGlobalConfigDto PoolGlobalConfigDto = getConfigByCode("account");
    if (PoolGlobalConfigDto == null || PoolGlobalConfigDto.getPayload().isEmpty())
      return new ArrayList<>();

    String payload = PoolGlobalConfigDto.getPayload();
    try {
      return JsonUtils.map(payload, new TypeReference<>() {});
    } catch (Exception e) {
      log.error("Exception in getGlobalAccountConfig ", e);
    }

    return new ArrayList<>();
  }

  @Cacheable(
      cacheNames = "getIsEncodePasswordRsa",
      unless = "#result == null")
  public Boolean getIsEncodePasswordRsa() {
    PoolGlobalConfigDto PoolGlobalConfigDto =
        getConfigByCode("is_enable_encode_password_by_rsa");
    if (PoolGlobalConfigDto == null || PoolGlobalConfigDto.getPayload().isEmpty()) return false;
    String payload = PoolGlobalConfigDto.getPayload();
    return Objects.equals(ENABLE, payload.trim());
  }

  public Map<String, AccountConfigDTO> mapDataConfig() {
    return ModelTransformUtils.toMap(getGlobalAccountConfig(), AccountConfigDTO::getCode);
  }
}
