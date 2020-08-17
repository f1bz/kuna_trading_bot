package com.tradingbot.kuna.service.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Slf4j
public abstract class TelegramBotCore extends TelegramLongPollingBot {

    private final String botToken;
    private final String botName;

    static {
        ApiContextInitializer.init();
    }

    public TelegramBotCore(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (Exception e) {
            log.error("Error while initialization bot: " + e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void sendMessageToUser(Long userId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessageObject = new SendMessage();
        sendMessageObject.setChatId(userId);
        sendMessageObject.enableHtml(true);
        sendMessageObject.disableWebPagePreview();
        if (replyKeyboard == null) {
            replyKeyboard = new ReplyKeyboardRemove();
        }
        sendMessageObject.setReplyMarkup(replyKeyboard);
        sendMessageObject.setText(text);
        try {
            execute(sendMessageObject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendMessageToUser(Long userId, String text) {
        SendMessage sendMessageObject = new SendMessage();
        sendMessageObject.setChatId(userId);
        sendMessageObject.enableHtml(true);
        sendMessageObject.disableWebPagePreview();
        sendMessageObject.setText(text);
        try {
            execute(sendMessageObject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
