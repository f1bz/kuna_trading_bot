package com.tradingbot.kuna.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "indicators")
@Data
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indicator_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "market_id")
    @ManyToOne
    private Market market;

    @Enumerated(EnumType.STRING)
    @Column(name = "indicator_type")
    private IndicatorType indicatorType;

    @Column(name = "origin_value")
    private BigDecimal originValue;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "fired")
    private Boolean fired;

    @Column(name = "timestamp_added")
    private Instant timestampAdded;

    @Column(name = "timestamp_fired")
    private Instant timestampFired;

}