API example with authentication, developed in Spring Boot.

Can serve as a starter project for APIs with JWT authentication. 

The API currently has the following endpoints;

/api/auth/signup - Signing up, receiving verification link by email.

/api/auth/signin - Signing in, returning a JWT bearer token.

/api/auth/verify/{token} - Verifying email by token.

/api/reset - Generating reset password token, sent by mail to user.

/api/reset/newpassword - Setting new password.

/api/user/{username} - Returning summary of a given username, e-mail and id excluded.

/api/user/me - Returning summary of currently logged in user, e-mail and id included

Authentication done with Spring Security and Json Web Tokens.

Backend consists of a MySQL database.
