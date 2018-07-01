package com.acacia.iotken.acacia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.acacia.iotken.acacia.R;
import com.acacia.iotken.acacia.entity.MeasurementData;
import com.acacia.iotken.acacia.model.FirebaseManager;
import com.acacia.iotken.acacia.model.Utilities;
import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.Map;

public class DebugActivity extends AppCompatActivity {

    private static final String TAG = "DebugActivity";

    private String mReadDate = "20080701";
    private ScrollView scrollView;

    // LogText
    private String mLogText = "";
    private static final int LOG_TEXT_COUNT_MAX = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_debug);

        scrollView = findViewById(R.id.scrollView);

        // Button Event
        findViewById(R.id.button_measurement_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick button_measurement_read");
                addLog("*** 読み込み ***\n");
                EditText editText = findViewById(R.id.editText_readDate);
                final String readDate = editText.getText().toString();
                FirebaseManager.readMeasurementData(new FirebaseManager.OnFinishedListener() {
                    @Override
                    public void onSuccess(Map<String, MeasurementData> result) {
                        addLog("date:\n" + readDate + "\n\n");
                        addLog("result:\n" +result + "\n\n");
                        addLog("*** 読み込み完了 ***\n\n");
                    }
                    @Override
                    public void onFailure(DatabaseError error) {
                        addLog("*** error:\n" + error + "\n\n");
                        addLog("*** 読み込み失敗 ***\n\n");
                    }
                }, readDate);
            }
        });

        findViewById(R.id.button_measurement_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick button_measurement_write");
                addLog("*** 書き込み ***\n\n");
                MeasurementData data = FirebaseManager.createMockMeasurementData();

                data.toMap().get("time");

                long unixTime = ((Long)data.toMap().get("time"));
                Date date = new Date(unixTime * 1000L);

                addLog("created at:\n" + date + "\n\n");
                addLog("date:\n" + Utilities.toDateString(unixTime * 1000L) + "\n\n");
                addLog("time:\n" + unixTime + "\n\n");
                addLog("data:\n" + data.toMap().toString() + "\n\n");

                // write data
                FirebaseManager.writeMeasurementData(data);

                addLog("*** 書き込み完了 ***\n\n");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        clearLog();
        addLog(FirebaseManager.isSingIn() ? "*** Firebase OK ***\n\n" : "*** Firebase NG Please Login. ***\n\n");
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

        scrollView.post(new Runnable() {
            public void run() {
                Log.d(TAG,"scrollView.FOCUS_DOWN");
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
            }
        });
    }
}
