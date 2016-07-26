package com.geekmenship.dao;

import com.geekmenship.app.util.RequestPackage;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    public static String getData(RequestPackage requestPackage) {

        BufferedReader reader = null;
        String uri = requestPackage.getUri();

        if (requestPackage.getMethod().equals("GET")) {
            uri += "?" + requestPackage.getEncodeParams();
        }

        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestPackage.getMethod());

            JSONObject json = new JSONObject(requestPackage.getParams());
            String params = "params=" + json.toString();

            if (requestPackage.getMethod().equals("POST")) {
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(params);
                writer.flush();
            }

            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();
        } catch (Exception e ) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }
    }
}
