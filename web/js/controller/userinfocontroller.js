define(["controller/apicontroller"], function(ApiController) {

    const DOMString = {
        usernamewelcome: "#username-welcome",
        username: "#username",
        firstname: "#firstname",
        lastname: "#lastname",
        email: "#email"
    };

    /*
     * Populating list with user info, callback function
     * which is passed to the ApiController.
     *
    */
    var loadUserCallback = function(data) {

        document.querySelector(DOMString.username).innerHTML = data["username"];
        document.querySelector(DOMString.firstname).innerHTML = data["firstName"];
        document.querySelector(DOMString.lastname).innerHTML = data["lastName"];
        document.querySelector(DOMString.email).innerHTML = data["email"];
        document.querySelector(DOMString.usernamewelcome).innerHTML = data["username"];
        
    }

    return {
        init: function() {
            ApiController.currentUser(loadUserCallback);
        }
    };
});