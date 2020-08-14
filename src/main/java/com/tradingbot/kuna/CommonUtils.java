package com.tradingbot.kuna;

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
}
