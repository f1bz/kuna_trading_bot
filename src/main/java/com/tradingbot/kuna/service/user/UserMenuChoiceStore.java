package com.tradingbot.kuna.service.user;

import com.tradingbot.kuna.model.UserMenuChoice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserMenuChoiceStore {

    private final Map<Long, UserMenuChoice> userMenuChoiceMap = new HashMap<>();

    public void changeUserMenuChoice(Long userId, Long marketId, Long indicatorTypeId, BigDecimal value) {
        UserMenuChoice userMenuChoice = userMenuChoiceMap.get(userId);
        if (userMenuChoice == null) {
            userMenuChoice = new UserMenuChoice();
        }
        userMenuChoice.setMarketId(marketId);
        userMenuChoice.setIndicatorTypeId(indicatorTypeId);
        userMenuChoice.setValue(value);
        userMenuChoiceMap.put(userId, userMenuChoice);
    }

    public UserMenuChoice getUserMenuChoice(Long userId) {
        UserMenuChoice userMenuChoice = userMenuChoiceMap.get(userId);
        if (userMenuChoice == null) {
            userMenuChoice = new UserMenuChoice();
        }
        return userMenuChoice;
    }

}
