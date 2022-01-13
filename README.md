# Vaadin 22 + Spring Boot + Auth0

Migrated basic functionality from @alump's Vaadin 8 + Spring Boot + Auth0  
(https://github.com/alump/Auth0Demo) and @johannest who migrated for Vaadin 14 (https://github.com/johannest/Auth0Demo) but it wasn't working for me.


If you want to play around with this project, you need to: https://github.com/alump/Auth0Demo

1. Register yourself as developer to Auth0
2. Create new Client under your account
3. Copy your **domain**, **client ID** and **client secret** from Auth0
4. (SKIP) Removed Management for simplicity
5. Create file ```auth0.properties``` to ```src/main/resources```, you can use ```auth0-default.properties``` as your template
6. Compile project with ```mvn clean install```
7. Start the Spring Boot app with ```java -jar target/Auth0Demo.jar```
8. Go to ```http://localhost:8080``` with your browser


Notice that you need to add your URLs to login and logout URL allowed lists on auth0.com.

