package webapi.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import webapi.infrastructure.config.jwt.JWTFilter;
import webapi.infrastructure.config.jwt.TokenProvider;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
@RequiredArgsConstructor
public class SecurityConfig {
    private final PoolProperties poolProperties;
    private final AuthEntryPointJwt authEntryPointJwt;
    @Autowired
    @Lazy
    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(
                        header -> {
                            header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                        })
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(authEntryPointJwt))
                .cors(
                        cors ->
                                cors.configurationSource(
                                        request -> {
                                            CorsConfiguration configuration = new CorsConfiguration();
                                            configuration.setAllowedOrigins(List.of("*"));
                                            configuration.setAllowedMethods(List.of("*"));
                                            configuration.setAllowedHeaders(List.of("*"));
                                            configuration.setAllowCredentials(true);
                                            return configuration;
                                        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                )
                .addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}