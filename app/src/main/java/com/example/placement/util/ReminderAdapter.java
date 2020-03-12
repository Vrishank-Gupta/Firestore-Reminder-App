package com.example.placement.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.placement.R;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    public static final String TAG = "Reminder List";

    private Context mContext;
    int mResource;
    public ReminderAdapter(@NonNull Context context, int resource, @NonNull List<Reminder> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String subject = getItem(position).getSubject();
        String email = getItem(position).getEmail();
        String number = getItem(position).getContact();
        String desc = getItem(position).getDescription();
        int freq = getItem(position).getDay();
        boolean status = getItem(position).isStatus();

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

        return convertView;
    }
}
