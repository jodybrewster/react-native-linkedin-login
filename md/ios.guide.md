iOS Guide
---------

Includes Linkedin iOS SDK v1.0.7

### Linkedin Getting Started Guide

-	[https://developer.linkedin.com/docs/ios-sdk](https://developer.linkedin.com/docs/ios-sdk)

### XCode configuration

- Install Cocoapods

```
sudo gem install cocoapods

# Move into project's folder
cd ios

# Create a Podfile
touch Podfile 
```

- Copy the contents of example/ios/Podfile from this repo into your newly created Podfile

- Install the Cocoapods

```
pod install
```


### Vector Icons

The button uses a vector icon to render the linkedin logo. To install the icon font
please install the FontAwesome font using the instructions on the vector-icon github page.

[https://github.com/oblador/react-native-vector-icons](https://github.com/oblador/react-native-vector-icons)

### Usage

Please change the init with your parameters. To find out more about these parameters, see the [documentation for Oauth2 by LinkedIn](https://developer.linkedin.com/docs/oauth2) has.

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
