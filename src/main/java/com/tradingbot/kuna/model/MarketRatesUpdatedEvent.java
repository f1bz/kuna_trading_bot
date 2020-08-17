package com.tradingbot.kuna.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class MarketRatesUpdatedEvent extends ApplicationEvent {

    private final List<MarketRate> newMarketList;

    public MarketRatesUpdatedEvent(List<MarketRate> newMarketList) {
        super(newMarketList);
        this.newMarketList = newMarketList;
    }
}
