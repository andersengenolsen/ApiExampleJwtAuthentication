/* Controller for the forms */
define(["model/loginrequest", "model/registerrequest", "controller/apicontroller"], function(
       LoginRequest, RegisterRequest, ApiController) {

    // Document Object Model strings.
    const DOMString = {
        loginRegisterChoiceContainer: ".choices",
        loginChoice: "#login",
        registerChoice: "#register",
        selectedChoice: ".choice",
        loginForm: "#login-form",
        registerForm: "#register-form"
    };

    // Login choice
    const loginCh = document.querySelector(DOMString.loginChoice);
    // Register choice
    const regCh = document.querySelector(DOMString.registerChoice);

    // Login form
    let loginForm = document.querySelector(DOMString.loginForm);
    // Register form
    let registerForm = document.querySelector(DOMString.registerForm);

    // Setting eventlisteners
    var setEventListeners = function() {

        // Event listener for choices, login / register
        loginCh.addEventListener("click", function() {
            switchSelected(loginCh);
            showElement(loginForm);
            hideElement(registerForm);
        });

        regCh.addEventListener("click", function() {
            switchSelected(regCh);
            showElement(registerForm);
            hideElement(loginForm);
        });

        // Form listeners
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();
            postData(loginForm);
        });
        registerForm.addEventListener("submit", function (event) {
            event.preventDefault();
            postData(registerForm);
        });
    };

    // Posting form data to the API.
    var postData = function(form) {
        // FormData object
        const formData = new FormData(form);

        // Converting to JSON
        let obj = {};
        formData.forEach((value, key) => {
            obj[key] = value; 
        });
        let json = JSON.stringify(obj);

        if(form === loginForm)
            ApiController.login(json, renderResult);
        else
            ApiController.register(json, renderResult);
    };

    // Rendering result for calls against the api
    var renderResult = function(data, success = false) {
        if(data["status"] != 200) {
            console.log(data["message"]);
        } else {
            console.log(data["message"]);
        }
    }

    // Hiding provided element, adding display none if no parameter provided
    var hideElement = function(el, value = "none") {
        el.style.display = "none";
    }

    // Showing provided form, adding display block if no parameter provided
    var showElement = function(el, value = "block") {
        el.style.display = value;
    }

    // Switching class of currently selected option, login / register
    var switchSelected = function(currentlySelected) {
        if(currentlySelected === loginCh) {
            loginCh.classList.add("selected");
            regCh.classList.remove("selected");
        } else {
            regCh.classList.add("selected");
            loginCh.classList.remove("selected");
        }
    };

    // Public methods
    return {

        // Public init method
        init: function() {
            setEventListeners();
        }
    };

});