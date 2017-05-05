package com.linkedin.android.mobilesdksampleapp;
/*
// comment out.

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.widget.TextView;

import com.linkedin.android.mobilesdksampleapp.MainActivity;
import com.linkedin.android.mobilesdksampleapp.R;
import com.linkedin.android.mobilesdksampleapp.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml",sdk = 19,constants = BuildConfig.class)
public class MainActivityTest {


    public static final String TEST_PACKAGE = "com.linkedin.android.mobilesdksampleapp";

    private void setupPackageManager() {
        RobolectricPackageManager rpm = (RobolectricPackageManager)RuntimeEnvironment.application.getPackageManager();
        PackageInfo pi = new PackageInfo();
        pi.packageName = TEST_PACKAGE;
        pi.signatures = new Signature[1];
        pi.signatures[0] = new Signature("23423523");
        rpm.addPackage(pi);
    }

    @Before
    public void setup() {
        setupPackageManager();
    }

    @Test
    public void clickingOnShowPkgHash_shouldDisplayPackageName() throws Exception {

        // test the package name is shown when the show pkg/hash button is clicked
        Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();

        // do click
        activity.findViewById(R.id.showPckHash).performClick();

        // check result
        TextView packageTextView = (TextView)activity.findViewById(R.id.pckText);
        assertEquals("package text is incorrect",TEST_PACKAGE,packageTextView.getText());
    }
}
*/
