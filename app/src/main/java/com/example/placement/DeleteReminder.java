package com.example.placement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placement.util.Reminder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

public class DeleteReminder extends AppCompatActivity {

    ImageView imDate;
    Calendar calendar;
    DatePickerDialog dialog;
    TextView tvDate, tvDesc;
    String subject,description;
    Spinner spinSubj, spinReminder;
    int  reminderIndex;
    Button btnConfirm, btnLogout;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_reminder);

        tvDesc = findViewById(R.id.TvDesc);
        tvDesc.setText("Desc");
        reminderIndex = -1;
        imDate = findViewById(R.id.imDate);
        tvDate = findViewById(R.id.tvDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnLogout = findViewById(R.id.btnLogOut);
        spinSubj = findViewById(R.id.spinSubject);
        spinReminder = findViewById(R.id.spinReminder);
        description = "";

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        imDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                dialog = new DatePickerDialog(DeleteReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        tvDate.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                }, year,month,day);
                dialog.show();
            }
        });


        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.subjects,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSubj.setAdapter(adapter);

        spinSubj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayAdapter<Reminder> adapterReminder =
                new ArrayAdapter<Reminder>
                        (getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                Main2Activity.reminderArrayList);

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinReminder.setAdapter(adapterReminder);


        spinReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reminderIndex = position;
                description = Main2Activity.reminderArrayList.get(position).getDescription();
                try{
                    tvDesc.setText(description);
                    reminderIndex = position;
                }catch (Exception e)
                {
                    Log.e("TVDesc", "onItemSelected: "+ e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(reminderIndex != -1){
                  Main2Activity.reminderArrayList.remove(reminderIndex);
                  Toast.makeText(DeleteReminder.this, "Reminder Deleted", Toast.LENGTH_SHORT).show();

              }
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DeleteReminder.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteReminder.this, MainActivity.class));
                        finish();
                    }
                });
    }
}
