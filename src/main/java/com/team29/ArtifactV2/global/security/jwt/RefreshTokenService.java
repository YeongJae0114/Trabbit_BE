package com.team29.ArtifactV2.global.security.jwt;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "refresh:";

    public void saveToken(Long userId, String refreshToken, long expirationMillis) {
        redisTemplate.opsForValue().set(
                refreshToken,                    // ✅ 토큰 자체를 key로
                String.valueOf(userId),          // ✅ 사용자 ID 저장 (또는 기타 정보)
                expirationMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public String getToken(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void deleteToken(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }

    public boolean isValid(Long userId, String refreshToken) {
        String saved = getToken(userId);
        return saved != null && saved.equals(refreshToken);
    }
}
