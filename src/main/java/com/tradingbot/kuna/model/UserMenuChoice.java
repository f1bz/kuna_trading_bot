package com.tradingbot.kuna.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserMenuChoice {

    private Long indicatorTypeId;
    private Long marketId;
    private BigDecimal value;

}
