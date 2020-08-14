package com.tradingbot.kuna.service;

import com.tradingbot.kuna.CommonUtils;
import com.tradingbot.kuna.model.Market;
import com.tradingbot.kuna.model.MarketRate;
import com.tradingbot.kuna.repository.MarketRateRepository;
import com.tradingbot.kuna.repository.MarketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class RateUpdaterService {

    private final MarketRateScraper marketRateScraper;
    private final MarketRateRepository marketRateRepository;
    private final MarketRepository marketRepository;

    public RateUpdaterService(MarketRateScraper marketRateScraper, MarketRateRepository marketRateRepository, MarketRepository marketRepository) {
        this.marketRateScraper = marketRateScraper;
        this.marketRateRepository = marketRateRepository;
        this.marketRepository = marketRepository;
    }

    @Transactional
    public void updateMarketRates() {
        List<Market> marketRepositoryList = (List<Market>) marketRepository.findAll();
        log.info("Start updating market rates..");
        for (int i = 0; i < marketRepositoryList.size(); i++) {
            Market market = marketRepositoryList.get(i);
            log.info("{} - Updating rate for {} ..", i + 1, market.getName());
            try {
                MarketRate marketRate = marketRateScraper.scrapeCurrentMarketRate(market);
                marketRateRepository.save(marketRate);
                log.info("{} rate updated and saved!", market.getName());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{} failed!\n{}", market.getName(), e.getMessage());
            }
            CommonUtils.sleep(1000L);
        }
        log.info("Finish updating market rates!");

    }
}
