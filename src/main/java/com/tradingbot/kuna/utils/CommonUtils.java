package com.tradingbot.kuna.utils;

import java.time.Instant;
import java.time.ZoneOffset;

public class CommonUtils {

    private CommonUtils() {
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Instant getNowTimeInUtc() {
        return Instant.now()
                .atOffset(ZoneOffset.UTC)
                .toInstant();
    }
}
