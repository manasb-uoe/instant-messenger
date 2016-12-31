# Instant Messenger
[![Build Status](https://travis-ci.org/enthusiast94/instant-messenger.svg?branch=master)](https://travis-ci.org/enthusiast94/instant-messenger)

Instant messenger application implemented using [Spark Java](http://sparkjava.com/) and [Angular 2](https://angular.io/). The application is hosted on an AWS EC2 instance, and can be accessed at: 
- http://ec2-52-56-74-93.eu-west-2.compute.amazonaws.com:4200.

## How to get started?
### Start client: 
Navigate to `client` directory and run the following commands:
- `npm install`
- `npm start`

### Start server:
Navigate to `server` directory and run the following commands: 
- `mvn clean package`
- `java -jar target/instant-messenger-server-1.0-jar-with-dependencies.jar`

The application can then be accessed from the browser at the following URL: 
`http://localhost:4200` 

**Note:** Server config can be found in `server/src/main/resources/public/config.json` and client config can be found in `client/src/config.json`. 

### Run server tests:
Navigate to `server` directory and run the following command:
- `mvn test`
