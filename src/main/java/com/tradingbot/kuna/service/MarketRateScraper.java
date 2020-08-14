package com.tradingbot.kuna.service;

import com.tradingbot.kuna.model.Market;
import com.tradingbot.kuna.model.MarketRate;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;

@Service
public class MarketRateScraper {

    private static final String MARKET_API_URL_TEMPLATE = "https://kuna.io/api/v2/tickers/%s";

    public MarketRate scrapeCurrentMarketRate(Market market) {
        HttpResponse<JsonNode> jsonResponse
                = Unirest.get(String.format(MARKET_API_URL_TEMPLATE, market.getShortcode()))
                .header("accept", "application/json")
                .asJson();
        MarketRate marketRate = parseResponse(jsonResponse);
        marketRate.setMarket(market);
        marketRate.setMarketName(market.getName());
        return marketRate;
    }

    private MarketRate parseResponse(HttpResponse<JsonNode> jsonResponse) {
        JSONObject responseObject = jsonResponse.getBody()
                .getObject();

        JSONObject ticker = responseObject.getJSONObject("ticker");

        long timestamp = responseObject.getLong("at");
        BigDecimal high = ticker.getBigDecimal("high");
        BigDecimal low = ticker.getBigDecimal("low");
        BigDecimal buy = ticker.getBigDecimal("buy");
        BigDecimal sell = ticker.getBigDecimal("sell");
        BigDecimal volume = ticker.getBigDecimal("vol");

        Instant timestampUTC = Instant.ofEpochSecond(timestamp)
                .atOffset(ZoneOffset.UTC)
                .toInstant();

        MarketRate marketRate = MarketRate.builder()
                .sell(sell)
                .buy(buy)
                .timestamp(timestampUTC)
                .high(high)
                .low(low)
                .volume(volume)
                .build();
        return marketRate;
    }
}
