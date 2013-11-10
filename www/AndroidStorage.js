var AndroidStorage =  {
    createEvent: function(action, rawArg, successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'AndroidStorage', // mapped to our native Java class called "AndroidStorage"
            action, // with this action name
            rawArg   // and this array of custom arguments to create our entry               
        );
    }
}
module.exports = AndroidStorage;