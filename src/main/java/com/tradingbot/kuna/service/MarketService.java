package com.tradingbot.kuna.service;

import com.tradingbot.kuna.model.Market;
import com.tradingbot.kuna.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public List<Market> findAll() {
        return (List<Market>) marketRepository.findAll();
    }
}
