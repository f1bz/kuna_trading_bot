package com.tradingbot.kuna.service.indicators;

import com.tradingbot.kuna.model.Indicator;
import com.tradingbot.kuna.model.MarketRate;
import org.springframework.stereotype.Service;

@Service
public class IndicatorFireService {

    public boolean isReadyToFire(MarketRate marketRate, Indicator indicator) {
        return true;
    }

}
