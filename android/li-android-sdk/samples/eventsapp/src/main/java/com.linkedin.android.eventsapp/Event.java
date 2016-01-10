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

import android.os.Parcel;
import android.os.Parcelable;

import com.linkedin.android.eventsapp.Person;

public class Event implements Parcelable {
    private String mEventName;
    private String mEventLocation;
    private int mImageResourceId;
    private long mEventDate;
    private Person[] mEventAttendees;
    private Boolean mIsAttending;

    public Event(String eventName, String eventLocation, int imageResourceId, long eventDate, Person[] eventAttendees) {
        mEventName = eventName;
        mEventLocation = eventLocation;
        mImageResourceId = imageResourceId;
        mEventDate = eventDate;
        mEventAttendees = eventAttendees;
        mIsAttending = false;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.mImageResourceId = imageResourceId;
    }

    public long getEventDate() {
        return mEventDate;
    }

    public void setEventDate(long eventDate) {
        this.mEventDate = eventDate;
    }

    public String getEventLocation() {
        return mEventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.mEventLocation = eventLocation;
    }

    public String getEventName() {
        return mEventName;
    }

    public void setEventName(String eventName) {
        this.mEventName = eventName;
    }

    public Person[] getEventAttendees() {
        return mEventAttendees;
    }

    public void setEventAttendees(Person[] eventAttendees) {
        this.mEventAttendees = eventAttendees;
    }

    public Boolean getIsAttending() {
        return mIsAttending;
    }

    public void setIsAttending(Boolean isAttending) {
        this.mIsAttending = isAttending;
    }

    @Override
    public String toString() {
        return this.getEventName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getEventName());
        dest.writeString(this.getEventLocation());
        dest.writeInt(this.getImageResourceId());
        dest.writeLong(this.getEventDate());
        dest.writeInt(this.getIsAttending() ? 1 : 0);
        dest.writeParcelableArray(this.getEventAttendees(), flags);
    }

    public Event(Parcel source) {
        mEventName = source.readString();
        mEventLocation = source.readString();
        mImageResourceId = source.readInt();
        mEventDate = source.readLong();
        mIsAttending = source.readInt() == 1 ? true : false;
        mEventAttendees = (Person[]) source.readParcelableArray(Person.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
