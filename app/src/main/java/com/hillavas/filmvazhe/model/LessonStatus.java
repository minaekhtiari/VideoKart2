package com.hillavas.filmvazhe.model;

/**
 * Created by arashjahani on 10/30/2016 AD.
 */

public enum LessonStatus implements BaseEnum {

    Disable(0,"غیر فعال"),
    Free(1,"رایگان"),
    Premium(2,"پولی");

    private Integer value;
    private String desc;

    LessonStatus(Integer val, String desc) {
        this.value = val;
        this.desc = desc;
    }


    public Integer getValue() {
        return value;
    }


    public String getTitle() {
        return desc;
    }


}
