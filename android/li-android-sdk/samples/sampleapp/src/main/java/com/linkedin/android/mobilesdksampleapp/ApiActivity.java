/*
    Copyright 2014 LinkedIn Corp.

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

package com.linkedin.android.mobilesdksampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.APIHelper;


public class ApiActivity extends Activity {

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:(first-name,last-name,public-profile-url)";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        final Button makeApiCall = (Button) findViewById(R.id.makeApiCall);
        makeApiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
                apiHelper.getRequest(ApiActivity.this, topCardUrl, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse s) {
                        ((TextView) findViewById(R.id.response)).setText(s.toString());
                    }

                    @Override
                    public void onApiError(LIApiError error) {
                        ((TextView) findViewById(R.id.response)).setText(error.toString());
                    }
                });
            }
        });

        final Button makePostApiCall = (Button) findViewById(R.id.makePostApiCall);
        makePostApiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText shareComment = (EditText) findViewById(R.id.shareComment);
                String shareJsonText = "{ \n" +
                        "   \"comment\":\"" + shareComment.getText() + "\"," +
                        "   \"visibility\":{ " +
                        "      \"code\":\"anyone\"" +
                        "   }," +
                        "   \"content\":{ " +
                        "      \"title\":\"Test Share Title\"," +
                        "      \"description\":\"Leverage the Share API to maximize engagement on user-generated content on LinkedIn\"," +
                        "      \"submitted-url\":\"https://www.americanexpress.com/us/small-business/openforum/programhub/managing-your-money/?pillar=critical-numbers\"," +
                        "      \"submitted-image-url\":\"http://m3.licdn.com/media/p/3/000/124/1a6/089a29a.png\"" +
                        "   }" +
                        "}";
                APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
                apiHelper.postRequest(ApiActivity.this, shareUrl, shareJsonText, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {
                        ((TextView) findViewById(R.id.response)).setText(apiResponse.toString());
                    }

                    @Override
                    public void onApiError(LIApiError error) {
                        ((TextView) findViewById(R.id.response)).setText(error.toString());
                    }
                });
            }
        });

    }

}
