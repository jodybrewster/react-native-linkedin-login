import React from 'react';
import {
  Platform,
  DeviceEventEmitter,
  NativeModules,
} from 'react-native';
const RNLinkedinLogin = NativeModules.LinkedinLogin;


export default class LinkedinLogin {

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
   * Initializes the Linkedin Login module
   *
   * @param {any} scopes
   * @returns {LinkedinLogin}
   */
  init(clientId, clientSecret, scopes) {
    this._clientId = clientId;
    this._clientSecret = clientSecret;
    this._scopes = scopes;
    return this;
  }

  /**
   * Gets the Profile image
   * @return {object} Returns the promise with the image
   */
  getProfileImages = () => {
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

      RNLinkedinLogin.getRequest(picstr);
    });
  }

  /**
   * Gets the user profile
   * @return {object} Returns a promise with the user object or error
   */
  getProfile = () => {
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

      RNLinkedinLogin.getRequest(profilestr);
    });
  }

  /**
  * Sets the Linkedin session
  * @param  {string} accessToken Linkedin access token
  * @param  {Number} expiresOn   The access token's expiration number
  * @return {object} promise     Returns if access token is valid or not
  */
  setSession = (accessToken, expiresOn) => {
    this._accessToken = accessToken;
    this._expiresOn = expiresOn;
  }

  /**
   * Logs the user in
   * @return {promise} returns whether or not the user logged in successfully
   */
  login = () => new Promise((resolve, reject) => {
    DeviceEventEmitter.addListener('linkedinLoginError', (error) => {
      reject(error);
    });

    DeviceEventEmitter.addListener('linkedinLogin', (data) => {
      this._accessToken = data.accessToken;
      this._expiresOn = data.expiresOn;

      resolve(data);
    });

    RNLinkedinLogin.login(
        this._scopes,
      );
  })

  logout = () => {
    this._accessToken = null;
  }
}
