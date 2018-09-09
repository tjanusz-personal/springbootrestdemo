# Spring Boot REST Demo

The spring boot REST demo project is a simple spring boot web project to contain some of the non-standard hello world
 things I've figured out on some of my projects. I put this project together to help remind my 30 day self of things 
 me and my teams have figured out especially since I don't use Spring everyday at work.

Includes:
* Spring Boot project w/executable JAR and embedded Tomcat server
* Spring REST Controller (for our REST endpoint examples)
* Spring Boot Actuator Support (https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html)

There are some REST endpoints you can invoke that will be further built out
* `http://localhost:8080/alive` - demonstrates simple alive ping GET request
* `http://localhost:8080/saveUrl` - demonstrates simple POST request with various options
 
Some features demo'd include:
* field validation using annotations and custom annotation types
* exception handling including body content response data hook
* HTTP request header mapping
* HTTP response header defaulting
* Supporting multiple content type responses XML/JSON
* Supporting multiple different media types: 'application/x-www-form-urlencoded', 'application/json'

## Installation
This project requires using Java 8.

This project requires Gradle for all build/run activities. A wrapper that you can use is included
with the project, but you can also install gradle locally via `brew install gradle`.

## Running
To run from a bash terminal window

* `./gradlew bootrun` - compiles, resolves all dependencies and starts spring boot server.
* `./gradlew build` - only builds the project into an executable JAR (not required if doing bootrun)

Once running in a terminal window can use embedded Spring Boot Actuator REST service to verify successful start by navigating to these
endpoints in a browser.
* `http://localhost:8080/actuator/health` - simple status indicator
* `http://localhost:8080/actuator/info` - simple info dump indicator
* `http://localhost:8080/actuator/beans` - dump out beans (what beans got loaded)
* `http://localhost:8080/actuator/env` - dump out env variables (see what it thinks its reading from ENV)
* `http://localhost:8080/actuator/configprops` - dump out config beans (so can tell how stuff is setup)

## Testing

Tests simple unit tests using spring web tests.

### Running Tests
* **IntelliJ IDE** - To run tests from within IntelliJ, simply right-click on a test file and choose RUN (Ctrl Shift F10)

# Further Work
This project is by no means finished! When I get a chance I'll add more examples to it including: 

* fix the bug where RestResponseEntityExceptionHandler isn't honoring the 'x-www-form-urlencoded' content type
* docker configuration/execution
* security - I never did this before in spring boot so nice to figure this out
* various AWS integrations we figured out in the past