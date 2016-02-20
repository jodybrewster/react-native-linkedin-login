iOS Guide
---------

Includes Linkedin iOS SDK v1.0.4

### Linkedin Getting Started Guide

-	[https://developer.linkedin.com/docs/ios-sdk](https://developer.linkedin.com/docs/ios-sdk)

### XCode configuration

-	Add `RNLinkedinLogin` folder to your XCode project (click on 'Options' button and make sure 'copy items if needed' is ticked and 'create groups' is selected)

[![xcode dialog](https://github.com/jodybrewster/react-native-linkedin-login/raw/master/md/assets/save.png)\]


### Vector Icons

The button uses a vector icon to render the linkedin logo. To install the icon font
please install the FontAwesome font using the instructions on the vector-icon github page.

[https://github.com/oblador/react-native-vector-icons](https://github.com/oblador/react-native-vector-icons)

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
