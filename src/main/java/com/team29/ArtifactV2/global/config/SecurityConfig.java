package com.team29.ArtifactV2.global.config;

import com.team29.ArtifactV2.domain.member.repository.RefreshRepository;
import com.team29.ArtifactV2.global.security.jwt.CustomLogoutFilter;
import com.team29.ArtifactV2.global.security.jwt.JWTFilter;
import com.team29.ArtifactV2.global.security.jwt.JWTUtil;
import com.team29.ArtifactV2.global.security.jwt.LoginFilterV2;
import com.team29.ArtifactV2.global.security.jwt.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshTokenService refreshTokenService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("*"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(false);
                        configuration.setMaxAge(7200L);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setExposedHeaders(Collections.singletonList("Access"));
                        return configuration;
                    }
                })));

        http.csrf(AbstractHttpConfigurer::disable);
        //From 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());
        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join", "/reissue", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilterV2.class);
        http.addFilterAt(
                new LoginFilterV2(refreshTokenService, authenticationManager(authenticationConfiguration), jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        /* */

//        http.addFilterAt(
//                new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository),
//                UsernamePasswordAuthenticationFilter.class);
//
//        http.addFilterBefore(new CustomLogoutFilte
//        r(jwtUtil, refreshRepository), LogoutFilter.class);

        //세션 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
