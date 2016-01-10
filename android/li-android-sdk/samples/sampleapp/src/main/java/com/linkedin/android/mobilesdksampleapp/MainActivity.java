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
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.LISession;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String PACKAGE_MOBILE_SDK_SAMPLE_APP = "com.linkedin.android.mobilesdksampleapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity thisActivity = this;
        setUpdateState();

        //Initialize session
        Button liLoginButton = (Button) findViewById(R.id.login_li_button);
        liLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        setUpdateState();
                        Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAuthError(LIAuthError error) {
                        setUpdateState();
                        ((TextView) findViewById(R.id.at)).setText(error.toString());
                        Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }, true);
            }
        });

        //Clear session
        Button liForgetButton = (Button) findViewById(R.id.logout_li_button);
        liForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISessionManager.getInstance(getApplicationContext()).clearSession();
                setUpdateState();
            }
        });

        //Go to API calls page
        Button liApiCallButton = (Button) findViewById(R.id.apiCall);
        liApiCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ApiActivity.class);
                startActivity(intent);
            }
        });

        //Go to DeepLink page
        Button liDeepLinkButton = (Button) findViewById(R.id.deeplink);
        liDeepLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeepLinkActivity.class);
                startActivity(intent);
            }
        });


        //Compute application package and hash
        Button liShowPckHashButton = (Button) findViewById(R.id.showPckHash);
        liShowPckHashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(
                            PACKAGE_MOBILE_SDK_SAMPLE_APP,
                            PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());

                        ((TextView) findViewById(R.id.pckText)).setText(info.packageName);
                        ((TextView) findViewById(R.id.pckHashText)).setText(Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.d(TAG, e.getMessage(), e);
                } catch (NoSuchAlgorithmException e) {
                    Log.d(TAG, e.getMessage(), e);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void setUpdateState() {
        LISessionManager sessionManager = LISessionManager.getInstance(getApplicationContext());
        LISession session = sessionManager.getSession();
        boolean accessTokenValid = session.isValid();

        ((TextView) findViewById(R.id.at)).setText(
                accessTokenValid ? session.getAccessToken().toString() : "Sync with LinkedIn to enable these buttons");
        ((Button) findViewById(R.id.apiCall)).setEnabled(accessTokenValid);
        ((Button) findViewById(R.id.deeplink)).setEnabled(accessTokenValid);
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

}
