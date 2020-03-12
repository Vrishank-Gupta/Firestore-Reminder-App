package com.example.placement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomePage extends AppCompatActivity {

    TextView tvWelcome, tvDate;

    Button btnSet, btnMod, btnDis, btnDel, btnEnab, btnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        tvDate = findViewById(R.id.tvDate);
        tvWelcome = findViewById(R.id.tvUserName);

        btnDel = findViewById(R.id.btnDelRem);
        btnDis = findViewById(R.id.btnDisRem);
        btnEnab = findViewById(R.id.btnEnaRem);
        btnMod = findViewById(R.id.btnModRem);
        btnSet = findViewById(R.id.btnSetRem);
        btnView = findViewById(R.id.btnViewRem);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd:MMM:yyyy");
        String formattedDate = df.format(c);

        tvDate.setText("Today is " + getCurrentDay() + " " +formattedDate);

        tvWelcome.setText("Welcome to the Reminder Application " + Main2Activity.name);


        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, NewReminder.class));
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, ViewReminder.class));
            }
        });

    }


    public String getCurrentDay(){

        String daysArray[] = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        return daysArray[day];

    }

}
