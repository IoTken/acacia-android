package com.acacia.iotken.acacia.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * WayPointのMeasurementId以下を保持するクラスです。
 * 親はWayPointData
 * 子はWayPoint
 */
public class Measurement {
    public Map<String, WayPoint> wayPointMap;

    public Measurement() {
        this.wayPointMap = new HashMap<>();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        for (Map.Entry<String, WayPoint> wayPoint : wayPointMap.entrySet())
            result.put(wayPoint.getKey(), wayPoint.getValue().toMap());

        return result;
    }
}