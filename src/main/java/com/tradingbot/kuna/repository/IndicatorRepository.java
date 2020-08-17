package com.tradingbot.kuna.repository;

import com.tradingbot.kuna.model.Indicator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorRepository extends CrudRepository<Indicator, Long> {

    List<Indicator> findAllByFiredFalse();

    @Query("select i from Indicator i  where i.user.id = :userId")
    List<Indicator> findAllByUserId(Long userId);

}