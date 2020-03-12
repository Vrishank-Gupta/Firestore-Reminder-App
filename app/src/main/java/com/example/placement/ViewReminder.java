package com.example.placement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.placement.util.ReminderAdapter;

import java.util.List;

public class ViewReminder extends AppCompatActivity {

    ListView lvRem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        lvRem = findViewById(R.id.lvRem);

        ReminderAdapter adapter = new ReminderAdapter(this,R.layout.reminder_item,Main2Activity.reminderArrayList);
        lvRem.setAdapter(adapter);

    }
}
