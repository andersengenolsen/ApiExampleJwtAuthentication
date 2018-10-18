API example with authentication, developed in Spring Boot.

Can serve as a starter project for APIs with JWT authentication. 

The API currently has the following endpoints;

/api/auth/signup - Signing up

/api/auth/signin - Signing in, returning a JWT bearer token.

/api/user/{username} - Returning summary of a given username, e-mail and id excluded.

/api/user/me - Returning summary of currently logged in user, e-mail and id included

Authentication done with Spring Security and Json Web Tokens.

Backend consists of a MySQL database.
