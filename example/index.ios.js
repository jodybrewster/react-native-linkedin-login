'use strict';

var React = require('react-native');
var { NativeAppEventEmitter, NativeModules } = require('react-native');
 var LinkedinLogin = NativeModules.RNLinkedinLogin;

var { Icon } = require('react-native-icons');

var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  TouchableHighlight
} = React;

class RNLinkedinLoginExample extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      user: null
    };
  }

  componentDidMount() {
    this._configureOauth(
      'redirectUrl',
      'clientId',
      'clientSecret',
      'state',
      [
        "r_emailaddress",
        "r_basicprofile"
      ]
    );
  }

  render() {

    if (!this.state.user) {
      return (
        <View style={styles.container}>
          <TouchableHighlight onPress={() => {this._signIn(); }}>
            <View style={{backgroundColor: '#0059b3', flexDirection: 'row'}}>
              <View style={{padding: 12, borderWidth: 1/2, borderColor: 'transparent', borderRightColor: 'white'}}>
                <Icon
                  name='ion|social-linkedin'
                  size={24}
                  color='white'
                  style={{width: 24, height: 24}}
                />
              </View>
              <Text style={{color: 'white', padding: 12, marginTop: 2, fontWeight: 'bold'}}>Sign in with Linkedin</Text>
            </View>
          </TouchableHighlight>
        </View>
      );
    }

    if (this.state.user) {
      return (
        <View style={styles.container}>
          <Text style={{fontSize: 18, fontWeight: 'bold', marginBottom: 20}}>Welcome {this.state.user.name}</Text>
          <Text>Your email is: {this.state.user.email}</Text>
          <Text>Your token expires in: {this.state.user.accessTokenExpirationDate.toFixed()}s</Text>

          <TouchableOpacity onPress={() => {this._signOut(); }}>
            <View style={{marginTop: 50}}>
              <Text>Log out</Text>
            </View>
          </TouchableOpacity>
        </View>
      );
    }
  }

  _configureOauth(redirectUrl, clientId, clientSecret, state, scopes=[]) {

    NativeAppEventEmitter.addListener('linkedinLoginError', (error) => {
      console.log('linkedinLoginError: ', error);
    });

    NativeAppEventEmitter.addListener('linkedinLogin', (data) => {

      console.log("linkedinLogin: ", data);
      this.setState({user: data});
    });

    LinkedinLogin.loginWithClientId(clientId, redirectUrl, clientSecret, state, scopes);



    return true;
  }

  _signIn() {
    LinkedinLogin.signIn();
  }

  _signOut() {
    LinkedinLogin.signOut();
    this.setState({user: null});
  }
};

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
});

AppRegistry.registerComponent('RNLinkedinLoginExample', () => RNLinkedinLoginExample);
