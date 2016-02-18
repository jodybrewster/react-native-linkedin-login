package com.rnlinkedinloginexample;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

import net.jodybrewster.linkedinlogin.RNLinkedinLoginModule;        // <------ add here
import net.jodybrewster.linkedinlogin.RNLinkedinLoginPackage;       // <------ add here

import com.oblador.vectoricons.VectorIconsPackage;
import com.linkedin.platform.LISessionManager;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "RNLinkedinLoginExample";
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

   /**
   * A list of packages used by the app. If the app uses additional views
   * or modules besides the default ones, add more packages here.
   */
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new VectorIconsPackage(),
        new RNLinkedinLoginPackage(this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
      LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data); // <------ add here

      super.onActivityResult(requestCode, resultCode, data);
    }

  
}
