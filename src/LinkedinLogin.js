'use strict';

const React = require('react-native');
const {
  Platform,
  DeviceEventEmitter,
  NativeModules
} = React;
const RNLinkedinLogin = NativeModules.LinkedinLogin;


class LinkedinLogin {

  _accessToken = null;
  _expiresOn = null;

  _redirectUrl = null;
  _clientId = null;
  _clientSecret = null;
  _state = null;
  _scopes = null;

  constructor() {
  }

  /**
   * Initializes the LinkedinLogin API
   * @param  {string} redirectUrl     [description]
   * @param  {string} clientId        [description]
   * @param  {string} clientSecret    [description]
   * @param  {string} state           [description]
   * @param  {array} scopes           [description]
   * @return {object} promise         [description]
   */
  init(redirectUrl, clientId, clientSecret, state, scopes) {
    console.log('LinkedinLogin.init', redirectUrl, clientId, clientSecret, state, scopes)
    this._redirectUrl = redirectUrl;
    this._clientId = clientId;
    this._clientSecret = clientSecret;
    this._state = state;
    this._scopes = scopes;
    return this;
  }

  /**
   * Gets the Profile image
   * @return {object} Returns the promise with the image
   */
  getProfileImages() {
    const atoken = this._accessToken;

    return new Promise((resolve, reject) => {
      DeviceEventEmitter.addListener('linkedinGetRequest', (d) => {
        const data = JSON.parse(d.data);

        if (data.values) {
          resolve(data.values);
        } else {
          reject('No profile image found');
        }
      });

      DeviceEventEmitter.addListener('linkedinGetRequestError', (error) => {
        reject(error);
      });

      const picstr = 'https://api.linkedin.com/v1/people/~/picture-urls::(original)';
      const picstrWithAuth = `${picstr}?oauth2_access_token=${atoken}&format=json`;

      if (Platform.OS === 'android') {
        RNLinkedinLogin.getRequest(picstr);
      } else {
        // if ios
        console.log('picstrWithAuth', picstrWithAuth);

        fetch(picstrWithAuth).then(function(response) {
        	return response.json();
        }).then((data) => {

          if (data.values && data.values.length > 0) {
            resolve(data.values);
          } else {
            reject('Profile has no images');
          }
        });
      }
    });
  }

  /**
   * Gets the user profile
   * @return {object} Returns a promise with the user object or error
   */
  getProfile() {
    const atoken = this._accessToken;

    return new Promise((resolve, reject) => {
      DeviceEventEmitter.addListener('linkedinGetRequest', (d) => {
        const data = JSON.parse(d.data);

        if (data) {
          resolve(data);
        }
      });

      DeviceEventEmitter.addListener('linkedinGetRequestError', (error) => {
        reject(error);
      });

      const options = 'id,first-name,last-name,industry,email-address';
      const profilestr = `https://api.linkedin.com/v1/people/~:(${options})`;
      const profilestrWithAuth = `${profilestr}?oauth2_access_token=${atoken}&format=json`;

      console.log(profilestrWithAuth);
      if (Platform.OS === 'android') {
        RNLinkedinLogin.getRequest(profilestr);
      } else {
        fetch(profilestrWithAuth).then(function(response) {
        	return response.json();
        }).then((data) => {

          if (data) {
            resolve(data);
          } else {
            reject('No profile found');
          }
        });
      }
    });
  }

  /**
  * Sets the Linkedin session
  * @param  {string} accessToken Linkedin access token
  * @param  {Number} expiresOn   The access token's expiration number
  * @return {object} promise     Returns if access token is valid or not
  */
  setSession(accessToken, expiresOn) {
    this._accessToken = accessToken;
    this._expiresOn = expiresOn;
  }

  /**
   * Logs the user in
   * @return {promise} returns whether or not the user logged in successfully
   */
  login() {
    return new Promise((resolve, reject) => {
      DeviceEventEmitter.addListener('linkedinLoginError', (error) => {
        reject(error);
      });

      DeviceEventEmitter.addListener('linkedinLogin', (data) => {
        this._accessToken = data.accessToken;
        this._expiresOn = data.expiresOn;

        resolve(data);
      });

      RNLinkedinLogin.login(
        this._scopes
      );
    });
  }

  logout() {
    this._accessToken = null;
  }
}

const linkedinLogin = new LinkedinLogin();

module.exports = linkedinLogin;
