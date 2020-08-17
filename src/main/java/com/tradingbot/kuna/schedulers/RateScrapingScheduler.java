package com.tradingbot.kuna.schedulers;

import com.tradingbot.kuna.service.rates.RateUpdaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RateScrapingScheduler {

    private final RateUpdaterService rateUpdaterService;

    public RateScrapingScheduler(RateUpdaterService rateUpdaterService) {
        this.rateUpdaterService = rateUpdaterService;
    }

    @Scheduled(fixedDelayString = "${rate.updater.delay}")
    public void scheduler() {
        rateUpdaterService.updateMarketRates();
    }
}
