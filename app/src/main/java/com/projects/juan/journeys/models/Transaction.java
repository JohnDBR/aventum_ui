package com.projects.juan.journeys.models;

/**
 * Created by juan on 14/02/18.
 */

public class Transaction {
    private int id;
    private int coins;
    private String transaction_code;
    private String status;
    private String kind;

    public Transaction(int id, int coins, String transaction_code, String status, String kind) {
        this.id = id;
        this.coins = coins;
        this.transaction_code = transaction_code;
        this.status = status;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public int getCoins() {
        return coins;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public String getStatus() {
        return status;
    }

    public String getKind() {
        return kind;
    }
}
