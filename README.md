# Instant Messenger - For Web and Windows
[![Build Status](https://travis-ci.org/enthusiast94/instant-messenger.svg?branch=master)](https://travis-ci.org/enthusiast94/instant-messenger)

Instant messenger application implemented using [Spark Java](http://sparkjava.com/), [Angular 2](https://angular.io/) and [WPF](https://docs.microsoft.com/en-us/dotnet/framework/wpf/). 
- The application is hosted on an AWS EC2 instance, and can be accessed at: http://ec2-52-56-74-93.eu-west-2.compute.amazonaws.com:4200.
- The native Windows client can be downloaded here: https://github.com/enthusiast94/instant-messenger/raw/master/InstantMessenger.rar

##Screenshots
![Login screen](https://github.com/enthusiast94/instant-messenger/blob/master/screenshots/2.png)
![Chat screen](https://github.com/enthusiast94/instant-messenger/blob/master/screenshots/1.png)

## How to get started?
### Start web client: 
Navigate to `client` directory and run the following commands:
- `npm install`
- `npm start`

The application can then be accessed from the browser at the following URL: 
`http://localhost:4200` 

### Start windows client:
Open `WpfClient` solution in Visual Studio and click 'Start'.

### Start server:
Navigate to `server` directory and run the following commands: 
- `mvn clean package`
- `java -jar target/instant-messenger-server-1.0-jar-with-dependencies.jar`

### Run server tests:
Navigate to `server` directory and run the following command:
- `mvn test`
