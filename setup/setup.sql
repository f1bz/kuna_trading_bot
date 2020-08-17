create table errors
(
    error_id    int  not null,
    name        text null,
    description text null,
    constraint errors_error_id_uindex
        unique (error_id)
);

alter table errors
    add primary key (error_id);

create table markets
(
    market_id int auto_increment,
    name      varchar(50) not null,
    shortcode varchar(20) not null,
    constraint markets_market_id_uindex
        unique (market_id)
);

alter table markets
    add primary key (market_id);

create table market_rates
(
    market_rate_id int auto_increment,
    market_id      int         not null,
    market_name    varchar(55) not null,
    buy            double      null,
    sell           double      null,
    high           double      null,
    volume         double      null,
    low            double      null,
    timestamp      timestamp   null,
    constraint kuna_market_rates_kuna_market_rate_id_uindex
        unique (market_rate_id),
    constraint market_rates_markets_market_id_fk
        foreign key (market_id) references markets (market_id)
);

alter table market_rates
    add primary key (market_rate_id);

create table users
(
    user_id         int auto_increment,
    username        varchar(255)                        null,
    first_name      varchar(1024)                       null,
    last_name       varchar(1024)                       null,
    date_registered timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    user_state      varchar(255)                        not null,
    constraint users_user_id_uindex
        unique (user_id)
);

alter table users
    add primary key (user_id);

create table indicators
(
    indicator_id    int auto_increment,
    user_id         int              not null,
    market_id       int              not null,
    indicator_type  varchar(255)     not null,
    origin_value    double           null,
    value           double           not null,
    timestamp_added timestamp        null,
    timestamp_fired timestamp        null,
    fired           bit default b'0' not null,
    constraint indicators_indicator_id_uindex
        unique (indicator_id),
    constraint indicators_markets_market_id_fk
        foreign key (market_id) references markets (market_id),
    constraint indicators_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

alter table indicators
    add primary key (indicator_id);


ALTER TABLE `kuna_trading_bot`.`users`
    CHANGE COLUMN `last_name` `last_name` VARCHAR(1024) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NULL DEFAULT NULL ;

ALTER TABLE `kuna_trading_bot`.`users`
    CHANGE COLUMN first_name `first_name` VARCHAR(1024) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NULL DEFAULT NULL ;


INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (1, 'BTC - USDT', 'btcusdt');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (2, 'BTC - USD', 'btcusd');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (3, 'BTC - UAH', 'btcuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (4, 'BTC - RUB', 'btcrub');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (5, 'ETH - UAH', 'ethuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (6, 'ETH - USDT', 'ethusdt');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (7, 'ETH - BTC', 'ethbtc');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (8, 'ETH - RUB', 'ethrub');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (9, 'XRP - UAH', 'xrpuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (10, 'XRP - RUB', 'xrprub');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (11, 'USDT - UAH', 'usdtuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (12, 'USDT - RUB', 'usdtrub');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (13, 'KUN - USDT', 'kunusdt');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (14, 'BCH - UAH', 'bchuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (15, 'EOS - UAH', 'eosuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (16, 'XLM - UAH', 'xlmuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (17, 'LTC - UAH', 'ltcuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (18, 'DASH - UAH', 'dashuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (19, 'ZEC - UAH', 'zecuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (20, 'XEM - UAH', 'xemuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (21, 'WAVES - UAH', 'wavesuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (22, 'DREAM - BTC', 'dreambtc');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (23, 'DREAM - UAH', 'dreamuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (24, 'BNB - UAH', 'bnbuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (25, 'BNB - RUB', 'bnbrub');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (26, 'USDC - UAH', 'usdcuah');
INSERT INTO kuna_trading_bot.markets (market_id, name, shortcode) VALUES (27, 'DAI - UAH', 'daiuah');