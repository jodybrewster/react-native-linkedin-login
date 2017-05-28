'use strict';


import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Text,
  Image,
  Button,
  TouchableOpacity,
  AsyncStorage
} from 'react-native';


import LinkedinLogin from 'react-native-linkedin-login';


class example extends Component {


  constructor(props) {
    super(props);

    this.login = this.login.bind(this);
    this.logout = this.logout.bind(this);


    this.state = {
      user: null
    };
  }
  componentWillMount() {
    // initialize LinkedinApi
    console.log('init');
    LinkedinLogin.init(
      [
        'r_emailaddress',
        'r_basicprofile'
      ]
    );

    this.getUserSession();
    
  }

  getUserSession() {
    // get the user session from the store

    
    AsyncStorage.getItem('user',  (err, result)  => {
      if (result) {
        const user = JSON.parse(result);

        // set the api session if found
        LinkedinLogin.setSession(user.accessToken, user.expiresOn);

        this.setState({
          user
        });

        console.log('user', user);
      }
    });

  }

  login() {
    LinkedinLogin.login().then((user) => {
      console.log('User logged in: ', user);

      // recieved auth token
      this.setState({ user });
    
       AsyncStorage.setItem('user', JSON.stringify(user), () => {
            this.getUserProfile();
        });
        

    }).catch((e) => {
      var err = JSON.parse(e.description);
      alert("ERROR: " + err.errorMessage);
      console.log('Error', e);
    });

    return true;
  }

  logout() {
    LinkedinLogin.logout();
    console.log('user logged out');
    
    AsyncStorage.removeItem('user');
    this.setState({ user: null });
  }
 
  getUserProfile(user) {
    LinkedinLogin.getProfile().then((data) => {
      console.log('received profile', data);
      const userdata = Object.assign({}, this.state.user, data);

      console.log('user: ', userdata);
      this.setState({ user: userdata });

      AsyncStorage.setItem('user', JSON.stringify(userdata), () => {
          this.getUserProfileImage();
        });

    }).catch((e) => {
      console.log(e);
    });
  }

  getUserProfileImage() {
    LinkedinLogin.getProfileImages().then((images) => {
      console.log('received profile image', images);

      const userdata = Object.assign({}, this.state.user, { images });

      AsyncStorage.setItem('user', JSON.stringify(userdata), () => {
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
          <Button
            title="Sign in with Linkedin"
            color="#0059b3"
            onPress={ this.login } />
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

          <TouchableOpacity onPress={ this.logout }>
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
