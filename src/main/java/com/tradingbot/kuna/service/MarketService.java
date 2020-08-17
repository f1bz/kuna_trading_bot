package com.tradingbot.kuna.service;

import com.tradingbot.kuna.model.Market;
import com.tradingbot.kuna.model.MarketRate;
import com.tradingbot.kuna.repository.MarketRateRepository;
import com.tradingbot.kuna.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final MarketRateRepository marketRateRepository;

    public MarketService(MarketRepository marketRepository, MarketRateRepository marketRateRepository) {
        this.marketRepository = marketRepository;
        this.marketRateRepository = marketRateRepository;
    }

    public List<Market> findAll() {
        return (List<Market>) marketRepository.findAll();
    }

    public MarketRate getLastMarketRate(Long marketId) {
        Optional<Market> marketById = marketRepository.findById(marketId);
        if (!marketById.isPresent()) {
            throw new IllegalArgumentException();
        }
        Optional<MarketRate> lastMarketRate = marketRateRepository.findTop1MarketRateByMarketOrderByIdDesc(marketById.get());
        if (lastMarketRate.isPresent()) {
            return lastMarketRate.get();
        }
        throw new IllegalArgumentException();
    }
}
