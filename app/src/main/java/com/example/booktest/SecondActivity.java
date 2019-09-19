package com.example.booktest;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SecondActivity extends AppCompatActivity {
    private static String dataKey = "data1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        String data = intent.getStringExtra(dataKey);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(762);
    }

    public static void actionStart(Context context,String data)
    {
        Intent intent = new Intent(context,SecondActivity.class);
        intent.putExtra(dataKey,data);
        context.startActivity(intent);
    }

    public void returnData()
    {
        Intent intent = new Intent();
        intent.putExtra("ReturnResult","返回的信息");
        setResult(RESULT_OK,intent);
        finish();
    }
}
