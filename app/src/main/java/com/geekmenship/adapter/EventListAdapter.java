package com.geekmenship.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekmenship.app.R;
import com.geekmenship.model.Event;
import com.geekmenship.model.EventItemHolder;

import java.util.List;

public class EventListAdapter extends ArrayAdapter<Event> {

    // Initialization
    private Context context;
    private List<Event> eventList;
    private int layoutID;

    // Constructor(s)
    public EventListAdapter(Context context, int layoutID,
                        List<Event> listEvents) {
        super(context, layoutID, listEvents);
        this.context = context;
        this.eventList = listEvents;
        this.layoutID = layoutID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventItemHolder eventHolder;
        View view = convertView;

        if (view == null) {
            //Declares an inflater
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            eventHolder = new EventItemHolder(); // Initializes a new event holder

            // Inflates the event item layout passed through the constructor
            view = inflater.inflate(layoutID, parent, false);

            // Initializes the event holders attributes
            eventHolder.communityName = (TextView) view.findViewById(R.id.communityNameTextView);
            eventHolder.eventTitle = (TextView) view.findViewById(R.id.eventTitleTextView);
            eventHolder.eventDescription = (TextView) view.findViewById(R.id.eventDescriptionTextView);
            eventHolder.eventDate = (TextView) view.findViewById(R.id.eventDateTextView);
            eventHolder.icon = (ImageView) view.findViewById(R.id.eventImageView);

			/* Associates the event holder with the view which 
			 * the event item layout has inflated with
			 */
            view.setTag(eventHolder);
        } else {
			/* Gets the  event item holder tag 
			 * If the view is empty
			 */
            eventHolder = (EventItemHolder) view.getTag();
        }

        // Declares and initializes a new event item
        Event event = (Event) this.eventList.get(position);

        // Sets the text for all the attributes of the event holder
        eventHolder.communityName.setText(event.getCommunityName());
        eventHolder.eventTitle.setText(event.getTitle());
        eventHolder.eventDescription.setText(event.getDescription());
        eventHolder.eventDate.setText(event.getDate());

        return view;
    }
}
