package com.example.booktest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ByHandActivity extends AppCompatActivity implements View.OnClickListener {

    private MyNetReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_by_hand);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
        {
            actionBar.hide();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transition = fm.beginTransaction();
        transition.replace(R.id.fragment_one,new FragmentOne());
        transition.commit();
        Button jumpButton = findViewById(R.id.by_hand_button1);
        jumpButton.setOnClickListener(this);
        Button hiddenJump = findViewById(R.id.by_hand_button2);
        hiddenJump.setOnClickListener(this);
        Button getDataBack = findViewById(R.id.by_hand_button3);
        getDataBack.setOnClickListener(this);
        Button sendNotify = findViewById(R.id.by_hand_button4);
        sendNotify.setOnClickListener(this);
        Button fragmentButton = findViewById(R.id.fragment_button);
        fragmentButton.setOnClickListener(this);
        Button swapButton = findViewById(R.id.fragment_swap);
        swapButton.setOnClickListener(this);
        Button saveFileButton = findViewById(R.id.save_file);
        saveFileButton.setOnClickListener(this);
        Button readFileButton = findViewById(R.id.read_file);
        readFileButton.setOnClickListener(this);
        Button takePhoto = findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(this);
        mReceiver = new MyNetReceiver();
        //声明一个IntentFilter并加入需要坚挺的action 然后通过registerReceiver来注册
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.setPriority(100); 如果是有序广播用来设置优先级
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.by_hand_button1:
                Intent listviewIntent = new Intent(this,ListViewTestActivity.class);
                startActivity(listviewIntent);
//                SecondActivity.actionStart(this,"from byhand to second!");
                break;
            case  R.id.by_hand_button2:
                //自定义的action和category
//                Intent intent = new Intent("com.action_start");
//                也可以加入自定义的Category 需要在Manifest中接收该Intent的Activity中的Intent-fliter
//                加入相应的Category 不写则默认为default
//                intent.addCategory("myCategory");

                //打开浏览器支持
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
                break;
            case R.id.by_hand_button3:
                Intent backIntent = new Intent(this,SecondActivity.class);
                //不是startActivity而是startActivityForResult
                startActivityForResult(backIntent,1);
                break;
            case R.id.by_hand_button4:
                showSomeNotification();
                break;
            case R.id.fragment_button:
                //先获取FragmentManager,然后通过findFragmentById获取
                FragmentThree fOne = (FragmentThree)getSupportFragmentManager().findFragmentById(R.id.fragment_one);
                fOne.SayIt();
                break;
            case R.id.fragment_swap:
                //获取fragmentManager不使用原生库 兼容性不好
                FragmentManager fm = getSupportFragmentManager();
                //开始切换
                FragmentTransaction transition = fm.beginTransaction();
                //传入需要切换的帧布局ID和 用来替换的fragment类
                transition.replace(R.id.fragment_one,new FragmentThree());
                //如果需要把切换的过程 放入活动的堆 则点击返回后会返回上一个碎片
                transition.addToBackStack(null);
                transition.commit();
                break;
            case R.id.save_file:
                BufferedWriter bufferedWriter = null;
                try {
                    FileOutputStream fileOutputStream = openFileOutput("viwesonic",Context.MODE_PRIVATE);
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                    bufferedWriter.write("让我们荡起双桨,小船儿推开波浪!!!!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (bufferedWriter != null){
                            bufferedWriter.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.read_file:
                BufferedReader bufferedReader = null;
                try {
                    FileInputStream fileInputStream = openFileInput("viwesonic");
                    bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    Toast.makeText(this, bufferedReader.readLine(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (bufferedReader!=null){
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.take_photo:
                Intent photoIntent = new Intent(this,TakePhotoActivity.class);
                startActivity(photoIntent);
                break;
        }
    }


    private void showSomeNotification() {
        createChannel();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(ByHandActivity.this,"normal");
        }
        else {
            builder = new NotificationCompat.Builder(ByHandActivity.this);
        }
        Intent intent = new Intent(this,SecondActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentTitle("我也不知道些啥");
        builder.setContentText("这大概就是内容吧");
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
//        builder.setFullScreenIntent(pendingIntent,true);
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.sunnyday)));
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("1212312313123123123211231231312312312321321123123131231231232112312313123123123213213123131231231232112312313123123123213213123123112312313123123123213213123123132131231231撒德哈空间"));
//        builder.setAutoCancel(true);
        Notification notification = builder.build();
        manager.notify(762,notification);
    }

    private NotificationChannel normalChannel;
    private NotificationChannel importantChannel;
    private void createChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            normalChannel = new NotificationChannel("normal", "普通消息通知", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(normalChannel);
            importantChannel = new NotificationChannel("important", "重要消息通知", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(importantChannel);
        }
    }

    //监听该事件,返回后会回调该方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            //需要和调用时的一致 主要是用来分辨是从哪个页面返回的
            case  1:
                //返回的状态为ok
                if (resultCode ==RESULT_OK)
                {
                    //data 就是返回的intent 就可以通过get的相关方法来获取返回的信息
                    String dataStr = data.getStringExtra("ReturnResult");
                }
        }
    }

    public void sendBroast()
    {
        Intent intent = new Intent("com.frommyself.test.msg");
        //发送普通广播
        sendBroadcast(intent);
        //发送有序广播
        sendOrderedBroadcast(intent,null);
        //发送本地广播只会被自己捕获
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
