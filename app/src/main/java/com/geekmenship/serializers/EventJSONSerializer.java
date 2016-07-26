package com.geekmenship.serializers;

import com.geekmenship.model.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EventJSONSerializer {

    public static TreeMap<String, String> serializeFeed(Event event) {

        try {
            Map<String, String> eventParams = new HashMap<>();

            eventParams.put("title", event.getTitle());
            eventParams.put("description", event.getDescription());
            eventParams.put("venue", event.getVenue());
            eventParams.put("date", event.getDate());
            eventParams.put("time", event.getTime());
            eventParams.put("photo", event.getPhoto());

            if (event.getEncodedString() != null && !event.getEncodedString().isEmpty()) {
                eventParams.put("image", event.getEncodedString());
            }

            eventParams.put("table", "EVENT");

            Map<String, String> treeMap = new TreeMap<>(eventParams);

            return (TreeMap<String, String>) treeMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
