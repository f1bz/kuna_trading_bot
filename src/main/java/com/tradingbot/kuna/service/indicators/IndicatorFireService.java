package com.tradingbot.kuna.service.indicators;

import com.tradingbot.kuna.model.Indicator;
import com.tradingbot.kuna.model.IndicatorType;
import com.tradingbot.kuna.model.MarketRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class IndicatorFireService {

    public boolean isReadyToFire(MarketRate marketRate, Indicator indicator) {
        BigDecimal marketValue = marketRate.getBuy();
        BigDecimal signalValue = indicator.getValue();

        if (indicator.getIndicatorType().equals(IndicatorType.BY_VALUE_ABOVE)) {
            return marketValue.compareTo(signalValue) >= 0;
        } else if (indicator.getIndicatorType().equals(IndicatorType.BY_VALUE_BELOW)) {
            return marketValue.compareTo(signalValue) <= 0;
        }
        return false;
    }

}
