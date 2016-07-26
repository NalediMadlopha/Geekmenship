package com.geekmenship.parsers;

import com.geekmenship.model.Event;
import com.geekmenship.model.Geek;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeekJSONParser {

    public static List<Geek> parseFeed(String content) {

        try {
            JSONArray jsonArray = new JSONArray(content);
            ArrayList<Geek> geekList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Geek geek = new Geek();

                geek.setId(jsonObject.getInt("id"));
                geek.setFirstName(jsonObject.getString("first_name"));
                geek.setLastName(jsonObject.getString("last_name"));
                geek.setEmail(jsonObject.getString("email"));
                geek.setDescription(jsonObject.getString("description"));
                geek.setPassword(jsonObject.getString("password"));
                geek.setProfilePicture(jsonObject.getString("photo"));
                geek.setCommunityName(jsonObject.getString("community_name"));
                geek.setCommunityDescription(jsonObject.getString("community_description"));

                geekList.add(geek);
            }

            return geekList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
