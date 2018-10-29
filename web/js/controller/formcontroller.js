/* Controller for the forms */
define(["controller/apicontroller", "util/helper"], function(ApiController, Helper) {

    // Document Object Model strings.
    const DOMString = {
        loginCh: document.querySelector("#login"),
        regCh: document.querySelector("#register"),
        loginForm: document.querySelector("#login-form"),
        registerForm: document.querySelector("#register-form"),
        resetTokenForm: document.querySelector("#reset-password-form"),
        newPasswordForm : document.querySelector("#new-password-form"),
        password: document.querySelector("#set-password"),
        passwordRepeat: document.querySelector("#passwordRep"),
        regError: document.querySelector("#reg-error"),
        loginError: document.querySelector("#login-error"),
        regSuccess: document.querySelector("#reg-success"),
        resetMessage: document.querySelector("#reset-msg"),
        newPasswordMessage: document.querySelector("#new-password-msg")
    };

    // Setting eventlisteners
    var setEventListeners = function() {

        // Event listener for choices, login / register
        DOMString.loginCh.addEventListener("click", function() {
            switchSelected(DOMString.loginCh);
            Helper.showElement(DOMString.loginForm);
            Helper.hideElement(DOMString.registerForm);
        });
        DOMString.regCh.addEventListener("click", function() {
            switchSelected(DOMString.regCh);
            Helper.showElement(DOMString.registerForm);
            Helper.hideElement(DOMString.loginForm);
        });

        // Form listeners
        DOMString.loginForm.addEventListener("submit", function (event) {
            event.preventDefault();
            postData(DOMString.loginForm);
        });
        DOMString.registerForm.addEventListener("submit", function (event) {
            event.preventDefault();
            postData(DOMString.registerForm);
        });
        DOMString.resetTokenForm.addEventListener("submit", function(event) {
            event.preventDefault();
            postResetRequest(DOMString.resetTokenForm);
        });
        DOMString.newPasswordForm.addEventListener("submit", function(event) {
            event.preventDefault();
            postNewPassword(DOMString.newPasswordForm);
        });

    };

    /**
     * Posting new password to the API. 
     * Callback handled in processNewPasswordResult.
     *
     '
     */
    var postNewPassword = function(form) {

        const formData = new FormData(form);
        let json = Helper.formDataToJson(formData);

        ApiController.postNewPassword(json, processNewPasswordResult);
    };


    /*
     * Posting reset password request through ApiController.
     * Callback handled in processResetResult
     */
    var postResetRequest = function(form) {
        const formData = new FormData(form);
        // Converting to JSON
        let json = Helper.formDataToJson(formData);

        ApiController.requestReset(json, processResetResult);
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

        let isLogin = (form === DOMString.loginForm) ? true : false;
        let errors = (isLogin) ? DOMString.loginError : DOMString.regError;

        // Removing error field
        Helper.hideElement(errors);

        // FormData object
        const formData = new FormData(form);
        let json = Helper.formDataToJson(formData);

        if(isLogin)
            ApiController.login(json, loginResult);
        else {
            // Verifying that password fields do match,
            // returning if no match.
            if(DOMString.passwordRepeat.value !== DOMString.password.value ||
               DOMString.passwordRepeat.value.length === 0) {
                errors.innerHTML = "Passwords must match!"
                Helper.showElement(errors);
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
            DOMString.loginError.innerHTML = "Network error occurred";
            Helper.showElement(DOMString.loginError);
        }
        else if(data["status"] != 200) {
            DOMString.loginError.innerHTML = data["message"];
            Helper.showElement(DOMString.loginError);
        } else {
            // Can now extract the JWT bearer token, and
            // redirect the user to the users home page!
            localStorage.setItem("token",data["accessToken"]);
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
            DOMString.regError.innerHTML = "Network error occurred";
            Helper.showElement(DOMString.regError);
        }
        else if(data["status"] != 200) {
            DOMString.regError.innerHTML = data["message"];
            Helper.showElement(DOMString.regError);
        } else {
            Helper.hideElement(DOMString.regError);
            Helper.showElement(DOMString.regSuccess);
        }
    }

    /**
     * Callback method for requests to reset password token. 
     *
     */
    var processResetResult = function(data, success = false) {
        if(data == undefined) {
            DOMString.resetMessage.innerHTML = "Network error occurred";
            Helper.showElement(DOMString.resetMessage);
        } else if(data["status"] != 200) {
            DOMString.resetMessage.innerHTML = data["message"];
            Helper.showElement(DOMString.resetMessage);
        } else {
            $('#forgotPasswordModal').modal("hide");
            $("#set-password-modal").modal("show");
        }

    };

    /**
     * Callback for when password has been changed.
     *
     */
    var processNewPasswordResult = function(data, success = false) {
        if(data == undefined) {
            DOMString.newPasswordMessage.innerHTML = "Network error occurred";
        } else {
            DOMString.newPasswordMessage.innerHTML = data["message"];
        }
        Helper.showElement(DOMString.newPasswordMessage);
    };

    // Switching class of currently selected option, login / register
    var switchSelected = function(currentlySelected) {
        if(currentlySelected === DOMString.loginCh) {
            DOMString.loginCh.classList.add("selected");
            DOMString.regCh.classList.remove("selected");
        } else {
            DOMString.regCh.classList.add("selected");
            DOMString.loginCh.classList.remove("selected");
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