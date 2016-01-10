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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.android.eventsapp.AttendeeAdapter;
import com.linkedin.android.eventsapp.Constants;
import com.linkedin.android.eventsapp.Event;
import com.linkedin.android.eventsapp.MainActivity;
import com.linkedin.android.eventsapp.Person;
import com.linkedin.android.eventsapp.ProfileActivity;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventFragment extends Fragment {
    public static final String TAG = "com.linkedin.android.events.EventFragment";
	public static final String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";
    public static final String EXTRA_PICTURE_ID = "EXTRA_PICTURE_ID";
    public static final String EXTRA_EVENT_LOCATION = "EXTRA_EVENT_LOCATION";
    public static final String EXTRA_EVENT_DATE = "EXTRA_EVENT_DATE";
    public static final String EXTRA_EVENT_ATTENDING = "EXTRA_EVENT_ATTENDING";
    public static final String EXTRA_EVENT_ATTENDEES = "EXTRA_EVENT_ATTENDEES";

    public static final EventFragment newInstance(Event event)
	{
		EventFragment f = new EventFragment();
		Bundle bdl = new Bundle();
	    bdl.putString(EXTRA_EVENT_NAME, event.getEventName());
        bdl.putInt(EXTRA_PICTURE_ID, event.getImageResourceId());
        bdl.putString(EXTRA_EVENT_LOCATION, event.getEventLocation());
        bdl.putLong(EXTRA_EVENT_DATE, event.getEventDate());
        bdl.putBoolean(EXTRA_EVENT_ATTENDING, event.getIsAttending());
        bdl.putParcelableArray(EXTRA_EVENT_ATTENDEES, event.getEventAttendees());
	    f.setArguments(bdl);
	    return f;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		final String eventNameArg = getArguments().getString(EXTRA_EVENT_NAME);
        final long eventDateArg = getArguments().getLong(EXTRA_EVENT_DATE);
        final String eventLocationArg = getArguments().getString(EXTRA_EVENT_LOCATION);
        int pictureIdArg = getArguments().getInt(EXTRA_PICTURE_ID);
        boolean isAttendingArg = getArguments().getBoolean(EXTRA_EVENT_ATTENDING);
        Person[] attendeesArg = (Person[]) getArguments().getParcelableArray(EXTRA_EVENT_ATTENDEES);

        SimpleDateFormat dateFormat = new SimpleDateFormat ("E dd MMM yyyy 'at' hh:00 a");
        final String dateString = dateFormat.format(new Date(eventDateArg));

        View v = inflater.inflate(R.layout.layout_event_fragment, container, false);

        boolean accessTokenValid = LISessionManager.getInstance(getActivity()).getSession().isValid();
        if(!accessTokenValid) {
            ViewStub linkedinLogin = (ViewStub)v.findViewById(R.id.connectWithLinkedInStub);
            linkedinLogin.inflate();

            Button loginButton = (Button)v.findViewById(R.id.connectWithLinkedInButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "clicked login button");
                    LISessionManager.getInstance(getActivity()).init(getActivity(), Constants.scope, new AuthListener() {
                        @Override
                        public void onAuthSuccess() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onAuthError(LIAuthError error) {

                        }
                    }, false);
                }
            });
        }

		TextView eventNameView = (TextView)v.findViewById(R.id.eventName);
        eventNameView.setText(eventNameArg);


        TextView eventLocationAndDateView = (TextView)v.findViewById(R.id.eventLocationAndDate);
        eventLocationAndDateView.setText(eventLocationArg + "   " + dateString);

        TextView eventAttendeeCount = (TextView)v.findViewById(R.id.attendeeCount);
        eventAttendeeCount.setText("Other Attendees (" + attendeesArg.length + ")");

        ImageView eventImageView = (ImageView)v.findViewById(R.id.eventImage);
        eventImageView.setImageResource(pictureIdArg);

        final Button attendButton = (Button)v.findViewById(R.id.attendButton);
        final Button declineButton = (Button)v.findViewById(R.id.declineButton);

        if(isAttendingArg) {
            attendButton.setText("Attending");
            attendButton.setEnabled(false);

            declineButton.setText("Decline");
            declineButton.setEnabled(true);
        }

        attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button)v).setText("Attending");
                v.setEnabled(false);
                declineButton.setText("Decline");
                declineButton.setEnabled(true);
                if(LISessionManager.getInstance(getActivity()).getSession().isValid()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Share on LinkedIn?");
                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    JSONObject shareObject = new JSONObject();
                                    try {
                                        JSONObject visibilityCode = new JSONObject();
                                        visibilityCode.put("code", "anyone");
                                        shareObject.put("visibility",visibilityCode);
                                        shareObject.put("comment", "I am attending " + eventNameArg + " in " + eventLocationArg + " on " + dateString);
                                    } catch (JSONException e) {

                                    }
                                    APIHelper.getInstance(getActivity()).postRequest(getActivity(), Constants.shareBaseUrl, shareObject, new ApiListener() {
                                        @Override
                                        public void onApiSuccess(ApiResponse apiResponse) {
                                            Toast.makeText(getActivity(), "Your share was successful!", Toast.LENGTH_LONG);
                                        }

                                        @Override
                                        public void onApiError(LIApiError apiError) {
                                            Log.e(TAG, apiError.toString());
                                            Toast.makeText(getActivity(), "Your share was unsuccessful. Try again later!", Toast.LENGTH_LONG);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button)v).setText("Declined");
                v.setEnabled(false);
                attendButton.setText("Attend");
                attendButton.setEnabled(true);
            }
        });

        ListView attendeesListView = (ListView)v.findViewById(R.id.attendeesList);
        AttendeeAdapter adapter = new AttendeeAdapter(getActivity(), R.layout.attendee_list_item, attendeesArg, accessTokenValid);
        attendeesListView.setAdapter(adapter);
		attendeesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
                Person person = (Person) adapter.getItem(position);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("person", person);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        return v;
    }
}
