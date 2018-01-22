package com.hillavas.filmvazhe.model;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by Arash on 16/06/25.
 */
public class Word implements Serializable {


    Integer id;
    String meaning;
    String name;
    String proun;
    String noun = null;
    String verb = null;
    String adj = null;
    Integer clipCount;
    boolean studied;
    Lesson lesson;


    public Word(JsonObject jsonObject) {

        this.id = jsonObject.get("id").getAsInt();
        this.meaning = jsonObject.get("meaning").getAsString();
        this.name = jsonObject.get("name").getAsString();

        if(!jsonObject.get("pronunciation").isJsonNull())
        this.proun = jsonObject.get("pronunciation").getAsString();

        if (!jsonObject.get("noun_synonym").isJsonNull())
            this.noun = jsonObject.get("noun_synonym").getAsString();

        if (!jsonObject.get("verb_synonym").isJsonNull())
            this.verb = jsonObject.get("verb_synonym").getAsString();

        if (!jsonObject.get("adj_synonym").isJsonNull())
            this.adj = jsonObject.get("adj_synonym").getAsString();

        this.lesson = new Lesson(jsonObject.get("lesson").getAsJsonObject());

        if (jsonObject.get("clip_count") != null)
            this.clipCount = jsonObject.get("clip_count").getAsInt();

        if (jsonObject.get("studied") != null)
            this.studied = jsonObject.get("studied").getAsBoolean();

    }
    public Integer getClipCount() {
        return clipCount;
    }

    public boolean isStudied() {
        return studied;
    }

    public Integer getId() {
        return id;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getName() {
        return name;
    }

    public String getProun() {
        return proun;
    }

    public String getNoun() {
        return noun;
    }

    public String getVerb() {
        return verb;
    }

    public String getAdj() {
        return adj;
    }

    public Lesson getLesson() {
        return lesson;
    }
}
