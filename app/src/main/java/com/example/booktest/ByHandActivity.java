package com.example.booktest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
        }
    }

    private void showSomeNotification() {

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "notification_simple";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "hideki", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(ByHandActivity.this,channelId)
                .setContentTitle("我也不知道些啥")
                .setContentText("这大概就是内容吧")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)).build();
        manager.notify(762,notification);
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
