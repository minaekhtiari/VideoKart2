package com.hillavas.filmvazhe.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by arashjahani on 5/3/2017 AD.
 */

public class JsonUtils {

    public static <T> T getNullSafe(JsonObject json, String property, T defaultValue) {
        JsonElement element = json.get(property);
        if (element == null || element.isJsonNull()) {
            return defaultValue;
        } else {
            if (defaultValue instanceof Integer) {
                return (T) new Integer(element.getAsInt());
            } else if (defaultValue instanceof String) {
                return (T) element.getAsString();
            } else if (defaultValue instanceof Long) {
                return (T) new Long(element.getAsLong());
            } else if (defaultValue instanceof Boolean) {
                return (T) new Boolean(element.getAsBoolean());
            } else if (defaultValue instanceof Short) {
                return (T) new Short(element.getAsShort());
            } else if (defaultValue instanceof Float) {
                return (T) new Float(element.getAsFloat());
            } else if (defaultValue instanceof Byte) {
                return (T) new Byte(element.getAsByte());
            } else {
                return defaultValue;
            }
        }

    }
}
