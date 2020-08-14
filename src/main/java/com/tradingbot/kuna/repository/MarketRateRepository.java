package com.tradingbot.kuna.repository;

import com.tradingbot.kuna.model.MarketRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRateRepository extends CrudRepository<MarketRate, Long> {

}