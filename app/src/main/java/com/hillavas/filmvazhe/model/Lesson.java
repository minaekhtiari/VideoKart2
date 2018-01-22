package com.hillavas.filmvazhe.model;

import com.google.gson.JsonObject;
import com.hillavas.filmvazhe.utils.EnumUtils;
import com.hillavas.filmvazhe.MyApplication;

import java.io.Serializable;

/**
 * Created by arashjahani on 10/12/2016 AD.
 */

public class Lesson implements Serializable {


    private String name;
    private String desc;
    private Integer id;
    private Integer bookID;
    private Integer status;
    private Integer wordsCount;
    private Integer studiedCount = 0;

    public Lesson(JsonObject object) {
        this.name = object.get("name").getAsString();
        this.desc = object.get("description").getAsString();
        this.id = object.get("id").getAsInt();
        this.bookID = object.get("book_id").getAsInt();

        if (MyApplication.getSharedPreferences().getBoolean("active",false))
            this.status = LessonStatus.Free.getValue();
        else
            this.status = object.get("status").getAsInt();

        if (object.get("words_count") != null)
            this.wordsCount = object.get("words_count").getAsInt();

        if (object.get("studied_count") != null)
            this.studiedCount = object.get("studied_count").getAsInt();

    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBookID() {
        return bookID;
    }

    public LessonStatus getStatus() {
        return EnumUtils.valueOf(LessonStatus.class, status);
    }

    public Integer getWordsCount() {
        return wordsCount;
    }

    public Integer getStudiedCount() {
        return studiedCount;
    }
}
