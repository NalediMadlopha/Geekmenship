package com.geekmenship.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.geekmenship.app.R;
import com.geekmenship.model.Geek;
import com.geekmenship.model.GeekItemHolder;
import com.geekmenship.app.util.GlobalConstant;

public class SignUpAdapter extends ArrayAdapter<Geek> {
    
    private Geek[] objects;

    @Override
    public int getViewTypeCount() {
        return 11;
    } // Gets the number of view types

    @Override
    public int getItemViewType(int position) {
        return objects[position].getType();
    } // Gets the number of item view types

    // Constructor
    public SignUpAdapter(Context context, int resource, Geek[] objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GeekItemHolder viewHolder = null;
        Geek listViewItem = objects[position];
        int listViewItemType = getItemViewType(position);

        // Checks if the convert view is not equal to null
        if (convertView == null) {
            if (listViewItemType == GlobalConstant.FIRST_NAME) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_first_name_2, null);
            } else if (listViewItemType == GlobalConstant.LAST_NAME) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_last_name_2, null);
            } else if (listViewItemType == GlobalConstant.EMAIL) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_email_2, null);
            } else if (listViewItemType == GlobalConstant.DESCRIPTION) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_description_2, null);
            } else if (listViewItemType == GlobalConstant.PASSWORD) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_password_2, null);
            }  else if (listViewItemType == GlobalConstant.CONFIRM_PASSWORD) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_confirm_password_2, null);
            }  else if (listViewItemType == GlobalConstant.EVENT_ORGANIZER) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.check_box_event_organiser_2, null);
            }

            convertView.setTag(viewHolder); // Sets a tag
        }else {
            viewHolder = (GeekItemHolder) convertView.getTag();
        }

        // Returns the converted view
        return convertView;
    }
}