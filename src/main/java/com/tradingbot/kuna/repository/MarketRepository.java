package com.tradingbot.kuna.repository;

import com.tradingbot.kuna.model.Market;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends CrudRepository<Market, Long> {

}