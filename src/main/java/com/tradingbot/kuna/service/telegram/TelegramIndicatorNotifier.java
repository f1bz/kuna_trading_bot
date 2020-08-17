package com.tradingbot.kuna.service.telegram;

import com.tradingbot.kuna.model.Indicator;
import com.tradingbot.kuna.model.MarketRate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TelegramIndicatorNotifier {

    private final KunaTelegramBot telegramBot;

    public TelegramIndicatorNotifier(@Lazy KunaTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void notifyFiredIndicator(MarketRate marketRate, Indicator indicator) {
        Long userId = indicator.getUser().getId();
        String messageText = String.format(UIText.INDICATOR_FIRED_FORMAT,
                indicator.getId(),
                indicator.getMarket().getName(),
                indicator.getIndicatorType().getDescription(),
                indicator.getOriginValue(),
                marketRate.getBuy(),
                indicator.getValue());
        telegramBot.sendMessageToUser(userId, messageText);
    }
}