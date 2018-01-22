package com.hillavas.filmvazhe.model;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by Arash on 16/06/25.
 */
public class Subtitle implements Serializable {

    Integer subtitleId;

    Integer dialogStart;
    Integer dialogEnd;
    String dialogText;



    public Subtitle(JsonObject jsonObject) {

        this.dialogStart =jsonObject.get("start").getAsInt();
        this.dialogEnd =jsonObject.get("end").getAsInt();
        this.dialogText =jsonObject.get("text").getAsString();
//        this.dialogueDuration=jsonObject.get("duration").getAsString();

    }


    public Integer getSubtitleId() {
        return subtitleId;
    }

    public Integer getDialogStart() {
        return dialogStart;
    }

    public void setDialogStart(Integer dialogStart) {
        this.dialogStart = dialogStart;
    }

    public Integer getDialogEnd() {
        return dialogEnd;
    }

    public void setDialogEnd(Integer dialogEnd) {
        this.dialogEnd = dialogEnd;
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

}
