package com.acacia.iotken.acacia.entity;

import com.acacia.iotken.acacia.model.Utilities;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * 測定データを保持するクラスです。
 */
public class MeasurementData {

    public long time;            // 測定時間（UNIXTIME（秒））
    public double latitude;         // 緯度（-90 〜 +90）
    public double longitude;        // 緯度（-180 〜 +180）
    public double temperature;      // 温度（℃）
    public double humidity;         // 湿度（%）
    public double discomfort_index; // 不快指数

    public MeasurementData() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("time", time);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("temperature", temperature);
        result.put("humidity", humidity);
        result.put("discomfort_index", discomfort_index);

        return result;
    }

    public String toDate() {
        return Utilities.toDateString(time*1000L);
    }
}
