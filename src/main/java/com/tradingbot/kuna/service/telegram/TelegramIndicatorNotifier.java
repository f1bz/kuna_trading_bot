package com.tradingbot.kuna.service.telegram;

import com.tradingbot.kuna.model.Indicator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TelegramIndicatorNotifier {

    private final KunaTelegramBot telegramBot;

    public TelegramIndicatorNotifier(@Lazy KunaTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void notifyFiredIndicator(Indicator indicator) {
        Long userId = indicator.getUser().getId();
        telegramBot.sendMessageToUser(userId, "yoyo");
    }
}