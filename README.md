Spring Session Bug
===================

This test application proofs an existing problem on the Spring Session integration with Spring Security.

The session managment funcionality in Spring Security allows to redirect the user to a configured URL when his session is invalid, because of a timeout, for example.

To configure this, we simply define the URL on the `HttpSecurity` object:

```java
http.sessionManagement().invalidSessionUrl("/expired");
```

The above works if `spring.session.store-type` is set to `none`.
As soon as we comment that line or we set it to `redis`, for example, Spring will no longer redirect the user to the invalid session URL.

Testing the expected behaviour
------------------------------

1. Start up the application by running `./gradlew bootRun`
2. Go to http://localhost:8080/login
3. Login using "user" as the username and "password" as the password.
4. Wait 10 seconds untill the session expires on the server.
5. Hit refresh on your browser, you should get redirected to "http://localhost:8080/expired".

Testing the bug
------------------------------

1. Open `application.properties` and comment out the following line:
```java
spring.session.store-type=none
```
2. Start up the application by running `./gradlew bootRun`
3. Go to http://localhost:8080/login
4. Login using "user" as the username and "password" as the password.
5. Wait 10 seconds untill the session expires on the server.
6. You will be redirected to the login page instead of the invalid session Url.