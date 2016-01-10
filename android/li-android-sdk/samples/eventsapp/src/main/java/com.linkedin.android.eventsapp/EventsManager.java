/*
    Copyright 2015 LinkedIn Corp.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.linkedin.android.eventsapp;

import android.content.Context;

import com.linkedin.android.eventsapp.Event;
import com.linkedin.android.eventsapp.EventsHelper;

import java.util.ArrayList;

public class EventsManager {

    private static EventsManager sEventsManager;
    private Context mAppContext;
    private ArrayList<Event> mEvents;

    private EventsManager(Context ctx, ArrayList<Event> events) {
        mAppContext = ctx;
        mEvents = events;
    }

    public static EventsManager getInstance(Context ctx) {
        if (sEventsManager == null) {
            sEventsManager = new EventsManager(ctx.getApplicationContext(), EventsHelper.getEvents(ctx));
        }
        return sEventsManager;
    }

    public ArrayList<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(ArrayList<Event> events) {
        mEvents.clear();
        mEvents.addAll(events);
    }

    public Event getEvent(String eventName) {
        for(Event p : mEvents) {
            if (p.getEventName().equals(eventName)) {
                return p;
            }
        }
        return null;
    }
}
