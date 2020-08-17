package com.tradingbot.kuna.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "market_rates")
@Data
public class MarketRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_rate_id")
    private Long id;
    private BigDecimal sell;

    @JoinColumn(name = "market_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Market market;

    @Column(name = "market_name")
    private String marketName;

    @Column(name = "buy")
    private BigDecimal buy;

    @Column(name = "low")
    private BigDecimal low;

    @Column(name = "high")
    private BigDecimal high;

    @Column(name = "volume")
    private BigDecimal volume;

    @Column(name = "timestamp")
    private Instant timestamp;

    public MarketRate() {
    }
}