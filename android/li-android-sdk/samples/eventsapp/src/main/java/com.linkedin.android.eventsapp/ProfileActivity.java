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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.eventsapp.Constants;
import com.linkedin.android.eventsapp.Event;
import com.linkedin.android.eventsapp.FetchImageTask;
import com.linkedin.android.eventsapp.Person;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.DeepLinkListener;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends Activity {

    public static final String TAG = ProfileActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent() != null ? getIntent().getExtras() : new Bundle();
        final Person person = extras.getParcelable("person");
        final Activity currentActivity = this;
        final ActionBar bar = getActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.layout_action_bar, null);

        ImageView backView = (ImageView) viewActionBar.findViewById(R.id.actionbar_left);
        backView.setImageResource(R.drawable.arrow_left);
        backView.setVisibility(View.VISIBLE);
        backView.setClickable(true);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.finish();
            }
        });

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        bar.setCustomView(viewActionBar, params);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setIcon(new ColorDrawable(Color.TRANSPARENT));
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F15153")));

        TextView attendeeNameView = (TextView) findViewById(R.id.attendeeName);
        attendeeNameView.setText(person.getFirstName() + " " + person.getLastName());

        final ImageView attendeeImageView = (ImageView) findViewById(R.id.attendeeImage);
        final TextView attendeeHeadlineView = (TextView) findViewById(R.id.attendeeHeadline);
        final TextView attendeeLocationView = (TextView) findViewById(R.id.attendeeLocation);

        boolean isAccessTokenValid = LISessionManager.getInstance(currentActivity).getSession().isValid();
        if(isAccessTokenValid) {
            String url = Constants.personByIdBaseUrl + person.getLinkedinId() + Constants.personProjection;
            APIHelper.getInstance(currentActivity).getRequest(currentActivity, url, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    try {
                        JSONObject s = apiResponse.getResponseDataAsJson();
                        String headline = s.has("headline") ? s.getString("headline") : "";
                        String pictureUrl = s.has("pictureUrl") ? s.getString("pictureUrl") : null;
                        JSONObject location = s.getJSONObject("location");
                        String locationName = location != null && location.has("name") ? location.getString("name") : "";

                        attendeeHeadlineView.setText(headline);
                        attendeeLocationView.setText(locationName);
                        if (pictureUrl != null) {
                            new FetchImageTask(attendeeImageView).execute(pictureUrl);
                        } else {
                            attendeeImageView.setImageResource(R.drawable.ghost_person);
                        }
                    } catch (JSONException e) {

                    }

                }

                @Override
                public void onApiError(LIApiError apiError) {

                }
            });

            ViewStub viewOnLIStub = (ViewStub)findViewById(R.id.viewOnLIStub);
            View viewOnLI = viewOnLIStub.inflate();
            Button viewOnLIButton = (Button)viewOnLI.findViewById(R.id.viewOnLinkedInButton);
            viewOnLIButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeepLinkHelper.getInstance().openOtherProfile(currentActivity, person.getLinkedinId(), new DeepLinkListener() {
                        @Override
                        public void onDeepLinkSuccess() {

                        }

                        @Override
                        public void onDeepLinkError(LIDeepLinkError error) {

                        }
                    });
                }
            });
        } else {
            attendeeImageView.setImageResource(R.drawable.ghost_person);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }
}