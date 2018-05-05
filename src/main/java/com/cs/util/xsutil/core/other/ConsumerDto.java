package com.cs.util.xsutil.core.other;


import com.cs.util.xsutil.common.base.ParamsDto;

public class ConsumerDto extends ParamsDto {

    private String game_id;

    private String terminal_id;

    private String terminal_number;

  //  private Card card;

    private String card_id;

    private String consumerCard_id;
    private String consumerGame_id;

    private String consumer_id;

    private int coins;

    public String getTerminal_number() {
        return terminal_number;
    }

    public void setTerminal_number(String terminal_number) {
        this.terminal_number = terminal_number;
    }

    public String getConsumerGame_id() {
        return consumerGame_id;
    }

    public void setConsumerGame_id(String consumerGame_id) {
        this.consumerGame_id = consumerGame_id;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(String consumer_id) {
        this.consumer_id = consumer_id;
    }

    public String getConsumerCard_id() {
        return consumerCard_id;
    }

    public void setConsumerCard_id(String consumerCard_id) {
        this.consumerCard_id = consumerCard_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
//
//    public Card getCard() {
//        return card;
//    }
//
//    public void setCard(Card card) {
//        this.card = card;
//    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }
}
