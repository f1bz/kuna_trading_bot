package com.tradingbot.kuna.service.telegram;

public class UIText {

    private UIText() {
    }

    public static final String TELEGRAM_START_COMMAND = "/start";
    public static final String INDICATORS_FORMAT = "%d. %s%nТип: %s%nНачальное значение: %f%nКонечное значение: %f%n%n";

    public static final String LAST_MARKET_VALUE_FORMAT = "Id: %d %nПара: %s%n%nЦена покупки: %f%nЦена продажи: %f%nМакс(24 ч.): %f%nМин (24 ч.): %f%nОбьём (24 ч.): %f%n%nОбновлено: %s";
    public static final String MARKETS_FORMAT = "%d. %s%n";

    public static final String MENU_STARTED = "Выберите действие";
    public static final String MENU_CREATE_NEW_INDICATOR = "Создать индикатор";
    public static final String MENU_DELETE_INDICATOR = "Удалить индикатор";
    public static final String MENU_SHOW_MY_INDICATORS = "Посмотреть индикаторы";
    public static final String MENU_SHOW_LAST_MARKET_RATE = "Посмотреть текущие котировки";
    public static final String MARKET_GO_BACK = "Вернуться в меню";

    public static final String ERROR_BAD_VALUE_SELECTED = "Ошибка!\nВыбрано недопустимое значение!";
    public static final String INDICATOR_DELETED = "Индикатор был успешно удалён!";
    public static final String INDICATOR_ADDED = "Индикатор был успешно добавлен!";

    public static final String SELECT_MARKET = "Выберите крипто-валютную пару и введите её номер";
    public static final String SELECT_INDICATOR = "Выберите индикатор и введите его номер";
    public static final String SELECT_INDICATOR_TYPE = "Выберите тип индикатора и введите его номер";

    public static final String SELECT_INDICATOR_VALUE = "Выберите значение, при котором индикатор должен сработать";

    public static final String NO_AVAILABLE_INDICATORS = "У вас нету индикаторов!";
    public static final String TOTAL_INDICATOR_AMOUNT = "Всего индикаторов: ";


}
