package com.acacia.iotken.acacia.model;

import android.util.Log;

import com.acacia.iotken.acacia.entity.Measurement;
import com.acacia.iotken.acacia.entity.MeasurementData;
import com.acacia.iotken.acacia.entity.WayPoint;
import com.acacia.iotken.acacia.entity.WayPointData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private static final String TAG = "FirebaseManager";


    /**
     * 測定データ読み込み完了リスナー
     */
    public interface OnFinishedMeasurementDataListener {
        /**
         * @param result
         */
        void onSuccess(Map<String, MeasurementData> result);

        void onFailure(DatabaseError databaseError);

    }

    /**
     * Waypoint読み込み完了リスナー
     */
    public interface OnFinishedWayPointDataListener {
        /**
         * @param result
         */
        void onSuccess(WayPointData result);

        void onFailure(DatabaseError databaseError);

    }

    // MeasurementData
    public static void writeMeasurementData(MeasurementData data) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("log").child(data.toDate()).child(data.toMap().get("time").toString()).updateChildren(data.toMap());
    }

    public static void readMeasurementData(final OnFinishedMeasurementDataListener listener, String date) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("log").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                Map<String, MeasurementData> measurementDataMap = (HashMap<String, MeasurementData>) dataSnapshot.getValue();

                Log.d(TAG, "onDataChange measurementDataMap:" + measurementDataMap);

                if (listener != null) {
                    listener.onSuccess(measurementDataMap);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.d(TAG, "onCancelled databaseError:" + databaseError);

                if (listener != null) {
                    listener.onFailure(databaseError);
                }
            }
        });
    }

    // WayPointData
    public static void writeWayPointData(WayPointData data) {
        String userId = FirebaseManager.userId();
        if (userId == null || userId.isEmpty()) {
            Log.d(TAG, "userId is null or empty");
            return;
        }
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(userId).child("way_point").setValue(data.toMap());
    }

    /**
     * measurementId以下を取得します。
     */
    public static void readWayPointData(final OnFinishedWayPointDataListener listener) {
        String userId = FirebaseManager.userId();
        if (userId == null || userId.isEmpty()) {
            Log.d(TAG, "userId is null or empty");
            return;
        }
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(userId).child("way_point").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange wayPointDataMap raw:" + dataSnapshot.getValue());

                WayPointData wayPointData = new WayPointData();
                wayPointData.measurementMap = (Map<String, Measurement>) dataSnapshot.getValue();

                for (DataSnapshot measurementSnapshot : dataSnapshot.getChildren()) {
                    Measurement measurement = new Measurement();
                    for (DataSnapshot wayPointSnapshot : measurementSnapshot.getChildren()) {
                        WayPoint wayPoint = wayPointSnapshot.getValue(WayPoint.class);
                        measurement.wayPointMap.put(wayPointSnapshot.getKey() ,wayPoint);
                    }
                    wayPointData.measurementMap.put(measurementSnapshot.getKey(), measurement);
                }

                if (listener != null) {
                    listener.onSuccess(wayPointData);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.d(TAG, "onCancelled databaseError:" + databaseError);

                if (listener != null) {
                    listener.onFailure(databaseError);
                }
            }
        });
    }

    // Firebase Utilities
    public static boolean isSingIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser() != null;
    }

    public static String userId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getUid();
    }

    // Mock Data
    public static MeasurementData createMockMeasurementData() {

        MeasurementData data = new MeasurementData();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(cal.YEAR, -10);
        data.time = cal.getTimeInMillis() / 1000; // CurrentDate - 10Years
        data.latitude = 36.01;
        data.longitude = 135.02;
        data.temperature = 24.3;
        data.humidity = 83.4;
        data.discomfort_index = 64.5;

        return data;
    }

    public static WayPointData createMockWayPointData1() {

        WayPoint wayPoint1 = new WayPoint(35.1, 136.1);
        WayPoint wayPoint2 = new WayPoint(35.1, 136.1);

        Measurement measurement = new Measurement();
        measurement.wayPointMap.put("way_point_id_1_1", wayPoint1);
        measurement.wayPointMap.put("way_point_id_1_2", wayPoint2);

        WayPointData wayPointData = new WayPointData();
        wayPointData.measurementMap.put("measurement_id_1", measurement);

        return wayPointData;
    }

    public static WayPointData createMockWayPointData2() {

        WayPoint wayPoint1 = new WayPoint(35.2, 136.2);
        WayPoint wayPoint2 = new WayPoint(35.2, 136.2);

        Measurement measurement = new Measurement();
        measurement.wayPointMap.put("way_point_id_2_1", wayPoint1);
        measurement.wayPointMap.put("way_point_id_2_2", wayPoint2);

        WayPointData wayPointData = new WayPointData();
        wayPointData.measurementMap.put("measurement_id_2", measurement);

        return wayPointData;
    }
}
