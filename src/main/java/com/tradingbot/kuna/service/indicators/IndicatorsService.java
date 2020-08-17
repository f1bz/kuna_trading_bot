package com.tradingbot.kuna.service.indicators;

import com.tradingbot.kuna.model.*;
import com.tradingbot.kuna.repository.IndicatorRepository;
import com.tradingbot.kuna.repository.MarketRateRepository;
import com.tradingbot.kuna.repository.MarketRepository;
import com.tradingbot.kuna.service.telegram.TelegramIndicatorNotifier;
import com.tradingbot.kuna.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IndicatorsService {

    private final MarketRepository marketRepository;
    private final MarketRateRepository marketRateRepository;
    private final IndicatorRepository indicatorRepository;
    private final IndicatorFireService indicatorFireService;
    private final TelegramIndicatorNotifier telegramIndicatorNotifierService;

    public IndicatorsService(MarketRepository marketRepository, MarketRateRepository marketRateRepository, IndicatorRepository indicatorRepository, IndicatorFireService indicatorFireService, TelegramIndicatorNotifier telegramIndicatorNotifierService) {
        this.marketRepository = marketRepository;
        this.marketRateRepository = marketRateRepository;
        this.indicatorRepository = indicatorRepository;
        this.indicatorFireService = indicatorFireService;
        this.telegramIndicatorNotifierService = telegramIndicatorNotifierService;
    }

    public void checkForIndicators(List<MarketRate> updatedMarketRates) {
        List<Indicator> indicators = indicatorRepository.findAllByFiredFalse();
        for (Indicator indicator : indicators) {
            Market indicatorMarket = indicator.getMarket();
            for (MarketRate marketRate : updatedMarketRates) {
                Market marketFromUpdatedRate = marketRate.getMarket();
                if (marketFromUpdatedRate.getId().equals(indicatorMarket.getId())
                        && indicatorFireService.isReadyToFire(marketRate, indicator)) {
                    fire(marketRate,indicator);
                }
            }
        }
    }

    private void fire(MarketRate marketRate, Indicator indicator) {
        telegramIndicatorNotifierService.notifyFiredIndicator(marketRate,indicator);
        markAsFired(indicator);
    }

    public void markAsFired(Indicator indicator) {
        indicator.setFired(true);
        indicator.setTimestampFired(CommonUtils.getNowTimeInUtc());
        indicatorRepository.save(indicator);
    }

    public void delete(Indicator indicator) {
        indicatorRepository.delete(indicator);
    }

    public Optional<Indicator> findById(Long id) {
        return indicatorRepository.findById(id);
    }

    public List<Indicator> findByUserId(Long userId) {
        return indicatorRepository.findAllByUserId(userId);
    }

    public List<Indicator> findAllNotFiredByUserId(Long userId) {
        return indicatorRepository.findAllByUserId(userId)
                .stream()
                .filter(i -> i.getFired().equals(Boolean.FALSE))
                .collect(Collectors.toList());
    }

    public void createIndicator(UserMenuChoice userMenuChoice, User user) {
        Long indicatorTypeId = userMenuChoice.getIndicatorTypeId();
        Long marketId = userMenuChoice.getMarketId();
        BigDecimal value = userMenuChoice.getValue();
        IndicatorType indicatorType = IndicatorType.byId(indicatorTypeId);
        Optional<Market> marketOptional = marketRepository.findById(marketId);

        validateSelectedValues(value, indicatorType, marketOptional);

        Indicator indicator = new Indicator();
        indicator.setIndicatorType(indicatorType);
        indicator.setFired(false);
        Market market = marketOptional.get();
        indicator.setMarket(market);
        indicator.setValue(value);
        Optional<MarketRate> lastMarketRate = marketRateRepository.findTop1MarketRateByMarketOrderByIdDesc(market);
        if (lastMarketRate.isPresent()) {
            BigDecimal originalValue = lastMarketRate.get().getBuy();
            indicator.setOriginValue(originalValue);
            if (indicatorType.equals(IndicatorType.BY_VALUE_ABOVE) && originalValue.compareTo(value) >= 0) {
                throw new IllegalArgumentException();
            } else if (indicatorType.equals(IndicatorType.BY_VALUE_BELOW) && originalValue.compareTo(value) <= 0) {
                throw new IllegalArgumentException();
            }
        } else {
            indicator.setOriginValue(new BigDecimal(0));
        }
        indicator.setUser(user);
        indicator.setTimestampAdded(CommonUtils.getNowTimeInUtc());
        indicatorRepository.save(indicator);
    }

    private void validateSelectedValues(BigDecimal value, IndicatorType indicatorType, Optional<Market> marketOptional) {
        if (!marketOptional.isPresent()) {
            throw new IllegalArgumentException();
        }
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
        if (indicatorType == null) {
            throw new IllegalArgumentException();
        }
    }
}
