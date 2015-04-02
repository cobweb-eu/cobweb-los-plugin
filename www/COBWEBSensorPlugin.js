var exec = require('cordova/exec');

exports.lineOfSight = function(intentString, success, error) {
    exec(success, error, "COBWEBSensorPlugin", "lineOfSight", [intentString]);
};

exports.deviceInfo = function(intentString, success, error) {
    exec(success, error, "COBWEBSensorPlugin", "deviceInfo", [intentString]);
};