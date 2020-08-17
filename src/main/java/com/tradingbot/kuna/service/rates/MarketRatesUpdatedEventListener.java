package com.tradingbot.kuna.service.rates;

import com.tradingbot.kuna.model.MarketRate;
import com.tradingbot.kuna.model.MarketRatesUpdatedEvent;
import com.tradingbot.kuna.service.indicators.IndicatorsService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarketRatesUpdatedEventListener implements ApplicationListener<MarketRatesUpdatedEvent> {

    private final IndicatorsService indicatorsService;

    public MarketRatesUpdatedEventListener(IndicatorsService indicatorsService) {
        this.indicatorsService = indicatorsService;
    }

    @Override
    public void onApplicationEvent(MarketRatesUpdatedEvent marketRatesUpdatedEvent) {
        List<MarketRate> updatedEventNewMarketList = marketRatesUpdatedEvent.getNewMarketList();
        indicatorsService.checkForIndicators(updatedEventNewMarketList);
    }
}
