package com.example.placement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.placement.util.Reminder;
import com.example.placement.util.ReminderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.placement.MainActivity.reminderArrayList;

public class ViewReminder extends AppCompatActivity {

    ListView lvRem;
    Button btnDis;
    ReminderAdapter adapter;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    Gson gson;
    ArrayList<Reminder> userArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        lvRem = findViewById(R.id.lvRem);
        btnDis = findViewById(R.id.btnDis);


        gson = new Gson();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());

        final Type userListType = new TypeToken<ArrayList<Reminder>>(){}.getType();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot snapshot = task.getResult();

                if(snapshot.exists() && snapshot!= null)
                {
                    Log.e("jsonReminder", "onComplete: " + snapshot.getData() );
                    String jsonReminder = snapshot.getString("Reminders");

                    if(jsonReminder == null || jsonReminder.equals("[]"))
                    {
                        reminderArrayList = new ArrayList<>();
                        jsonReminder = gson.toJson(reminderArrayList);
                        adapter = new ReminderAdapter(ViewReminder.this,R.layout.reminder_item,reminderArrayList,documentReference);
                        lvRem.setAdapter(adapter);
                        Log.e("jsonReminder", "onComplete: " );

                    }
                    else
                    {
                        Type userListType = new com.google.common.reflect.TypeToken<ArrayList<Reminder>>(){}.getType();
                        reminderArrayList = gson.fromJson(jsonReminder, userListType);
                        adapter = new ReminderAdapter(ViewReminder.this,R.layout.reminder_item,reminderArrayList,documentReference);
                        lvRem.setAdapter(adapter);
                        Log.e("JsonReminder", "onComplete: " + jsonReminder );

                    }
                }

            }
        });




        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvRem.setAdapter(adapter);
            }
        });
    }
}