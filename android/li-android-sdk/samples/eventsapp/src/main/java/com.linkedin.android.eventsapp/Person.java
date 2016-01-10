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

public class Person implements Parcelable {
    private String mLinkedinId;
    private String mFirstName;
    private String mLastName;
    private String mHeadline;

    public Person() {

    }

    public Person(String linkedinId, String firstName, String lastName, String headline) {
        mLinkedinId = linkedinId;
        mFirstName = firstName;
        mLastName = lastName;
        mHeadline = headline;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String headline) {
        this.mHeadline = headline;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLinkedinId() {
        return mLinkedinId;
    }

    public void setLinkedinId(String linkedinId) {
        this.mLinkedinId = linkedinId;
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getLinkedinId());
        dest.writeString(this.getFirstName());
        dest.writeString(this.getLastName());
        dest.writeString(this.getHeadline());
    }

    public Person(Parcel source) {
        mLinkedinId = source.readString();
        mFirstName = source.readString();
        mLastName = source.readString();
        mHeadline = source.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
