define(["controller/apicontroller", "util/helper"], function(ApiController, Helper) {

    const DOMString = {
        usernamewelcome: "#username-welcome",
        userinfo: ".user-info",
        editProfileForm: "#edit-profile-form",
        infoMsg: "#info-msg"
    };

    /*
     * Populating ul with user info, callback function
     * which is passed to the ApiController.
     *
    */
    var loadUserCallback = function(data) {
        // Welcome message
        document.querySelector(DOMString.usernamewelcome).innerHTML += data["username"];

        // User data in ul.
        let ul = document.querySelector(DOMString.userinfo);
        for(var key in data) {
            let li = document.createElement("li");
            li.innerHTML = capitalizeFirstLetter(key) + ": " + data[key];
            ul.appendChild(li);
        }
    };

    /*
     * Setting event listeners to objects in DOM
     *
     */
    var setListeners = function() {
        document.querySelector(DOMString.editProfileForm).addEventListener("submit", function(e) {
            e.preventDefault();
            updateUserData(this);
        });
    };
    
    /*
     * Reading input data from form, passing to the ApiController.
     *
     */
    var updateUserData = function(form) {
        const formData = new FormData(form);
        let json = Helper.formDataToJson(formData);
        
        ApiController.updateUser(json, updateUserCallback);
    };
    
    /*
     * Callback method for update user action.
     *
     */
    var updateUserCallback = function(data) {
        let msg = document.querySelector(DOMString.infoMsg);
        console.log(data);
        if(data === undefined) { 
            msg.innerHTML = "Network error occurred";
            Helper.showElement(msg);
        }
        else {
            msg.innerHTML = data["message"];
            Helper.showElement(msg);
        }
    }

    /**
     * Capitalizing first letter of a string.
     *
     */
    var capitalizeFirstLetter = function(string) {
        return string[0].toUpperCase() + string.slice(1);  
    }

    return {
        init: function() {
            // Setting listeners in DOM.
            setListeners();
            // Loading information about currently logged in user.
            ApiController.currentUser(loadUserCallback);
        }
    };
});