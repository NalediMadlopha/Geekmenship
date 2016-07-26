package com.geekmenship.serializers;

import com.geekmenship.model.Event;
import com.geekmenship.model.Geek;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GeekJSONSerializer {

    public static TreeMap<String, String> serializeFeed(Geek geek) {

        try {
            Map<String, String> geekParams = new HashMap<>();

            geekParams.put("first_name", geek.getFirstName());
            geekParams.put("last_name", geek.getLastName());
            geekParams.put("email", geek.getEmail());
            geekParams.put("description", geek.getDescription());
            geekParams.put("password", geek.getPassword());
            geekParams.put("photo", geek.getProfilePicture());
            geekParams.put("organizer", Boolean.toString(geek.getEventOrganiser()));
            geekParams.put("community_name", geek.getCommunityName());
            geekParams.put("community_description", geek.getCommunityDescription());

            if (geek.getEncodedString() != null && !geek.getEncodedString().isEmpty()) {
                geekParams.put("image", geek.getEncodedString());
            }

            geekParams.put("table", "GEEK");

            Map<String, String> treeMap = new TreeMap<>(geekParams);

            return (TreeMap<String, String>) treeMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
