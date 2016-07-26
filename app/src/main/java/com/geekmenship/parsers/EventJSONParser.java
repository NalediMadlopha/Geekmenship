package com.geekmenship.parsers;

import com.geekmenship.model.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventJSONParser {

    public static List<Event> parseFeed(String content) {

        try {
            JSONArray jsonArray = new JSONArray(content);
            ArrayList<Event> eventList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Event event = new Event();

                event.setId(jsonObject.getInt("id"));
                event.setCommunityName(jsonObject.getString("community_name"));
                event.setTitle(jsonObject.getString("title"));
                event.setDescription(jsonObject.getString("description"));
                event.setVenue(jsonObject.getString("venue"));
                event.setDate(jsonObject.getString("date"));
                event.setTime(jsonObject.getString("time"));
                event.setPhoto(jsonObject.getString("photo"));

                eventList.add(event);
            }

            return eventList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
