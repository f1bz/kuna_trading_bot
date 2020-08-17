package com.tradingbot.kuna.utils;

import java.math.BigDecimal;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isDigit(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBigDecimal(String text) {
        try {
            new BigDecimal(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
