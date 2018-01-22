package com.hillavas.filmvazhe.utils;

/**
 * Created by Arash on 16/07/03.
 */
public class TimeFormat {


    public static String getLastTime(int totalSecs) {
        int sec=totalSecs/1000
                ;
        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = sec % 60;


        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        return String.format("%02d:%02d", minutes, seconds);
    }
}
