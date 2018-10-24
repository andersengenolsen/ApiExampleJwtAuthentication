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
        registerForm: "#register-form",
        resetPasswordForm: "#reset-password-form",
        newPasswordForm : "#new-password-form",
        passwordRep: "#passwordRep",
        regError: "#reg-error",
        loginError: "#login-error",
        regSuccess: "#reg-success",
        resetMessage: "#reset-msg"
    };

    // Login choice
    const loginCh = document.querySelector(DOMString.loginChoice);
    // Register choice
    const regCh = document.querySelector(DOMString.registerChoice);
    // Login form
    const loginForm = document.querySelector(DOMString.loginForm);
    // Register form
    const registerForm = document.querySelector(DOMString.registerForm);
    // Reset password form
    const resetPasswordForm = document.querySelector(DOMString.resetPasswordForm);
    // Set new password form
    const newPasswordForm = document.querySelector(DOMString.newPasswordForm);
    // Password repeat field
    const passwordRepeat = document.querySelector(DOMString.passwordRep);
    // Registration and login error
    const regError = document.querySelector(DOMString.regError);
    const loginError = document.querySelector(DOMString.loginError);
    const regSuccess = document.querySelector(DOMString.regSuccess);
    const resetMessage = document.querySelector(DOMString.resetMessage);

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
        resetPasswordForm.addEventListener("submit", function(event) {
            event.preventDefault();
            postResetRequest(resetPasswordForm);
        });
        newPasswordForm.addEventListener("submit", function(event) {
            event.preventDefault();
            postNewPassword(newPasswordForm);
        });

    };

    var postNewPassword = function(form) {
        const formData = new FormData(form);
        
        // Converting to JSON
        let obj = {};
        for(const keyvalue of formData.entries()) {
            obj[keyvalue[0]] = keyvalue[1];
        }

        let json = JSON.stringify(obj);
        console.log(json);
    };

    /*
     * Posting reset password request through ApiController.
     * Callback handled in processResetResult
     */
    var postResetRequest = function(form) {
        const formData = new FormData(form);

        // Converting to JSON
        let obj = {};
        for(const keyvalue of formData.entries()) {
            obj[keyvalue[0]] = keyvalue[1];
        }

        ApiController.requestReset(JSON.stringify(obj), processResetResult);
    };


    /*
     * Posting form data to the API.
     * The form object decides which function it will call in the
     * ApiController.
     * 
     * The provided callback method is #renderResult()
     *
     */
    var postData = function(form) {
        // FormData object
        const formData = new FormData(form);

        let isLogin = (form === loginForm) ? true : false;
        let errors = (isLogin) ? loginError : regError;

        // Removing error field
        hideElement(errors);

        // Converting to JSON
        let obj = {};
        for(const keyvalue of formData.entries()) {
            if(keyvalue[1].length < 2) {
                errors.innerHTML = "Too short input";
                showElement(errors);
                return false;
            }
            obj[keyvalue[0]] = keyvalue[1];
        }

        let json = JSON.stringify(obj);

        if(isLogin)
            ApiController.login(json, loginResult);
        else {
            // Verifying that password fields do match,
            // returning if no match.
            if(passwordRepeat.value !== obj["password"] ||
               passwordRepeat.value.length === 0) {
                errors.innerHTML = "Passwords must match!"
                showElement(errors);
                return;
            }
            // Passing data to the api controller.            
            ApiController.register(json, registerResult);
        }
    };

    /*
     * Callback method for the ApiController.
     * Showing error message from login operation,
     * or logging user in.
     */
    var loginResult = function(data, success = false) {
        if(data === undefined) {
            loginError.innerHTML = "Network error occurred";
            showElement(loginError);
        }
        else if(data["status"] != 200) {
            loginError.innerHTML = data["message"];
            showElement(loginError);
        } else {
            // Can now extract the JWT bearer token, and
            // redirect the user to the users home page!
            localStorage.setItem("token",data["accessToken"]);
            console.log(localStorage.getItem("token"));
            window.location.href = "user.html";
        }
    }

    /*
     * Callback method for the API calls. This method is passed to the
     * ApiController, and called when the API call is finished.
     * Showing error message from register operation, or registering user.
     */
    var registerResult = function(data, success = false) {
        if(data === undefined) {
            regError.innerHTML = "Network error occurred";
            showElement(regError);
        }
        else if(data["status"] != 200) {
            regError.innerHTML = data["message"];
            showElement(regError);
        } else {
            hideElement(regError);
            showElement(regSuccess);
        }
    }

    /**
     * Callback method for requests to reset password.
     *
     */
    var processResetResult = function(data, success = false) {

        if(data == undefined) {
            resetMessage.innerHTML = "Network error occurred";
            showElement(resetMessage);
        } else if(data["status"] != 200) {
            resetMessage.innerHTML = data["message"];
            showElement(resetMessage);
        } else {
            $('#forgotPasswordModal').modal("hide");
            $("#set-password-modal").modal("show");
        }

    };

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