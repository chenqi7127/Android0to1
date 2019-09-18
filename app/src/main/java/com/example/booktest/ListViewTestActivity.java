package com.example.booktest;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewTestActivity extends AppCompatActivity {

    ArrayList<StudentInfo> infos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_list_view_test);
        infos.add(new StudentInfo("小明","1年纪"));
        infos.add(new StudentInfo("小明2","2年纪"));
        infos.add(new StudentInfo("小明2","3年纪"));
        infos.add(new StudentInfo("小明3","4年纪"));
        StudentInfoAdaper sia = new StudentInfoAdaper(ListViewTestActivity.this,R.layout.layout_studentinfo_item,infos);
        ListView lv = findViewById(R.id.listview_test);
        lv.setAdapter(sia);
    }
}

class StudentInfo
{
    String name;
    String grade;

    public StudentInfo(String sname,String sgrade)
    {
        name = sname;
        grade = sgrade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

class StudentInfoAdaper extends ArrayAdapter<StudentInfo>
{
    private int resourceID;
    public StudentInfoAdaper(Context context, int resource, List<StudentInfo> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentInfo sti = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        TextView nameText = view.findViewById(R.id.studentinfo_name);
        TextView gradeText = view.findViewById(R.id.studentinfo_grade);
        nameText.setText(sti.name);
        gradeText.setText(sti.grade);

       return view;

    }
}