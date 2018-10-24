/*
 * API Controller, handling calls against the API.
 * Every module who use this module, must provide a callback method
 * which will fire when an API call is finished.
 */
define(function() {

    /* -- URLS -- */
    const url = "http://localhost:5000/api/";
    const urlUser = "http://localhost:5000/api/user/me"
    const signUp = "auth/signup";
    const signin = "auth/signin";
    const reset = "reset";
    
    /* -- JSON -- */
    const contentType = "Content-Type";
    const contentVal = "application/json";
    const charSet = "charset=UTF-8";

    /*
     * Setting up a XmlHTTPRequest with token
     *
     */
    var apiRequestWithToken = function(method, url) {
        let req = new XMLHttpRequest();
        req.open(method, url, true);
        req.setRequestHeader("Authorization",
                             "Bearer " + localStorage.getItem("token"));

        return req;
    };

    /*
     * Returning information about currently logged in user.
     *
     */
    var currentUser = function(callback) {
        let req = apiRequestWithToken("get", urlUser);

        req.send(null);

        req.onloadend = function() {
            let response = JSON.parse(req.responseText);
            callback(response);
        }
    };

    /*
     * Sending request for password reset.
     *
     */
    var requestReset = function(json, callback) {
        try {
            var req = new XMLHttpRequest();

            req.open("post", url + reset, true);
            req.setRequestHeader(contentType, contentVal, charSet);
            console.log(json);
            req.send(json);

            req.onloadend = function() {
                let response = JSON.parse(req.responseText);
                callback(response, true);
            };
            
        } catch(error) {
            callback(undefined, false);
        }
    };

    /*
     * Registering new user with a XMLHttpRequest, POST.
     * When response has been received, the passed in callback-function
     * will be called.
     * If error: Passing undefined and false to callback function.
     */
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

    /* 
     * Logging user in with a XMLHttpRequest, POST.
     * The login will return a JWT bearer token if successful.
     * If error: passing undefined and false to callback function
     */
    var login = function(json, callback) {
        try {
            var req = new XMLHttpRequest();

            // Posting login
            req.open("post", url + signin, true);
            req.setRequestHeader(contentType, contentVal, charSet);
            req.send(json);

            // Returning response
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

            login(json, callback);
        },

        /*
        Registering a new user.
        */
        register: function(json, callback) {
            if(callback===undefined)
                console.log("No callback method provided!");

            register(json,callback);
        },

        /*
         * Returning information about currently logged in user.
         */
        currentUser: function(callback) {
            if(callback === undefined)
                console.log("No callback method provided!");

            currentUser(callback);
        }, 

        /**
         *
         * Requesting password reset.
         */
        requestReset: function(json, callback) {
            if(callback===undefined)
                console.log("No callback method provided");

            requestReset(json, callback);
        }
    };
});