# bookingserv

## Assumptions or Justifications (Prakash)
- ID sent by user will be silently ignored, creating id is application's responsibility in this project. 
- Duplicate requests will be responded with the existing records over and over again(Idempotency requirement)
- Date & time are in ISO-8601 standard format (in UTC Zone)
  - Dates (date_of_birth) are in format yyyy-MM-dd 
  - TimeStamps (check_in, check_out) are in format yyyy-MM-dd'T'HH:mm:ss
- [BookingExceptionHandler](bookingservImplementation/src/main/java/com/paypal/bfs/test/bookingserv/utils/BookingExceptionHandler.java) handles all the exceptions in the project
- [BookingResource](bookingservApi/src/main/java/com/paypal/bfs/test/bookingserv/api/BookingResource.java) documents all response codes & error handlings
- Thank you so much for this opportunity!

## Application Overview
bookingserv is a spring boot rest application which would provide the CRUD operations for `Booking` resource.

There are three modules in this application
- bookingservApi - This module contains the interface.
    - `v1/schema/bookingEntity.json` defines the bookingEntity resource.
    - `jsonschema2pojo-maven-plugin` is being used to create `Booking POJO` from json file.
    - `BookingResource.java` is the interface for CRUD operations on `Booking` resource.
        - POST `/v1/bfs/bookingEntity` endpoint defined to create the resource.
- bookingservImplementation - This module contains the implementation for the rest endpoints.
    - `BookingResourceImpl.java` implements the `BookingResource` interface.
- bookingservFunctionalTests - This module would have the functional tests.

## How to run the application
- Please have Maven version `3.3.3` & Java 8 on your system.
- Use command `mvn clean install` to build the project.
- Use command `mvn spring-boot:run` from `bookingservImplementation` folder to run the project.
- Use postman or curl to access `http://localhost:8080/v1/bfs/bookingEntity` POST or GET endpoint.

## Assignment
We would like you to enhance the existing project and see you complete the following requirements:

- `bookingEntity.json` has only `name`, and `id` elements. Please add `date of birth`, `checkin`, `checkout`, `totalprice`, `deposit` and `addressEntity` elements to the `Booking` resource. Address will have `line1`, `line2`, `city`, `state`, `country` and `zip_code` elements. `line2` is an optional element.
- Add one more operation in `BookingResource` to Get All the bookings. `BookingResource` will have two operations, one to create, and another to retrieve all bookings.
- Implement create and get all the bookings operations in `BookingResourceImpl.java`.
- Please add the unit tests to validate your implementation.
- Please use h2 in-memory database or any other in-memory database to persist the `Booking` resource. Dependency for h2 in-memory database is already added to the parent pom.
- Please make sure the validations done for the requests.
- Response codes are as per rest guidelines.
- Error handling in case of failures.
- Implement idempotency logic to avoid duplicate resource creation.

## Assignment submission
Thank you very much for your time to take this test. Please upload this complete solution in Github and send us the link.