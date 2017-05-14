# Android Guide

## Installation

```
npm install react-native-linkedin-login --save
```


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
    compile "com.facebook.react:react-native:+"  // From node_modules
    compile project(":react-native-linkedin-login") // <-- add here
    compile project(':react-native-vector-icons')
}
```

- Register Module (in MainApplication.java)

```java

package com.rnlinkinloginexample;

import android.app.Application;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;

import net.jodybrewster.linkedinlogin.RNLinkedinLoginPackage;  // <------ add here
//import com.oblador.vectoricons.VectorIconsPackage;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    protected boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new RNLinkedinLoginPackage() // <------ add this line to yout MainActivity class
          //new VectorIconsPackage()
      );
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
  }
}


```


### Linkedin Getting Started Guide

Check out the following Android guide for reference

-	[https://developer.linkedin.com/docs/android-sdk](https://developer.linkedin.com/docs/android-sdk)

### Vector Icons

The button uses a vector icon to render the linkedin logo. To install the icon font
please install the FontAwesome font using the instructions on the vector-icon github page.

[https://github.com/oblador/react-native-vector-icons](https://github.com/oblador/react-native-vector-icons)


## Usage
Please change the init with your parameters

```js
...


this.init(
    [
        'r_emailaddress',
        'r_basicprofile'
    ]
).then((e) => {
    console.log('Linkedin initialized');
});
```
