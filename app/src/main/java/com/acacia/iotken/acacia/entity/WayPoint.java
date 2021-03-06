package com.acacia.iotken.acacia.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * WayPointを保持するクラスです。
 * 親はMeasurement
 */
public class WayPoint {
    public double latitude;         // 緯度（-90 〜 +90）
    public double longitude;        // 緯度（-180 〜 +180）

    public WayPoint() {
        // Default constructor required for calls to DataSnapshot.getValue(WayPoint.class)
    }

    public WayPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
