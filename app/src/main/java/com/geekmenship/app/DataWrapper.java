package com.geekmenship.app;

import com.geekmenship.model.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataWrapper implements Serializable {

    private List<Event> events;

    public DataWrapper(List<Event> data) {
        this.events = data;
    }

    public List<Event> getEvents() {
        return this.events;
    }

}