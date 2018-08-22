# react-native-linkedin-login


[![npm version](https://img.shields.io/npm/v/react-native-linkedin-login.svg?style=flat-square)](https://www.npmjs.com/package/react-native-linkedin-login)
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)
[![Dependency Status](https://david-dm.org/jodybrewster/react-native-linkedin-login.svg)](https://david-dm.org/jodybrewster/react-native-linkedin-login)
[![Coverage Status](https://coveralls.io/repos/github/jodybrewster/react-native-linkedin-login/badge.svg?branch=master)](https://coveralls.io/github/jodybrewster/react-native-linkedin-login?branch=master)
[![Gitter](https://badges.gitter.im/react-native-linkedin-login/Lobby.svg)](https://gitter.im/react-native-linkedin-login/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Let your users sign in with their Linkedin account.

## Requirements

* Node v4+.
* React Native 0.30+

## Installation

### iOS Guide


#### Automatic

First install and save the library

```bash
npm install react-native-linkedin-login --save;
```

Then link the library to your project

```bash
react-native link react-native-linkedin-login;
```

In the manual section below you will find updates you need to make to your Info.plist file.  Please also refer to the linkedin documentation at the bottom of this readme for more info.


#### Manual

First install and save the library

```
npm install react-native-linkedin-login --save
```

Drag and drop the following xcode project file into the xcode project...

node_modules/react-native-linkedin-login/ios/RCTLinkedinLogin.xcodeproj


Add these lines to your AppDelegate.m

```objc
#import <RCTLinkedinLogin/RCTLinkedinLogin.h>
```

```objc

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
  if ([RCTLinkedinLogin shouldHandleUrl:url])
  {
    return [RCTLinkedinLogin application:application openURL:url sourceApplication:sourceApplication annotation:annotation];
  }
  return YES;
}
```

Add the following to your Info.plist, please refer to the Linkedin docs below...

```plist

<key>NSAppTransportSecurity</key>
<dict>
	<key>NSExceptionDomains</key>
	<dict>
		<key>linkedin.com</key>  
		<dict>
			<key>NSExceptionAllowsInsecureHTTPLoads</key>
			<true/>
			<key>NSExceptionRequiresForwardSecrecy</key>
			<false/>
			<key>NSIncludesSubdomains</key>
			<true/>
		</dict>
		<key>localhost</key>
		<dict>
			<key>NSExceptionAllowsInsecureHTTPLoads</key>
			<true/>
		</dict>
	</dict>
</dict>
<key>CFBundleURLTypes</key>
<array>
	<dict>
		<key>CFBundleTypeRole</key>
		<string>Editor</string>
		<key>CFBundleURLSchemes</key>
		<array>
			<string>li{YOUR_APP_ID_GOES_HERE}</string>
		</array>
	</dict>
</array>
<key>LIAppId</key>
<string>{YOUR_APP_ID_GOES_HERE}</string>
<key>LSApplicationQueriesSchemes</key>
<array>
	<string>linkedin</string>
	<string>linkedin-sdk2</string>
	<string>linkedin-sdk</string>
</array>
```

### Android Guide


#### Automatic

First install and save the library

```bash
npm install react-native-linkedin-login --save;
```

Then link the library to your project

```bash
react-native link;
```

#### Manual

First install and save the library

```bash
npm install react-native-linkedin-login --save
```

Then modify the following fules

- In `android/setting.gradle`

```gradle
...
include ':react-native-linkedin-login', ':app'
project(':react-native-linkedin-login').projectDir = new File(rootProject.projectDir, '../../android')


```

- In `android/app/build.gradle`

```gradle
...
dependencies {
    compile fileTree(dir: "libs", include: ["*.jar"])
    compile "com.android.support:appcompat-v7:23.0.1"
    compile "com.facebook.react:react-native:+"  // From node_modules
    compile project(":react-native-linkedin-login") // <-- add here
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
## Options For Linkedin Profile Fields

Check out following Linkedin developer guied for profile fields
-	[https://developer.linkedin.com/docs/fields/basic-profile](https://developer.linkedin.com/docs/fields/basic-profile)


```js
...

//pass options as string parameters in getProfile()
var options = 'id,first-name,last-name,industry,email-address,industry,location'

LinkedinLogin.getProfile(options)
```

## Additional scopes

Please note that basic and email permissions are hardcoded. Pull requests are welcome! ლ(́◉◞౪◟◉‵ლ)


## Alternatives
* [react-native-linkedin](https://github.com/xcarpentier/react-native-linkedin)
* [react-native-linkedin-sdk](https://www.npmjs.com/package/react-native-linkedin-sdk)
* [react-native-linkedin-oauth](https://www.npmjs.com/package/react-native-linkedin-oauth)



## License

The MIT License (MIT)

Copyright (c) 2015 Jody Brewster

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
