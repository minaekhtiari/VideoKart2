package com.hillavas.filmvazhe.model;

import com.google.gson.JsonObject;

/**
 * Created by arashjahani on 11/16/2016 AD.
 */

public class Transaction {

    String date;
    Boolean activation;

    public Transaction(JsonObject object) {
        this.date = object.get("date").getAsString();
        this.activation = object.get("activation").getAsBoolean();
    }

    public String getDate() {
        return date;
    }

    public Boolean getActivation() {
        return activation;
    }
}
