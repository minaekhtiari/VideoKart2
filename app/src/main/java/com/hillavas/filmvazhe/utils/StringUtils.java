package com.hillavas.filmvazhe.utils;

import java.net.URLEncoder;

/**
 * Created by Arash on 15/09/06.
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String removeSpecialCharacter(String result) {
        try {
            result = URLEncoder.encode(result, "UTF-8");
            result = result.replaceAll("=", "");
            result = result.replaceAll("/", "");
            result = result.replaceAll("%", "");
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String toSummary(String value, Integer max) {
        if (value.length() > max) {
            return value.substring(0, max) + "...";
        } else return value;
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
