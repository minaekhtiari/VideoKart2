package com.hillavas.filmvazhe.utils;



import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;

import org.joda.time.DateTime;




/**
 * Created by ArashJahani on 5/20/2015.
 */
public class DateFormat {



    public static String getTimeLeft(Long date) {

        if (date == 0) {
            return MyApplication.getContext().getString(R.string.now);
        }

        Long now = DateTime.now().getMillis() / 1000;
        long diff = (now - date); // by second

        int day = (int) (diff / (60 * 60 * 24));
        int hour = (int) (diff / (60 * 60));
        int minute = (int) (diff / 60);
        int second = (int) diff;

        if (diff > (86400*7)) // one Week
        {
            return  PersianDateTime.valueOf(date*1000).toDateString();
        }
        if (diff > 86400) // one day
        {
            return day + " " + MyApplication.getContext().getString(R.string.day)+" پیش";
        }
        if (diff > 3600) // one hour
        {
            return hour + " " + MyApplication.getContext().getString(R.string.hours)+" پیش";
        } else if (diff > 60) // minutes
        {
            return minute + " " + MyApplication.getContext().getString(R.string.minuts)+" پیش";
        } else if (diff <= 60) {
            //return second + " " + MyApplication.getContext().getString(R.string.second);
            return MyApplication.getContext().getString(R.string.now);
        } else if (diff == 0) {
            return MyApplication.getContext().getString(R.string.now);
        } else {
            return  PersianDateTime.valueOf(date*1000).toDateString();
        }

    }

    public static String getDate(Long date) {
        DateTime d = new DateTime(date);
        PersianDateTime adDate = PersianDateTime.valueOf(d);

        return  getDay(adDate)+getMonth(adDate.getMonth()) + getYear(adDate);
    }

    public static String getMonth(int num) {
        String month = "";
//        ConvertDate d = ConvertDate.Now();
        switch (num) {
            case 1:
                month = " فروردین ";
                break;
            case 2:
                month = " اردیبهشت ";
                break;
            case 3:
                month = " خرداد ";
                break;
            case 4:
                month = " تیر ";
                break;
            case 5:
                month = " مرداد ";
                break;
            case 6:
                month = " شهریور ";
                break;
            case 7:
                month = " مهر ";
                break;
            case 8:
                month = " آبان ";
                break;
            case 9:
                month = " آذر ";
                break;
            case 10:
                month = " دی ";
                break;
            case 11:
                month = " بهمن ";
                break;
            case 12:
                month = " اسفند ";
                break;
            default:
                break;
        }
        return month;
    }

    private static String getWeek(DateTime date) {
        String week = "";
        switch (date.getDayOfWeek()) {
            case 1:
                week = "دو شنبه";
                break;
            case 2:
                week = "سه شنبه";
                break;
            case 3:
                week = "چهار شنبه";
                break;
            case 4:
                week = "پنج شنبه";
                break;
            case 5:
                week = "جمعه";
                break;
            case 6:
                week = "شنبه";
                break;
            case 7:
                week = "یک شنبه";
                break;
            default:
                break;
        }
        return week;
    }

    private static String getDay(PersianDateTime date) {
        return String.valueOf(date.getDay());

    }

    private static String getYear(PersianDateTime date) {
        return String.valueOf(date.getYear());

    }


}
