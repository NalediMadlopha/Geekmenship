package com.geekmenship.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekmenship.app.R;
import com.geekmenship.model.Event;
import com.geekmenship.app.util.GlobalConstant;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class EventDetailsAdapter extends ArrayAdapter<Event> {

    // Initialization
    private Context context;
    private int resource;
    private List<Event> eventList;

    private LruCache<Integer, Bitmap> imageCache;

    // Constructor(s)
    public EventDetailsAdapter(Context context, int resource,
                               List<Event> eventList) {
        super(context, resource, eventList);
        this.context = context;
        this.resource = resource;
        this.eventList = eventList;

        imageCache = new LruCache<>(GlobalConstant.cacheSize);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_event_details, parent, false);

        Event event = eventList.get(position);

        TextView eventCommunityNameTextView = (TextView) view.findViewById(R.id.communityNameTextView);
        eventCommunityNameTextView.setText(event.getCommunityName());

        TextView eventTitleTextView = (TextView) view.findViewById(R.id.eventTitleTextView);
        eventTitleTextView.setText(event.getTitle());

        TextView eventDescriptionTextView = (TextView) view.findViewById(R.id.eventDescriptionTextView);
        eventDescriptionTextView.setText(event.getDescription());

        TextView eventDateTextView = (TextView) view.findViewById(R.id.eventDateTextView);
        eventDateTextView.setText(event.getDate());

        Bitmap bitmap = imageCache.get(event.getId());
        if (bitmap != null) {
            ImageView image = (ImageView) view.findViewById(R.id.eventImageView);
            image.setImageBitmap(event.getBitmap());
        } else {
            EventAndView container = new EventAndView();
            container.event = event;
            container.view = view;

            new ImageLoader().execute(container);
        }

        return view;
    }

    class EventAndView {
        public Event event;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<EventAndView, Void, EventAndView> {

        InputStream inputStream = null;

        @Override
        protected EventAndView doInBackground(EventAndView... params) {

            EventAndView container = params[0];
            Event event = container.event;

            try {
                String imageUrl = GlobalConstant.PHOTOS_BASE_URL + event.getPhoto();
                inputStream = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                event.setBitmap(bitmap);

                container.bitmap = bitmap;
                return container;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(EventAndView result) {
            ImageView image = (ImageView) result.view.findViewById(R.id.eventImageView);
            image.setImageBitmap(result.bitmap);
//            result.event.setBitmap(result.bitmap);
            imageCache.put(result.event.getId(), result.bitmap);
        }
    }
}
