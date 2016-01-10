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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchImageTask extends AsyncTask<String, Void, Bitmap> {

    public static final String TAG = FetchImageTask.class.getCanonicalName();
    ImageView attendeeImageView;

    public FetchImageTask(ImageView imageView) {
        super();
        attendeeImageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bm = null;
        try {
            URL imageURL = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();
            bm = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed image URL : " + urls[0], e);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching image from URL : " + urls[0], e);
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        attendeeImageView.setImageBitmap(bitmap);
    }
}