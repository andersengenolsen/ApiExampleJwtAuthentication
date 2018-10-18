// API controller
define(function() {

    const url = "http://localhost:5000/api/auth/";
    const signUp = "signup";
    const signIn = "signin";

    const contentType = "Content-Type";
    const contentVal = "application/json";
    const charSet = "charset=UTF-8";

    // Registering new user
    var register = function(json, callback) {
        try {
            var req = new XMLHttpRequest();
            
            // Sending user data
            req.open("post", url + signUp, true);
            req.setRequestHeader(contentType, contentVal, charSet);
            req.send(json);
            
            // Returning the response to the caller
            req.onloadend = function() {
                let response = JSON.parse(req.responseText);
                callback(response, true);
            }
        } catch(error) {
            callback(undefined, false);
        }
    };

    // Public methods
    return {
        /*
        Logging user into the API.
        */
        login: function(json, callback) {
            if(callback === undefined)
                console.log("No callback method provided!");
        },

        /*
        Registering a new user.
        */
        register: function(json, callback) {
            if(callback===undefined)
                console.log("No callback method provided!");

            register(json,callback);
        }
    };
});