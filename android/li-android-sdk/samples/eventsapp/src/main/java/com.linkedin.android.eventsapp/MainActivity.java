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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.linkedin.android.eventsapp.Event;
import com.linkedin.android.eventsapp.EventFragment;
import com.linkedin.android.eventsapp.EventsManager;
import com.linkedin.android.eventsapp.Person;
import com.linkedin.platform.LISessionManager;

import java.util.*;
import java.text.*;

public class MainActivity extends FragmentActivity {
	public static final String TAG = MainActivity.class.getCanonicalName();
	private ViewPager pager;
	private com.linkedin.android.eventsapp.EventTabsAdapter mEventTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pager = new ViewPager(this);
		pager.setId(R.id.pager);
        pager.setOffscreenPageLimit(5);
		setContentView(pager);

		final ActionBar bar = getActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.layout_action_bar, null);

        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        textviewTitle.setText("UPCOMING EVENTS");
        bar.setCustomView(viewActionBar, params);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setIcon(new ColorDrawable(Color.TRANSPARENT));
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F15153")));

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mEventTabsAdapter = new com.linkedin.android.eventsapp.EventTabsAdapter(this, pager);
        SimpleDateFormat ft = new SimpleDateFormat ("E dd MMM");
        ArrayList<Event> events = EventsManager.getInstance(this).getEvents();
        for (Event event : events) {
            String eventDay = ft.format(new Date(event.getEventDate()));
            mEventTabsAdapter.addTab(bar.newTab().setText(eventDay), EventFragment.class, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }
}
