package com.team29.ArtifactV2.global.security.controller;

import com.team29.ArtifactV2.global.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReissueControllerV2 {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public ReissueControllerV2(JWTUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/reissue2")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 1. get refresh token from cookie
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 2. check if expired
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 3. check if token exists in Redis
        Boolean isExist = redisTemplate.hasKey(refresh);
        log.info("{}", refresh);
        if (!Boolean.TRUE.equals(isExist)) {

            return new ResponseEntity<>("invalid refresh token2", HttpStatus.BAD_REQUEST);
        }

        // 4. verify token is refresh type
        String category = jwtUtil.getCategory(refresh);
        if (!"refresh".equals(category)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 5. extract user info
        Long userId = jwtUtil.getUserId(refresh);
        String role = jwtUtil.getRole(refresh);

        // 6. create new JWT tokens
        String newAccess = jwtUtil.createJwt("access", userId, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", userId, role, 86400000L);

        // 7. delete old refresh token from Redis and save new one
        redisTemplate.delete(refresh);
        storeRefreshToken(newRefresh, userId, 86400000L);

        // 8. set access token and refresh cookie
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void storeRefreshToken(String refreshToken, Long userId, Long expireMs) {
        redisTemplate.opsForValue().set(
                refreshToken,
                String.valueOf(userId),
                Duration.ofMillis(expireMs)
        );
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
