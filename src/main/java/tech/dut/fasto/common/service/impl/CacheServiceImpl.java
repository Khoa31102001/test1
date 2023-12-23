package tech.dut.fasto.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tech.dut.fasto.common.service.CacheService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final CacheManager cacheManager;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public synchronized void clearCache(String... name) {
        cacheManager.getCacheNames().forEach(item -> {
            for (String s : name) {
                if (item.contains(s)) {
                    Optional.ofNullable(cacheManager.getCache(item)).ifPresent(Cache::clear);
                }
            }
        });

    }
}
