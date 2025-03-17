package com.kosta.big.cache.controller;

import com.kosta.big.cache.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/cache")
@RestController
public class CacheController {

    private final CacheService cacheService;

    @DeleteMapping("/clear")
    public String clearAllCache() {
        cacheService.clearAllCaches();
        System.out.println("Clear all cache");
        return "All cache cleared";
    }
}
