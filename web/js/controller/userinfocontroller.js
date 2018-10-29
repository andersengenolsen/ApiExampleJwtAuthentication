define(["controller/apicontroller"], function(ApiController) {

    const DOMString = {
        usernamewelcome: "#username-welcome",
        userinfo: ".user-info"
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
            console.log(key + " " + data[key]);
            let li = document.createElement("li");
            li.innerHTML = capitalizeFirstLetter(key) + ": " + data[key];
            ul.appendChild(li);
        }
    };
    
    /**
     * Capitalizing first letter of a string.
     *
     */
    var capitalizeFirstLetter = function(string) {
      return string[0].toUpperCase() + string.slice(1);  
    }

    return {
        init: function() {
            ApiController.currentUser(loadUserCallback);
        }
    };
});