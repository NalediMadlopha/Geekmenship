package com.geekmenship.app;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class EventFragment extends Fragment {

    static String[] month ={
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        ListAdapter myArrayAdapter =
                new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_list_item_1, month);
//        setListAdapter(myArrayAdapter);

    }
}
