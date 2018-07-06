package com.acacia.iotken.acacia.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class WayPointData {

    public Map<String, Measurement> measurementMap;

    public WayPointData() {
        this.measurementMap = new HashMap<>();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Measurement> measurement : measurementMap.entrySet())
            result.put(measurement.getKey(), measurement.getValue().toMap());

        return result;
    }
}
