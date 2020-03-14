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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyReminder extends AppCompatActivity {

    ImageView imDate;
    Calendar calendar;
    DatePickerDialog dialog;
    TextView tvDate;
    String subject,description,email,number, date;
    CheckBox cb7,cb5,cb3,cb2;
    Spinner spinSubj, spinReminder;
    int day, reminderIndex;
    Button btnConfirm, btnClear, btnLogout;
    EditText etDesc,etEmail,etNumber;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_reminder);

        reminderIndex = -1;
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
        btnLogout = findViewById(R.id.btnLogOut);
        spinSubj = findViewById(R.id.spinSubject);
        spinReminder = findViewById(R.id.spinReminder);


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

                dialog = new DatePickerDialog(ModifyReminder.this, new DatePickerDialog.OnDateSetListener() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDate.getText().equals("") ||
                        etDesc.getText().toString().equals("") ||
                        etEmail.getText().toString().equals("") ||
                        etNumber.getText().toString().equals(""))
                {
                    Toast.makeText(ModifyReminder.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }

                if(!isEmailValid(etEmail.getText().toString()))
                {
                    Toast.makeText(ModifyReminder.this, "Invalid Email", Toast.LENGTH_SHORT).show();
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
                    if(reminderIndex!=-1) Main2Activity.reminderArrayList.set(reminderIndex, reminder);
                    Toast.makeText(ModifyReminder.this, "Submitted", Toast.LENGTH_SHORT).show();
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


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
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

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ModifyReminder.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ModifyReminder.this, MainActivity.class));
                        finish();
                    }
                });
    }
}
