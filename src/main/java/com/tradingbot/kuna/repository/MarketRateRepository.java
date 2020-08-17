package com.tradingbot.kuna.repository;

import com.tradingbot.kuna.model.Market;
import com.tradingbot.kuna.model.MarketRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketRateRepository extends CrudRepository<MarketRate, Long> {

    Optional<MarketRate> findTop1MarketRateByMarketOrderByIdDesc(Market market);
}