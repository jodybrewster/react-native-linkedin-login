# Android Guide
Includes Linkedin Android SDK v1.1.4

## Linkedin Getting Started Guide
- [https://developer.linkedin.com/docs/ios-sdk](https://developer.linkedin.com/docs/ios-sdk)

## Setup
- In `android/setting.gradle`

```gradle
...
include ':react-native-linkedin-login', ':app'
project(':react-native-linkedin-login').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-linkedin-login/android')
```

- In `android/app/build.gradle`

```gradle
...
dependencies {
    compile fileTree(dir: "libs", include: ["*.jar"])
    compile "com.android.support:appcompat-v7:23.0.1"
    compile "com.facebook.react:react-native:0.14.+"
    compile project(":react-native-linkedin-login") // <--- add this
}
```

- Register Module (in MainActivity.java)

```java
import net.jodybrewster.linkedinlogin.RNLinkedinLoginModule; // <--- import
import net.jodybrewster.linkedinlogin.RNLinkedinLoginPackage;  // <--- import

import com.linkedin.platform.LISessionManager; // <--- import


public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
  ......

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new RNLinkedinLoginPackage(this)) // <------ add this line to yout MainActivity class
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "AndroidRNSample", null);

    setContentView(mReactRootView);
  }

  // add this method inside your activity class
  @Override
  protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
      LISessionManager.getInstance(getApplicationContext())       // <------ add here
          .onActivityResult(this, requestCode, resultCode, data); // <------ add here
  }

  ......

}
```

## Usage
Please change the init with your parameters

```js
...


this.init(
    'https://www.yourdomain.org',
    'your_client_id',
    'your_client_secret',
    'your_state',
    [
        'r_emailaddress',
        'r_basicprofile'
    ]
).then((e) => {
    console.log('Linkedin initialized');
});
```
