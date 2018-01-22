package com.hillavas.filmvazhe.model;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by Arash on 16/06/25.
 */
public class Clip implements Serializable {


    private final String videoUrl;
    Integer id;
    Integer dialogId;
    Integer wordId;
    // Integer subtitleId;
    Integer movieId;

    Integer clipStart;
    Integer clipEnd;
    String dialogText;

    String wordName;
    String wordMeaning;
    String movieName;

    Boolean bookmark = false;
    Boolean liked = false;
    Boolean studied = false;
    Integer likeCount=0, viewCount;

    Integer duration;

    Word word;

    public Clip(JsonObject jsonObject) {

        this.id = jsonObject.get("id").getAsInt();

        this.duration = jsonObject.get("length").getAsInt();

        this.clipStart = jsonObject.get("start").getAsInt();
        this.clipEnd = jsonObject.get("end").getAsInt();

        this.dialogText = jsonObject.getAsJsonObject("dialog").get("text").getAsString();

        this.dialogId = jsonObject.getAsJsonObject("dialog").get("id").getAsInt();
        this.movieId = jsonObject.get("movie").getAsJsonObject().get("id").getAsInt();

        this.wordName = jsonObject.getAsJsonObject("word").get("name").getAsString();
        this.wordMeaning = jsonObject.getAsJsonObject("word").get("meaning").getAsString();
        this.movieName = jsonObject.get("movie").getAsJsonObject().get("name").getAsString();

        this.videoUrl = jsonObject.get("video_url").getAsString();

        this.word = new Word(jsonObject.getAsJsonObject("word"));

        //this.liked = JsonUtils.getNullSafe(jsonObject, "liked", false);
//        this.viewCount = JsonUtils.getNullSafe(jsonObject, "view_count", 0);
        this.liked = jsonObject.get("liked").getAsBoolean();
        //this.likeCount = jsonObject.get("like_count").getAsInt();
        this.viewCount = jsonObject.get("view_count").getAsInt();
//        }
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public Word getWord() {
        return word;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public Boolean getLiked() {
        return liked;
    }

    public Boolean getStudied() {
        return studied;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public Integer getdialogId() {
        return dialogId;
    }

    public Integer getWordId() {
        return wordId;
    }

//    public Integer getSubtitleId() {
//        return subtitleId;
//    }


    public Integer getClipStart() {
        return clipStart;
    }

    public Integer getClipEnd() {
        return clipEnd;
    }

    public String getDialogText() {
        return dialogText;
    }

    public String getdialogText() {
        return dialogText;
    }

    public void setdialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordMeaning() {
        return wordMeaning;
    }

    public void setWordMeaning(String wordMeaning) {
        this.wordMeaning = wordMeaning;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getDialogId() {
        return dialogId;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
