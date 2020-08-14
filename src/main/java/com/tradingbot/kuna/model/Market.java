package com.tradingbot.kuna.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "markets")
@Entity
@Data
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "shortcode")
    private String shortcode;
}