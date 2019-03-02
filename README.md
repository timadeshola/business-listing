# Country Resource API
Test-Driven backend application RESTFUL API for performing CRUD operations on a Country resources

Stack: Spring Boot

Modules:

User Management Module

Create new user
Update existing user
Delete user
Toggle user status (user status determines if the user can login or not)
View all users
View user by username

Role Management Module

Create new role.
Update existing role
Delete role
Toggle role status
View all roles
View role by role name

Country Management Module

Add business
Update business
Delete business
View all countries
View business by business name


Roles: 
S/N	    Role	    Access Control      Description

1.      Admin	    Super Admin         Super admin can add, update, delete and view every module on the system
2.      User	    User                User can only view countries

DEPLOYMENT

Local Deployment:
Pull the source code from github repository here or download as zip file: https://github.com/timadeshola/countries.git

If you download as zip file, extract the zip code, 
* open IntelliJ, click on open existing project, 
* select the pom.xml file 
* click on Ok, 
* Intellij will ask you if you want to open it as new project or new file, 
* click on Project.
* Run mvn clean install
* The run the application from your IntelliJ using the play button.

This application is also hosted on Heroku.
See link here https://countries-rest-api.herokuapp.com/swagger-ui

This application implement the following features:

* [Spring Boot]
* [Securing a Web Application using Spring Security and JWT]
* [Building REST services with Spring Boot]
* [Spring Data JPA]
* [Accessing data with SQL Server, PostgreSQL and H2]
* [Spring Actuator]
* [Building system Metric and using micrometer prometeus]



