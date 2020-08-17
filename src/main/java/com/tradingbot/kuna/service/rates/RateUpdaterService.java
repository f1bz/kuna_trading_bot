package com.tradingbot.kuna.service.rates;

import com.tradingbot.kuna.model.Market;
import com.tradingbot.kuna.model.MarketRate;
import com.tradingbot.kuna.repository.MarketRateRepository;
import com.tradingbot.kuna.repository.MarketRepository;
import com.tradingbot.kuna.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class RateUpdaterService {

    private static final long TIME_TO_SLEEP_WHEN_SCRAPING_RATES = 1000L;
    private final MarketRateScraper marketRateScraper;
    private final MarketRateRepository marketRateRepository;
    private final MarketRepository marketRepository;
    private final MarketRatesUpdatedEventPublisher eventPublisher;

    public RateUpdaterService(MarketRateScraper marketRateScraper, MarketRateRepository marketRateRepository, MarketRepository marketRepository, MarketRatesUpdatedEventPublisher eventPublisher) {
        this.marketRateScraper = marketRateScraper;
        this.marketRateRepository = marketRateRepository;
        this.marketRepository = marketRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void updateMarketRates() {
        List<Market> marketRepositoryList = (List<Market>) marketRepository.findAll();
        System.out.println("\n");
        log.info("Start updating market rates..");
        int marketListSize = marketRepositoryList.size();
        List<MarketRate> updateMarketRates = new LinkedList<>();
        for (int i = 0; i < marketListSize; i++) {
            Market market = marketRepositoryList.get(i);
            log.info("{}/{} | Updating rate for {} ..", i + 1, marketListSize, market.getName());
            try {
                MarketRate marketRate = marketRateScraper.scrapeCurrentMarketRate(market);
                MarketRate rate = marketRateRepository.save(marketRate);
                updateMarketRates.add(rate);
                log.info("{} rate updated and saved!", market.getName());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{} failed!\n{}", market.getName(), e.getMessage());
            }
            CommonUtils.sleep(TIME_TO_SLEEP_WHEN_SCRAPING_RATES);
        }
        log.info("Finish updating market rates!");
        eventPublisher.publishEventWithUpdatedMarketRates(updateMarketRates);
    }
}
