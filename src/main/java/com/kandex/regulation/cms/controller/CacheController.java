package com.kandex.regulation.cms.controller;

import com.kandex.regulation.cms.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regulation-cms/caches")
@RequiredArgsConstructor
//@SecurityRequirement(name = "Authorization")
public class CacheController {
    private final CacheService cacheService;

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCaches() {
        cacheService.populateCaches();
    }
}
