package com.hillavas.filmvazhe.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by Arash on 16/06/25.
 */
public class Movie implements Serializable {

    String name;
    Integer id;
    String length;
    String fileExt;


    public Movie(JsonObject object) {
        this.id = object.get("id").getAsInt();
        this.name = object.get("name").getAsString();
        if (!object.get("length").isJsonNull())
            this.length = object.get("length").getAsString();
//        if (!object.get("file_ext").isJsonNull())
//            this.fileExt = object.get("file_ext").getAsString();
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getLength() {
        return length;
    }


}
