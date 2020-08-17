package com.tradingbot.kuna.service.rates;

import com.tradingbot.kuna.model.MarketRate;
import com.tradingbot.kuna.model.MarketRatesUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarketRatesUpdatedEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public MarketRatesUpdatedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEventWithUpdatedMarketRates(List<MarketRate> newMarketList) {
        MarketRatesUpdatedEvent event = new MarketRatesUpdatedEvent(newMarketList);
        applicationEventPublisher.publishEvent(event);
    }
}
