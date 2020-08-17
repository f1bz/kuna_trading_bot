package com.tradingbot.kuna.service.telegram;

import com.tradingbot.kuna.model.*;
import com.tradingbot.kuna.service.MarketService;
import com.tradingbot.kuna.service.indicators.IndicatorsService;
import com.tradingbot.kuna.service.user.UserMenuChoiceStore;
import com.tradingbot.kuna.service.user.UserService;
import com.tradingbot.kuna.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KunaTelegramBot extends TelegramBotCore {

    @Value("${time.zone.hours:3}")
    private Integer zonedOffset;
    private final UserService userService;
    private final UserMenuChoiceStore userMenuChoiceStore;
    private final IndicatorsService indicatorsService;
    private final MarketService marketService;

    public KunaTelegramBot(@Value("${kuna.bot.name}") String botName, @Value("${kuna.bot.token}") String botToken, UserService userService, UserMenuChoiceStore userMenuChoiceStore, IndicatorsService indicatorsService, MarketService marketService) {
        super(botName, botToken);
        this.userService = userService;
        this.userMenuChoiceStore = userMenuChoiceStore;
        this.indicatorsService = indicatorsService;
        this.marketService = marketService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = getTelegramUser(update);
        if (message == null || message.getText() == null) {
            showStartMenu(user);
            return;
        }
        String messageText = message.getText();
        if (messageText.equals(UIText.TELEGRAM_START_COMMAND) || messageText.equals(UIText.MARKET_GO_BACK)) {
            showStartMenu(user);
        } else {
            processUserState(user, messageText);
        }
    }

    private void showStartMenu(User user) {
        sendMessageToUser(user.getId(), UIText.MENU_STARTED, createMenuKeyboard());
        userService.changeState(user, UserState.STARTED);
    }

    private void processUserState(User user, String text) {
        UserState userState = user.getUserState();
        Long userId = user.getId();
        if (userState.equals(UserState.STARTED)) {
            if (text.equals(UIText.MENU_CREATE_NEW_INDICATOR)) {
                String uiText = UIText.SELECT_MARKET + "\n\n" + getAllAvailableMarketsAsString();
                sendMessageToUser(userId, uiText, createGoBackToMenuKeyboard());
                userService.changeState(user, UserState.ADD_NEW_INDICATOR_SELECT_MARKET);
            } else if (text.equals(UIText.MENU_DELETE_INDICATOR)) {
                String uiText = getAllUsersIndicatorsAsString(userId);
                sendMessageToUser(userId, uiText, createGoBackToMenuKeyboard());
                if (uiText.contains(UIText.NO_AVAILABLE_INDICATORS)) {
                    showStartMenu(user);
                } else {
                    userService.changeState(user, UserState.SELECT_INDICATOR_TO_DELETE);
                }
            } else if (text.equals(UIText.MENU_SHOW_MY_INDICATORS)) {
                String uiText = getAllUsersIndicatorsAsString(userId);
                sendMessageToUser(userId, uiText, null);
                showStartMenu(user);
            } else if (text.equals(UIText.MENU_SHOW_LAST_MARKET_RATE)) {
                String uiText = UIText.SELECT_MARKET + "\n\n" + getAllAvailableMarketsAsString();
                sendMessageToUser(userId, uiText, createGoBackToMenuKeyboard());
                userService.changeState(user, UserState.SHOW_MARKET_RATE_SELECT_MARKET);
            } else {
                showStartMenu(user);
            }
        } else if (userState.equals(UserState.ADD_NEW_INDICATOR_SELECT_MARKET)) {
            if (ValidationUtils.isDigit(text)) {
                long marketId = Long.parseLong(text);
                UserMenuChoice userMenuChoice = userMenuChoiceStore.getUserMenuChoice(userId);
                userMenuChoiceStore.changeUserMenuChoice(userId, marketId, userMenuChoice.getIndicatorTypeId(), userMenuChoice.getValue());
                sendMessageToUser(userId, getAllAvailableIndicatorTypesAsString() + "\n" + UIText.SELECT_INDICATOR_TYPE, createGoBackToMenuKeyboard());
                userService.changeState(user, UserState.ADD_NEW_INDICATOR_SELECT_INDICATOR_TYPE);
            } else {
                sendMessageToUser(userId, UIText.ERROR_BAD_VALUE_SELECTED);
            }
        } else if (userState.equals(UserState.ADD_NEW_INDICATOR_SELECT_INDICATOR_TYPE)) {
            if (ValidationUtils.isDigit(text)) {
                long indicatorTypeId = Long.parseLong(text);
                UserMenuChoice userMenuChoice = userMenuChoiceStore.getUserMenuChoice(userId);
                userMenuChoiceStore.changeUserMenuChoice(userId, userMenuChoice.getMarketId(), indicatorTypeId, userMenuChoice.getValue());
                sendMessageToUser(userId, UIText.SELECT_INDICATOR_VALUE, createGoBackToMenuKeyboard());
                userService.changeState(user, UserState.ADD_NEW_INDICATOR_SELECT_VALUE);
            } else {
                sendMessageToUser(userId, UIText.ERROR_BAD_VALUE_SELECTED);
            }
        } else if (userState.equals(UserState.ADD_NEW_INDICATOR_SELECT_VALUE)) {
            if (ValidationUtils.isBigDecimal(text)) {
                try {
                    BigDecimal value = new BigDecimal(text);
                    UserMenuChoice userMenuChoice = userMenuChoiceStore.getUserMenuChoice(userId);
                    userMenuChoiceStore.changeUserMenuChoice(userId, userMenuChoice.getMarketId(), userMenuChoice.getIndicatorTypeId(), value);
                    indicatorsService.createIndicator(userMenuChoice, user);
                    sendMessageToUser(userId, UIText.INDICATOR_ADDED, null);
                } catch (Exception e) {
                    sendMessageToUser(userId, UIText.ERROR_BAD_VALUE_SELECTED);
                }
                showStartMenu(user);
            } else {
                sendMessageToUser(userId, UIText.ERROR_BAD_VALUE_SELECTED);
            }
        } else if (userState.equals(UserState.SELECT_INDICATOR_TO_DELETE)) {
            if (ValidationUtils.isDigit(text)) {
                Long id = Long.parseLong(text);
                Optional<Indicator> byId = indicatorsService.findById(id);
                if (byId.isPresent() && byId.get().getUser().getId().equals(userId)) {
                    sendMessageToUser(userId, UIText.INDICATOR_DELETED, null);
                    indicatorsService.delete(byId.get());
                } else {
                    sendMessageToUser(userId, UIText.ERROR_BAD_VALUE_SELECTED, null);
                }
            }
            showStartMenu(user);
        } else if (userState.equals(UserState.SHOW_MARKET_RATE_SELECT_MARKET)) {
            if (ValidationUtils.isDigit(text)) {
                Long id = Long.parseLong(text);
                try {
                    MarketRate lastMarketRate = marketService.getLastMarketRate(id);
                    OffsetDateTime dateTimeZoned = lastMarketRate.getTimestamp().atOffset(ZoneOffset.ofHours(zonedOffset));
                    String uiText = String.format(UIText.LAST_MARKET_VALUE_FORMAT,
                            lastMarketRate.getId(),
                            lastMarketRate.getMarketName(),
                            lastMarketRate.getBuy(),
                            lastMarketRate.getSell(),
                            lastMarketRate.getHigh(),
                            lastMarketRate.getLow(),
                            lastMarketRate.getVolume(),
                            dateTimeZoned);
                    sendMessageToUser(userId, uiText, null);
                } catch (Exception e) {
                    sendMessageToUser(userId, UIText.ERROR_BAD_VALUE_SELECTED, null);
                }

            }
            showStartMenu(user);
        } else {
            showStartMenu(user);
        }
    }

    private User getTelegramUser(Update update) {
        Chat chat = update.getMessage().getChat();
        Long userId = chat.getId();
        boolean userExist = userService.isUserExist(userId);
        if (!userExist) {
            String userName = chat.getUserName();
            String lastName = chat.getLastName();
            String firstName = chat.getFirstName();
            userService.registerNewUser(firstName, lastName, userName, userId);
        }
        return userService.findById(userId);
    }

    private ReplyKeyboard createMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(UIText.MENU_SHOW_MY_INDICATORS);
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(UIText.MENU_CREATE_NEW_INDICATOR);
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(UIText.MENU_DELETE_INDICATOR);
        KeyboardRow keyboardForthRow = new KeyboardRow();
        keyboardForthRow.add(UIText.MENU_SHOW_LAST_MARKET_RATE);
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardForthRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboard createGoBackToMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(UIText.MARKET_GO_BACK);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private String getAllAvailableMarketsAsString() {
        List<Market> allMarkets = marketService.findAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (Market market : allMarkets) {
            stringBuilder.append(String.format(UIText.MARKETS_FORMAT, market.getId(), market.getName()));
        }
        return stringBuilder.toString();
    }

    private String getAllAvailableIndicatorTypesAsString() {
        List<IndicatorType> allIndicators = Arrays.stream(IndicatorType.values())
                .sorted(Comparator.comparingInt(IndicatorType::getId)).collect(Collectors.toList());

        StringBuilder stringBuilder = new StringBuilder();
        for (IndicatorType indicatorType : allIndicators) {
            stringBuilder.append(String.format(UIText.MARKETS_FORMAT, indicatorType.getId(), indicatorType.getDescription()));
        }
        return stringBuilder.toString();
    }

    private String getAllUsersIndicatorsAsString(Long userId) {
        List<Indicator> allUserNotFiredIndicators = indicatorsService.findAllNotFiredByUserId(userId);
        StringBuilder stringBuilder = new StringBuilder();
        if (allUserNotFiredIndicators.isEmpty()) {
            stringBuilder.append(UIText.NO_AVAILABLE_INDICATORS);
        } else {
            stringBuilder.append(UIText.TOTAL_INDICATOR_AMOUNT).append(allUserNotFiredIndicators.size()).append("\n\n");
        }
        for (Indicator indicator : allUserNotFiredIndicators) {
            stringBuilder.append(String.format(UIText.INDICATORS_FORMAT,
                    indicator.getId(),
                    indicator.getMarket().getName(),
                    indicator.getIndicatorType().getDescription(),
                    indicator.getOriginValue(),
                    indicator.getValue()
            ));
        }
        if (!allUserNotFiredIndicators.isEmpty()) {
            stringBuilder.append(UIText.SELECT_INDICATOR);
        }
        return stringBuilder.toString();
    }
}
