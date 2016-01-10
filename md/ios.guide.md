iOS Guide
---------

Includes Linkedin iOS SDK v1.0.4

### Linkedin Getting Started Guide

-	[https://developer.linkedin.com/docs/ios-sdk](https://developer.linkedin.com/docs/ios-sdk)

### XCode configuration

-	Add `RNLinkedinLogin` folder to your XCode project (click on 'Options' button and make sure 'copy items if needed' is ticked and 'create groups' is selected)

[![xcode dialog](https://github.com/jodybrewster/react-native-linkedin-login/raw/master/md/assets/save.png)\]


### Usage

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
