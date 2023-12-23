package tech.dut.fasto.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tech.dut.fasto.common.dto.CartDto;
import tech.dut.fasto.common.service.RedisService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<Object, Object> redisTemplate;

    private static final String CART_CACHE_NAME = "CART";

    @Override
    public void setCarts(String key,Map<String,List<CartDto>> map) {
        redisTemplate.opsForValue().set(generatorKey(key,CART_CACHE_NAME), map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String,List<CartDto>> getCarts(String key) {
        return (Map<String,List<CartDto>>) redisTemplate.opsForValue().get(generatorKey(key,CART_CACHE_NAME));
    }

    private String generatorKey(Object key, String prefix){
        return prefix + "::" +key;
    }
}
