/* Module representing a register request */
define(function() {
    
    function RegisterRequest(firstname, lastname, 
                              username, password,
                             email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    return RegisterRequest;
    
});