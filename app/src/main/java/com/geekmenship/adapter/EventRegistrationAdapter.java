package com.geekmenship.adapter;

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

import com.geekmenship.app.R;
import com.geekmenship.model.Event;
import com.geekmenship.app.util.GlobalConstant;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class EventRegistrationAdapter extends ArrayAdapter<Event> {

    // Initialization
    private Context context;
    private List<Event> eventList;

    private LruCache<Integer, Bitmap> imageCache;

    // Constructor
    public EventRegistrationAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
        this.eventList = events;

        imageCache = new LruCache<>(GlobalConstant.cacheSize);
    }




    private List<Event> objects;

    @Override
    public int getViewTypeCount() {
        return 5;
    } // Gets the number of view types

    @Override
    public int getItemViewType(int position) {
        return eventList.get(position).getType();
    } // Gets the number of item view types

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int listViewItemType = getItemViewType(position);

        if (listViewItemType == GlobalConstant.EVENT_TITLE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_event_title_2, null);
        } else if (listViewItemType == GlobalConstant.EVENT_DESCRIPTION) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_event_description_2, null);
        } else if (listViewItemType == GlobalConstant.EVENT_LOCATION) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_event_location_2, null);
        } else if (listViewItemType == GlobalConstant.EVENT_DATE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_event_date_2, null);
        } else if (listViewItemType == GlobalConstant.EVENT_TIME) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_text_event_time_2, null);
        }

        return convertView;
    }

    class EventAndView {
        public Event event;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<EventAndView, Void, EventAndView> {
        @Override
        protected EventAndView doInBackground(EventAndView... params) {

            EventAndView container = params[0];
            Event event = container.event;

            try {
                String imageUrl = GlobalConstant.PHOTOS_BASE_URL + event.getPhoto();
                InputStream inputStream = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                event.setBitmap(bitmap);
                inputStream.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e) {
                e.printStackTrace();
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