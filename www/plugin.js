/* eslint-disable @typescript-eslint/no-var-requires */
var cordova = require('cordova');

var PLUGIN_NAME = 'PaciolanSDK';

function PaciolanSDK() {
  this.show = function (initializationParams, success, error) {
    return new Promise(function (resolve, reject) {
      var params = [initializationParams];
      cordova.exec(
        success || resolve,
        error || reject,
        PLUGIN_NAME,
        'show',
        params,
      );
    });
  };
  this.getToken = function (success, error) {
    return new Promise(function (resolve, reject) {
      cordova.exec(
        success || resolve,
        error || reject,
        PLUGIN_NAME,
        'getPaciolanJWT',
        null,
      );
    });
  };
}

module.exports = new PaciolanSDK();
