package com.example.placement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placement.util.Reminder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.placement.MainActivity.reminderArrayList;

public class NewReminder extends AppCompatActivity {

    ImageView imDate;
    Calendar calendar;
    DatePickerDialog dialog;
    TextView tvDate;
    String subject,description,email,number, date;
    CheckBox cb7,cb5,cb3,cb2;
    Spinner spinSubj;
    int day;
    Button btnConfirm, btnClear;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    Gson gson;
    EditText etDesc,etEmail,etNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        gson = new Gson();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());

        etDesc = findViewById(R.id.etDesc);
        etEmail = findViewById(R.id.etEmail);
        etNumber = findViewById(R.id.etNumber);
        imDate = findViewById(R.id.imDate);
        tvDate = findViewById(R.id.tvDate);
        cb2 = findViewById(R.id.chk2);
        cb3 = findViewById(R.id.chk3);
        cb5 = findViewById(R.id.chk5);
        cb7 = findViewById(R.id.chk7);
        btnClear = findViewById(R.id.btnClear);
        btnConfirm = findViewById(R.id.btnConfirm);
        spinSubj = findViewById(R.id.spinSubject);

        imDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                dialog = new DatePickerDialog(NewReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        tvDate.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                }, year,month,day);
                dialog.show();
            }
        });





        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.subjects,android.R.layout.simple_spinner_item);
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


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDate.getText().equals("") || etDesc.getText().toString().equals("") || etEmail.getText().toString().equals("") || etNumber.getText().toString().equals(""))
                {
                    Toast.makeText(NewReminder.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }

                if(!isEmailValid(etEmail.getText().toString()))
                {
                    Toast.makeText(NewReminder.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    description = etDesc.getText().toString();

                    email = etEmail.getText().toString();
                    date = tvDate.getText().toString();
                    number = etNumber.getText().toString();
                    if(cb2.isChecked())
                    {
                        day = 2;
                    }

                    if(cb3.isChecked())
                    {
                        day = 3;
                    }

                    if(cb5.isChecked())
                    {
                        day = 5;
                    }

                    if(cb7.isChecked())
                    {
                        day = 7;
                    }

                    Reminder reminder = new Reminder(Main2Activity.name,email, date,subject, description,number, day, true);
                    Log.d("New Reminder", "onClick: " + reminder.toString());
                    reminderArrayList.add(reminder);
                    String gsonReminder = gson.toJson(reminderArrayList);
                    MainActivity.map.put("Reminders", gsonReminder);
                    documentReference.set(MainActivity.map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(NewReminder.this, "Pushed", Toast.LENGTH_SHORT).show();
                        }
                    });




                    Toast.makeText(NewReminder.this, "Submitted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDesc.setText("");
                etEmail.setText("");
                etNumber.setText("");
                cb7.setChecked(true);
                cb2.setChecked(false);
                cb3.setChecked(false);
                cb5.setChecked(false);
                tvDate.setText("");
            }
        });

        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb7.setChecked(false);
                }

            }
        });

        cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked) {
                   cb2.setChecked(false);
                   cb3.setChecked(false);
                   cb5.setChecked(false);
               }

            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cb5.setChecked(false);
                    cb3.setChecked(false);
                    cb7.setChecked(false);
                }

            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cb5.setChecked(false);
                    cb2.setChecked(false);
                    cb7.setChecked(false);
                }

            }
        });
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
