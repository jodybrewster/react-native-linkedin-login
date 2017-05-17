# iOS Guide


## Installation

```
npm install react-native-linkedin-login --save
```

Drag and drop the following into the xcode project...

node_modules/react-native-linkedin-login/linkedin-sdk.framework

and the entire folder...

node_modules/react-native-linkedin-login/RNLinkedinLogin/

Add these lines to your AppDelegate.m

```
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
  if ([LISDKCallbackHandler shouldHandleUrl:url])
  {
    return [LISDKCallbackHandler application:application openURL:url sourceApplication:sourceApplication annotation:annotation];
  }
  return YES;
}
```

Add the following to your Info.plist, please refer to the Linkedin docs below...

```xml

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


### Linkedin Getting Started Guide

Check out the following iOS guide for reference

-	[https://developer.linkedin.com/docs/ios-sdk](https://developer.linkedin.com/docs/ios-sdk)



## Usage

Please change the init with your parameters. To find out more about these parameters, see the [documentation for Oauth2 by LinkedIn](https://developer.linkedin.com/docs/oauth2).

```js

this.init(
	[
		'r_emailaddress',
		'r_basicprofile'
	]
).then((e) => {
	console.log('Linkedin initialized');
});
```
