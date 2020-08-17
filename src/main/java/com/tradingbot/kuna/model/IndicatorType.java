package com.tradingbot.kuna.model;

import lombok.Getter;

@Getter
public enum IndicatorType {
    BY_VALUE_ABOVE(1, "Значение больше чем"),
    BY_VALUE_BEHIND(2, "Значение меньше чем");

    IndicatorType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    private final String description;
    private final int id;

    public static IndicatorType byId(long id) {
        for (IndicatorType indicatorType : IndicatorType.values()) {
            if (indicatorType.getId() == id) {
                return indicatorType;
            }
        }
        return null;
    }
}

