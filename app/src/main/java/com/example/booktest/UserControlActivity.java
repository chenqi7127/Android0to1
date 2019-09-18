package com.example.booktest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UserControlActivity extends LinearLayout implements View.OnClickListener {

    public UserControlActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
        //加载需要的layout,还是使用之前的引用布局中的layout root制定其父布局
        LayoutInflater.from(context).inflate(R.layout.layout_placetest,this);
        //设置按钮的监听和之前的一致
        Button button1 = findViewById(R.id.placeTest_button1);
        Button button2 = findViewById(R.id.placeTest_button2);
        button1.setOnClickListener(this);
        button1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "反正就是点击了", Toast.LENGTH_SHORT).show();
    }
}
