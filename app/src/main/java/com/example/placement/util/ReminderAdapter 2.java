package com.example.placement.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.placement.R;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    private static class ViewHolder {
        private TextView itemView;
    }

    public ReminderAdapter(Context context, int textViewResourceId, ArrayList<Reminder> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.reminder_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.ItemView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyClass item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(String.format("%s %d", item.reason, item.long_val));
        }

        return convertView;
    }
}
