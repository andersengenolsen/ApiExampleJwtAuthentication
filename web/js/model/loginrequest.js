// Module representing a login request
define(function() {
    
    function LoginRequest(usernameOrEmail, password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
    
    return LoginRequest;
    
});