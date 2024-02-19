# API Ecommerce  v1.0
<hr>
This project demonstrates the implementation of security using Spring Boot 3.0 and JSON Web Tokens (JWT). It includes the following features:


### Features
<hr>
* User registration and login with JWT authentication
* Password encryption using BCrypt
* Role-based authorization with Spring Security
* Customized access denied handling
* Logout mechanism
* Refresh token
* Product, File, Order management
* Social Oauth2 login
* Integrated with VNPAY

### Technologies
<hr>
* Spring boot 3.0 
* Spring Security
* Spring Data JPA
* JSON Web Tokens (JWT)
* BCrypt 
* Gradle
* MySql database
* VNPAY Api

### Getting Started
<hr>
To get started with this project, you will need to have the following installed on your local machine:

* JDK 17+
* Maven 3+ 

To build and run the project, follow these steps:

* Clone the repository: git clone https://github.com/hqlinh99/ecommerce-api.git
* Navigate to the project directory: cd spring-boot-security-jwt
* Add database "ecommerce_api" to mysql
* Build the project: ./gradle build
* Run the project: ./gradlew run
* -> The application will be available at http://localhost:8080.

### Reference Documentation
<hr>
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#io.validation)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web.security)