'use strict';


import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Text,
  Image,
  TouchableOpacity
} from 'react-native';



import LinkedinLogin from 'react-native-linkedin-login';
import Icon from 'react-native-vector-icons/FontAwesome';
import Store from 'react-native-simple-store';


class example extends Component {


  constructor(props) {
    super(props);

    this._login = this._login.bind(this);
    this._logout = this._logout.bind(this);


    this.state = {
      user: null
    };
  }
  componentWillMount() {
    // initialize LinkedinApi
    console.log('init');
    LinkedinLogin.init(
      'https://www.linkedintest.org',
      '782p0ki4p07yct',
      'DELETEME',
      'DCEEFWF45453sdffef424',
      [
        'r_emailaddress',
        'r_basicprofile'
      ]
    );

    // get the user session from the store
    Store.get('user').then((user) => {
      if (user) {
        // set the api session if found
        LinkedinLogin.setSession(user.accessToken, user.expiresOn);

        this.setState({
          user
        });

        console.log('user', user);
      }
    });
  }
  _login() {
    LinkedinLogin.login().then((user) => {
      console.log('User logged in: ', user);

      // recieved auth token
      this.setState({ user });
      Store.save('user', user).then(() => {
        // get the profile
        this._getUserProfile();
      });
    }).catch((e) => {
      var err = JSON.parse(e.description);
      alert("ERROR: " + err.errorMessage);
      console.log('Error', e);
    });

    return true;
  }
  _logout() {
    LinkedinLogin.logout();
    console.log('user logged out');
    Store.delete('user');
    this.setState({ user: null });
  }
  _getUserProfile() {
    LinkedinLogin.getProfile().then((data) => {
      console.log('received profile', data);
      const userdata = Object.assign({}, this.state.user, data);

      console.log('user: ', userdata);
      this.setState({ user: userdata });

      Store.save('user', userdata).then(() => {
        this._getUserProfileImage();
      });
    }).catch((e) => {
      console.log(e);
    });
  }
  _getUserProfileImage() {
    LinkedinLogin.getProfileImages().then((images) => {
      console.log('received profile image', images);

      const userdata = Object.assign({}, this.state.user, { images });

      Store.save('user', userdata).then(() => {
        console.log('user: ', userdata);
        this.setState({ user: userdata });
      });
    }).catch((e) => {
      console.log(e);
    });
  }
  render() {
    if (!this.state.user) {
      return (
        <View style={ styles.container }>
          <Icon.Button
            name="linkedin"
            backgroundColor="#0059b3"
            onPress={ this._login }>
          Sign in with Linkedin
          </Icon.Button>
        </View>
      );
    }

    if (this.state.user) {
      const lastNameComp = (this.state.user.lastName) ? (
          <Text style={ { fontSize: 18, fontWeight: 'bold', marginBottom: 20 } }>
          Welcome { `${this.state.user.firstName} ${this.state.user.lastName}` }
          </Text>
        ) : <View/>;
      const emailAddressComp = (this.state.user.emailAddress) ? (
        <Text>Your email is: { this.state.user.emailAddress }</Text>
      ) : <View/>;
      const imageComp = (this.state.user.images) ? (
        <Image
          source={ { uri: this.state.user.images[0].toString() } }
          style={ { width: 100, height: 100 } } />
      ) : <View/>;
      const expiresOnComp = (this.state.user.expiresOn) ? (
        <Text>Your token expires in: { this.state.user.expiresOn }</Text>
      ) : <View/>;

      return (
        <View style={ styles.container }>
          { lastNameComp }
          { emailAddressComp }
          { expiresOnComp }
          { imageComp }

          <TouchableOpacity onPress={ this._logout }>
            <View style={ { marginTop: 50 } }>
              <Text>Log out</Text>
            </View>
          </TouchableOpacity>


        </View>
      );
    }
  }
};
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF'
  }
});

module.exports = example;
