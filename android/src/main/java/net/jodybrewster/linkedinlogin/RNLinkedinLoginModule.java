package net.jodybrewster.linkedinlogin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.AccessToken;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 *
 * RNLinkedinLoginModule Class provides core functionality for the linkedin api
 *
 */
public class RNLinkedinLoginModule extends ReactContextBaseJavaModule {

    // TAG!
    public static final String TAG = RNLinkedinLoginModule.class.getCanonicalName();

    // Activity
    private Activity _activity;

    // Context
    private static ReactApplicationContext _context;

    private ArrayList<Scope.LIPermission> _availablePermissions;


    /**
     *
     * Module constructor
     *
     * @param reactContext ReactContext used to access react methods
     * @param activity Originating Activity
     */
    public RNLinkedinLoginModule(final ReactApplicationContext reactContext, Activity activity) {

        super(reactContext);
        _activity = activity;
        _context = reactContext;

        _availablePermissions = new ArrayList<Scope.LIPermission>();
        _availablePermissions.add(Scope.R_BASICPROFILE);
        _availablePermissions.add(Scope.R_CONTACTINFO);
        _availablePermissions.add(Scope.R_EMAILADDRESS);
        _availablePermissions.add(Scope.R_FULLPROFILE);
        _availablePermissions.add(Scope.RW_COMPANY_ADMIN);
        _availablePermissions.add(Scope.W_SHARE);
    }

    /**
     *
     * @return returns the name of the module
     */
    @Override
    public String getName() {
        return "LinkedinLogin";
    }

    /**
     * Initializes the Linkedin Api
     * @param accessToken Access Token for existing sessions
     * @param expiresOn Expires on long value
     */
    @ReactMethod
    public void init(final String accessToken, final String expiresOn)
    {

        Log.d(TAG, "init");
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    final JSONObject obj = new JSONObject();
                    obj.put(AccessToken.ACCESS_TOKEN_VALUE, accessToken);
                    obj.put(AccessToken.EXPIRES_ON, expiresOn);

                    AccessToken accessToken = new AccessToken(obj);
                    LISessionManager.getInstance(_context).init(accessToken);

                    WritableMap params = Arguments.createMap();
                    params.putBoolean("success",  true);

                    _context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("linkedinInit", params);

                } catch (JSONException e) {
                    e.printStackTrace();

                    WritableMap params = Arguments.createMap();
                    params.putString("error",  e.getMessage());

                    _context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("linkedinInitError", params);
                }

            }
        });
    }

    /**
     * Makes an API request
     * @param url complete url for the api request without session information,
     *            ex: https://api.linkedin.com/v1/people/~:(id,first-name,last-name,industry,email-address)
     */
    @ReactMethod
    public void getRequest(final String url) {

        Log.d(TAG, "getRequest");
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                APIHelper.getInstance(_context).getRequest(_context, url, new ApiListener() {

                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {

                        Log.d(TAG, "getRequest onApiSuccess");

                        JSONObject dataAsJson = apiResponse.getResponseDataAsJson();
                        try {
                            Log.i(TAG, dataAsJson.toString(4));


                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }

                        WritableMap params = Arguments.createMap();
                        params.putString("data",  dataAsJson.toString());

                        _context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("linkedinGetRequest", params);

                    }

                    @Override
                    public void onApiError(LIApiError apiError) {
                        Log.e(TAG, apiError.toString());
                        WritableMap params = Arguments.createMap();
                        params.putString("error",  apiError.toString());

                        _context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("linkedinGetRequestError", params);
                    }

                });
            }

        });
    }

    /**
     *
     * logs in the user
     *
     * @param clientId clientId from linkedin developer portal
     * @param redirectUrl redirectUrl can be anything I guess? lol
     * @param clientSecret clientSecret from linkedin developer portal
     * @param state any random string no one else has
     * @param scopes array of available permissions
     */
    @ReactMethod
    public void login(final String clientId, final String redirectUrl, final String clientSecret, final String state, final ReadableArray scopes) {

        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Log.d(TAG, "signIn");
                LISessionManager.getInstance(_context).init(_activity, Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        // Authentication was successful.  You can now do
                        // other calls with the SDK.


                        String accessToken = LISessionManager.getInstance(_context).getSession().getAccessToken().getValue();
                        Log.d(TAG, accessToken);

                        WritableMap params = Arguments.createMap();
                        params.putString("accessToken",  LISessionManager.getInstance(_context).getSession().getAccessToken().getValue());
                        params.putDouble("expiresOn",  LISessionManager.getInstance(_context).getSession().getAccessToken().getExpiresOn());

                        _context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("linkedinLogin", params);

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        // Handle authentication errors

                        Log.e(TAG, error.toString());

                        WritableMap params = Arguments.createMap();
                        params.putString("description", error.toString());

                        _context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("linkedinLoginnError", params);
                    }
                }, true);
            }

            // TODO: this is broke, need more time to look at this, replace with Scope.build above
            private Scope scopeFromArray(final ReadableArray array) {

                Log.d(TAG, array.toString());
                Scope.LIPermission[] permissions = new Scope.LIPermission[array.size()];
                for(int i =0; i < array.size(); i++) {
                    for(Scope.LIPermission value : _availablePermissions) {
                        if (value.getName() == array.getString(i))
                        {
                            permissions[i] = value;
                        }
                    }
                }

                Scope scope = new Scope(permissions);
                return scope;
            }
        });
    }

    /**
     * Logs user out!
     */
    @ReactMethod
    public void logout() {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LISessionManager.getInstance(_context).clearSession();

                _context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("linkedinLogout", null);
            }
        });

    }

    public static void onActivityResult(Intent data) {


        Log.e(TAG, "onActivityResult");

    }


}
