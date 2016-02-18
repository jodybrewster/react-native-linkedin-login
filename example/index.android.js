'use strict';

var React = require('react-native');
var Store = require('react-native-simple-store');
var LinkedinApi = require('./LinkedinMixin');
var Icon = require('react-native-vector-icons/FontAwesome');

var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  TouchableHighlight,
  Image,
} = React;

var RNLinkedinLoginExample = React.createClass({

  mixins: [LinkedinApi],

  getInitialState: function() {
    return ({
      user: null
    });
  },

  componentDidMount: function() {

    // initialize LinkedinApi

    this.init(
      'https://www.linkedintest.org',
      '2krikav6u8j6',
      'SR1UCLdjBOj1udlB',
      'DCEEFWF45453sdffef424',
      [
        'r_emailaddress',
        'r_basicprofile'
      ]
    ).then((e) => {
      console.log('Linkedin initialized');
    });



    // get the user session from the store
    Store.get('user').then((user) => {

      if (user) {

        // set the api session if found
        this.setSession(user.accessToken, user.expiresOn).then((e) => {
          console.log('LinkedinLogin session set');
        }).error((e) => {
          console.log('Error: ', e);

          // clear the user when the session isn't found
          Store.delete('user');
          this.setState({ user: null });
        });

        this.setState({
          user: user
        });

        console.log('user', user);
      }

    });

  },


  render: function() {

    if (!this.state.user) {
      return (
        <View style={styles.container}>

          <Icon.Button name="linkedin" backgroundColor="#0059b3" onPress={() => {this._login(); }}>
            Sign in with Linkedin
          </Icon.Button>
        </View>
      );
    }


    if (this.state.user) {
      var lastNameComp = (this.state.user.lastName) ? <Text style={{fontSize: 18, fontWeight: 'bold', marginBottom: 20}}>Welcome {this.state.user.firstName + " " + this.state.user.lastName}</Text> : <View/>;
      var emailAddressComp = (this.state.user.emailAddress) ? <Text>Your email is: {this.state.user.emailAddress}</Text> : <View/>;
      var imageComp = (this.state.user.images) ? <Image source={{ uri: this.state.user.images[0].toString() }} style={{ width: 100, height: 100 }} /> : <View/>;
      var expiresOnComp = (this.state.user.expiresOn) ? <Text>Your token expires in: {this.state.user.expiresOn.toFixed()}</Text> : <View/>;

      return (
        <View style={styles.container}>
          { lastNameComp }
          { emailAddressComp }
          { expiresOnComp }
          { imageComp }

          <TouchableOpacity onPress={() => {this._logout(); }}>
            <View style={{marginTop: 50}}>
              <Text>Log out</Text>
            </View>
          </TouchableOpacity>


        </View>
      );
    }

  },



  _login: function() {

    this.login().then((user) => {

      console.log('User logged in: ', user);

      // recieved auth token
      this.setState({ user: user });
      Store.save('user', user).then(() => {

        // get the profile
        this.getProfile().then((data) => {
          console.log('received profile', data);
          var user = Object.assign({}, this.state.user, data);
          console.log('user: ', user);
          this.setState({user: user});

          Store.save('user', user).then(() => {
            // get the profile image
            this.getProfileImages().then((images) => {

              console.log('received profile image', images);

              var user = Object.assign({}, this.state.user, { images: images });
              Store.save('user', user);
              console.log('user: ', user);
              this.setState({user: user});


            }).error((e) => {

              console.log('error getting profile image: ', e);

            });
          });


        }).error((e) => {

          console.log('error getting profile: ', e);

        });
      });

    }).error((e) => {
      console.log('Error: ', e.message);
      this.setState({user: null});
    });


    return true;
  },

  _logout: function() {
    this.logout().then((e) => {
      console.log('user logged out');
      Store.delete('user');
      this.setState({user: null});
    }).error((e) => {
      console.log('Error: ', e.message);
    })


  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
});

AppRegistry.registerComponent('RNLinkedinLoginExample', () => RNLinkedinLoginExample);
