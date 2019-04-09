package org.bba.utils;

import com.google.gson.Gson;

import java.util.Map;
import java.util.function.BiConsumer;

public class ResultFormatter {

    public static String getJsonString(Map map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public static String getDashboardJson(Map map) {
        StringBuilder stringBuilder = new StringBuilder("{");
        if (map.entrySet().size() > 1) {
            System.out.println("[WARNING]: numerical result map contains " + map.entrySet().size() + " values, expected 1.");
        }
        map.forEach((k, v) -> {
            stringBuilder.append("\"value\":" + (int) (Double.valueOf(v.toString()).doubleValue() * 100));
            stringBuilder.append(",\"start\":0");
            stringBuilder.append(",\"end\":100");
        });
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static String getDashboardMoreJson(Map map, String title, String subTitle) {
        StringBuilder stringBuilder = new StringBuilder("{\"graph\":{");
        stringBuilder.append("\"title\":\""+title+"\",");
        stringBuilder.append("\"datasequences\":\"[{");
        stringBuilder.append("\"title\":\""+subTitle+"\",");
        stringBuilder.append("\"datapoints\":\"[{");

        map.forEach((k, v) -> {
            stringBuilder.append("\"title\":" + (int) (Double.valueOf(v.toString()).doubleValue() * 100));
            stringBuilder.append(",\"value\":0");
            stringBuilder.append(",\"end\":100");
        });
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static String getSingleValue(Map map) {
        if (map.entrySet().size() > 1) {
            System.out.println("[WARNING]: numerical result map contains " + map.entrySet().size() + " values, expected 1.");
        }
        return String.valueOf(map.values().toArray()[0]);
    }
}
