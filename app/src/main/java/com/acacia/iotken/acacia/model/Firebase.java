package com.acacia.iotken.acacia.model;

import com.acacia.iotken.acacia.entity.MeasurementData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class Firebase {

    public static void writeMeasurementData(MeasurementData data) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("log").child(data.toDate()).child(data.toMap().get("time").toString()).updateChildren(data.toMap());
    }

    public static void readMeasurementData(String date) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("something");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
//                String foo = dataSnapshot.getValue(String.class);
//                // foo => "foo"

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
            }
        });
    }

    public static boolean isSingIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser() != null;
    }

    // Mock Data
    public static MeasurementData createMockMeasurementData() {

        MeasurementData data = new MeasurementData();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(cal.YEAR, -10);
        data.time = cal.getTimeInMillis()/1000; // CurrentDate - 10Years
        data.latitude = 36.01;
        data.longitude = 135.02;
        data.temperature = 24.3;
        data.humidity = 83.4;
        data.discomfort_index = 64.5;

        return data;
    }
}
