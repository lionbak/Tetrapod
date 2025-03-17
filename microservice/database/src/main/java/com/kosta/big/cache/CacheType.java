package com.kosta.big.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    //읽기에 대한 이름 선언부, 1분마다 expired됨, 최대캐시 사이즈 = 10만
    BOARD_READ("boardCache", 60, 100000);
    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;


}
