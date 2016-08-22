# Android Guide
Includes Linkedin Android SDK v1.1.4

## Linkedin Getting Started Guide
- [https://developer.linkedin.com/docs/android-sdk](https://developer.linkedin.com/docs/android-sdk)


## Setup
- In `android/setting.gradle`

```gradle
...
include ':react-native-linkedin-login', ':app'
project(':react-native-linkedin-login').projectDir = new File(rootProject.projectDir, '../../android')

include ':react-native-vector-icons'
project(':react-native-vector-icons').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-vector-icons/android')

```

- In `android/app/build.gradle`

```gradle
...
dependencies {
    compile fileTree(dir: "libs", include: ["*.jar"])
    compile "com.android.support:appcompat-v7:23.0.1"
    compile "com.facebook.react:react-native:0.20.+"
    compile project(":react-native-linkedin-login") // <--- add this
}
```

- Register Module (in MainActivity.java)

```java
import net.jodybrewster.linkedinlogin.RNLinkedinLoginModule;        // <------ add here
import net.jodybrewster.linkedinlogin.RNLinkedinLoginPackage;       // <------ add here

import com.oblador.vectoricons.VectorIconsPackage;  // <------ add here
import com.linkedin.platform.LISessionManager;  // <------ add here

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
        new VectorIconsPackage(), // <------ add this line to yout MainActivity class
        new RNLinkedinLoginPackage(this)); // <------ add this line to yout MainActivity class
    }

    // add this method inside your activity class
    @Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
      LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data); // <------ add here

      super.onActivityResult(requestCode, resultCode, data);
    }
```

### Vector Icons

The button uses a vector icon to render the linkedin logo. To install the icon font
please install the FontAwesome font using the instructions on the vector-icon github page.

[https://github.com/oblador/react-native-vector-icons](https://github.com/oblador/react-native-vector-icons)


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
