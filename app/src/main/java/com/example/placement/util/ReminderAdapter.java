package com.example.placement.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.placement.DisableReminder;
import com.example.placement.MainActivity;
import com.example.placement.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    Gson gson;
    public static final String TAG = "Reminder List";

    private Context mContext;
    ArrayList<Reminder> reminderArrayList;
    int mResource;
    DocumentReference documentReference;
    public ReminderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Reminder> reminderArrayList, DocumentReference documentReference) {
        super(context, resource, reminderArrayList);
        mContext = context;
        this.documentReference = documentReference;
        this.reminderArrayList = reminderArrayList;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String subject = getItem(position).getSubject();
        String email = getItem(position).getEmail();
        String number = getItem(position).getContact();
        String desc = getItem(position).getDescription();
        int freq = getItem(position).getDay();
        final boolean status = getItem(position).isStatus();
        gson = new Gson();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvSubject, tvDesc, tvEmail, tvContact, tvFreq, tvStatus;
        CheckBox cb;

        tvContact = (TextView) convertView.findViewById(R.id.tvContact);
        tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
        tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
        tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        tvFreq= (TextView) convertView.findViewById(R.id.tvFreq);
        tvSubject  = (TextView) convertView.findViewById(R.id.tvSubject);
        cb = (CheckBox) convertView.findViewById(R.id.cbList);

        tvContact.setText(number);
        tvDesc.setText(desc);
        tvEmail.setText(email);
        tvSubject.setText(subject);
        tvFreq.setText("Frequency " + freq);
        if(status == true)
        {
            tvStatus.setText("Active");
        }

        else
            tvStatus.setText("Inactive");

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    getItem(position).setStatus(false);

                    String jsonReminder = gson.toJson(reminderArrayList);
                    MainActivity.map.put("Reminders", jsonReminder);
                    documentReference.set(MainActivity.map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mContext, "Pushed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if(!isChecked)
                {
                    getItem(position).setStatus(true);

                    String jsonReminder = gson.toJson(reminderArrayList);
                    MainActivity.map.put("Reminders", jsonReminder);
                    documentReference.set(MainActivity.map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mContext, "Pushed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return convertView;
    }
}
