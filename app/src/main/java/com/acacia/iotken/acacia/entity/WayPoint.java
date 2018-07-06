package com.acacia.iotken.acacia.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class WayPoint {
    public Double latitude;         // 緯度（-90 〜 +90）
    public Double longitude;        // 緯度（-180 〜 +180）

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
