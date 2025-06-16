package webapi.application.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webapi.application.dtos.responses.AuthPayload;
import webapi.domain.AuthUser;
import webapi.domain.AuthUserGroup;
import webapi.domain.AuthGroup;
import webapi.domain.AuthUserUserPermission;
import webapi.domain.AuthPermission;
import webapi.infrastructure.config.PoolProperties;
import webapi.infrastructure.helper.ModelTransformUtils;
import webapi.infrastructure.repositories.AuthUserGroupRepository;
import webapi.infrastructure.repositories.AuthUserUserPermissionRepository;
import webapi.infrastructure.repositories.AuthPermissionRepository;
import webapi.infrastructure.repositories.AuthGroupRepository;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TokenGenerationService {
    private final AuthUserGroupRepository authUserGroupRepository;
    private final AuthUserUserPermissionRepository authUserPermissionRepository;
    private final AuthPermissionRepository authPermissionRepository;
    private final AuthGroupRepository roleRepository;
    private final PoolProperties poolProperties;

    public Map<String, Object> buildTokenPayload(AuthUser authUser) {
        List<AuthUserGroup> authUserGroupList =
                authUserGroupRepository.findAllByUserIdIn(Collections.singletonList(authUser.getId()));
        List<Integer> groupIds = ModelTransformUtils.getAttribute(authUserGroupList, AuthUserGroup::getId);
        List<AuthGroup> roles = roleRepository.findAllByIdIn(groupIds);

        AuthPayload authPayload =
                AuthPayload.builder()
                        .id(authUser.getId().intValue())
                        .email(authUser.getEmail())
                        .name(authUser.getUsername())
                        .isSuperUser(authUser.getIsSuperuser())
                        .roles(ModelTransformUtils.getAttribute(roles, AuthGroup::getName))
                        .build();

        AuthUserUserPermission authUserPermission =
                authUserPermissionRepository.findFirstByUserId(authUser.getId());

        if (Objects.nonNull(authUserPermission)) {
            Optional<AuthPermission> authPermission =
                    authPermissionRepository.findById(authUserPermission.getPermission().getId());
            authPermission.ifPresent(permission -> authPayload.setPosition(permission.getCodename()));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", authPayload.getId());
        map.put("email", authPayload.getEmail());
        map.put("name", authPayload.getName());
        map.put("isSuperUser", authPayload.getIsSuperUser());
        map.put("roles", authPayload.getRoles());
        map.put("position", authPayload.getPosition());
        return map;
    }

    public String generateToken(AuthUser authUser) {
        Map<String, Object> payload = buildTokenPayload(authUser);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(payload)
                .setSubject(authUser.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + poolProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds() * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(AuthUser authUser) {
        Map<String, Object> payload = buildTokenPayload(authUser);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(payload)
                .setSubject(authUser.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + poolProperties.getSecurity().getAuthentication().getJwt().getRefreshTokenValidityInSeconds() * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(poolProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}