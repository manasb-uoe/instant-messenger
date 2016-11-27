# What is this about?
Instant messenger application implemented using Spark (Websockets) and React.js

# How to get started?
Navigate to `src/main/resources/public` and run the following commands: 
- `npm install` to download all npm dependencies.
- `npm run build` to build javascript project into `bundle/bundle.js`.

Then navigate to the root directory and run: 
- `mvn package` to build jar file for the entire project.
- `java -jar target/instant-messenger-1.0-jar-with-dependencies.jar` to start the server. 

**Note:** Application config can be found in `src/main/resources/public/config.json`. 
