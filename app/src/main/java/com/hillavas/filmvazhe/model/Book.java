package com.hillavas.filmvazhe.model;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by Arash on 16/06/25.
 */
public class Book implements Serializable {


    Integer id;
    Integer count;
    String name;


    public Book(JsonObject jsonObject) {

        this.id=jsonObject.get("id").getAsInt();
        this.count=jsonObject.get("count").getAsInt();
        this.name=jsonObject.get("name").getAsString();


    }

    public Integer getId() {
        return id;
    }

    public Integer getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
