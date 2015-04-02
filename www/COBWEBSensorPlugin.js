var exec = require('cordova/exec');

exports.lineOfSight = function("", success, error) {
    exec(success, error, "COBWEBSensorPlugin", "lineOfSight", [""]);
};

exports.deviceInfo = function("", success, error) {
    exec(success, error, "COBWEBSensorPlugin", "deviceInfo", [""]);
};