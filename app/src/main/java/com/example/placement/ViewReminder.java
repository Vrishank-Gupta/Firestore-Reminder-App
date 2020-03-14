package com.example.placement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.placement.util.ReminderAdapter;

import java.util.List;

public class ViewReminder extends AppCompatActivity {

    ListView lvRem;
    Button btnDis;
    ReminderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        lvRem = findViewById(R.id.lvRem);
        btnDis = findViewById(R.id.btnDis);

        adapter = new ReminderAdapter(this,R.layout.reminder_item,Main2Activity.reminderArrayList);
        lvRem.setAdapter(adapter);


        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvRem.setAdapter(adapter);
            }
        });
    }
}