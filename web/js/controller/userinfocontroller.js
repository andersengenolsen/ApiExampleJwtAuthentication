define(["controller/apicontroller"], function(ApiController) {

    const DOMString = {
        userInfo: ".user-info"
    };

    var loadUserCallback = function(data) {
        document.querySelector(DOMString.userInfo).innerHTML = JSON.stringify(data);
        console.log(data);
    }

    return {
        init: function() {
            ApiController.currentUser(loadUserCallback);
        }
    };
});