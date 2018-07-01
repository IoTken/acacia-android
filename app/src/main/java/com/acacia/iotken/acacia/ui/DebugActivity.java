package com.acacia.iotken.acacia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.acacia.iotken.acacia.R;
import com.acacia.iotken.acacia.entity.MeasurementData;
import com.acacia.iotken.acacia.model.Firebase;
import com.acacia.iotken.acacia.model.Utilities;

import java.util.Date;

public class DebugActivity extends AppCompatActivity {

    private static final String TAG = "DebugActivity";

    // LogText
    private String mLogText = "";
    private static final int LOG_TEXT_COUNT_MAX = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_debug);

        // Button Event
        findViewById(R.id.button_measurement_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick button_measurement_read");
                addLog("*** 読み込み ***\n");
                Firebase.readMeasurementData("20180101");
                addLog("*** 読み込み完了 ***\n\n");
            }
        });

        findViewById(R.id.button_measurement_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick button_measurement_write");
                addLog("*** 書き込み ***\n\n");
                MeasurementData data = Firebase.createMockMeasurementData();

                data.toMap().get("time");

                long unixTime = ((Long)data.toMap().get("time"));
                Date date = new Date(unixTime * 1000L);

                addLog("created at:\n" + date + "\n\n");
                addLog("date:\n" + Utilities.toDateString(unixTime * 1000L) + "\n\n");
                addLog("time:\n" + unixTime + "\n\n");
                addLog("data:\n" + data.toMap().toString() + "\n\n");

                // write data
                Firebase.writeMeasurementData(data);

                addLog("*** 書き込み完了 ***\n\n");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        clearLog();
        addLog(Firebase.isSingIn() ? "*** Firebase OK ***\n\n" : "*** Firebase NG Please Login. ***\n\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearLog() {
        mLogText = "";

        // Update UI
        TextView textView = findViewById(R.id.text_log);
        textView.setText("");
    }

    private void addLog(String log) {

        int substringIndex = mLogText.length() + log.length() - LOG_TEXT_COUNT_MAX;

        if (substringIndex > 0) {
            mLogText.substring(substringIndex);
        }
        mLogText = mLogText + log;

        // Update UI
        TextView textView = findViewById(R.id.text_log);
        textView.setText(mLogText);

        final ScrollView scrollView = findViewById(R.id.scrollView);

        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, scrollView.getBottom());
            }
        });
    }
}