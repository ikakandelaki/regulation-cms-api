package com.kandex.regulation.cms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kandex.regulation.cms.repository.RegulationRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
public class RedisCacheServiceImpl extends AbstractCacheService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final RegulationRepository regulationRepository;

    @Override
    <T> void saveInHash(String key, Map<String, T> map) {
        if (StringUtils.isBlank(key) || map == null || map.isEmpty()) {
            return;
        }

        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Map<String, String> mapToSave = getMapToSave(map);
        hashOperations.putAll(key, mapToSave);
    }

    private <T> Map<String, String> getMapToSave(Map<String, T> map) {
        Map<String, String> mapToSave = new HashMap<>();
        map.forEach((hashKey, hashValue) -> {
            try {
                String jsonStringOfSet = objectMapper.writeValueAsString(hashValue);
                mapToSave.put(hashKey, jsonStringOfSet);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return mapToSave;
    }

    void checkIfCacheIsRunning() {
        try {
            redisTemplate.opsForValue().set("healthCheck", "ok");
            String value = redisTemplate.opsForValue().get("healthCheck");
            if ("ok".equals(value)) {
                log.info("Redis healthCheck passed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void clearCache() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    RegulationRepository getRegulationRepository() {
        return regulationRepository;
    }
}
