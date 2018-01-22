package com.hillavas.filmvazhe.utils;

import com.google.gson.JsonObject;
import com.hillavas.filmvazhe.model.BaseEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arashjahani on 10/30/2016 AD.
 */
public class EnumUtils {


    public static <E extends BaseEnum> Map<Integer, String> toMap(final Class<E> enumClass) {
        final Map<Integer, String> map = new HashMap<>();
        for (final E e : enumClass.getEnumConstants()) {
            map.put(e.getValue(), e.getTitle());
        }
        return map;
    }

    public static <T extends BaseEnum> T valueOf(final Class<T> enumClass, Integer value) {
        for (final T item : enumClass.getEnumConstants()) {
            if (item.getValue()== value) {
                return (T) item;
            }
        }
        return null;
    }

    public static <T extends BaseEnum> T valueOf(final Class<T> enumClass, Integer value, T defaultValue) {
        T t = valueOf(enumClass, value);
        if (t == null) {
            return defaultValue;
        } else {
            return t;
        }
    }

    public static <T extends BaseEnum> T valueOf(final Class<T> enumClass, String desc) {
        for (final T item : enumClass.getEnumConstants()) {
            if (item.getTitle().equals(desc)) {
                return (T) item;
            }
        }
        return null;
    }

    public static JsonObject toJson(BaseEnum enumItem) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", enumItem.getValue());
        jsonObject.addProperty("name", enumItem.getTitle());
        return jsonObject;
    }

    public static <T extends BaseEnum> JsonObject toJson(final Class<T> enumClass, Integer value) {
        T enumItem = valueOf(enumClass, value);
        return toJson(enumItem);
    }
}
