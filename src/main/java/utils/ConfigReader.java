package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.Environment;

import java.io.*;

/**
 * Created by manasb on 27-11-2016.
 */
public class ConfigReader {

    private JsonObject fileContents;

    public ConfigReader() throws Exception {
        read();
    }

    private void read() throws Exception {
        final String configFilePath = "public/config.json";
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFilePath);
        if (inputStream == null) {
            throw new FileNotFoundException(String.format("Could not find config file [%s]", configFilePath));
        }

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = fileReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            fileContents = convertStringToJsonObject(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Environment getEnvironment() {
        return Environment.valueOf(fileContents.get("environment").getAsString());
    }

    public String getWebSocketEndpoint() {
        return fileContents.get("websocket_endpoint").getAsString();
    }

    private JsonObject convertStringToJsonObject(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jsonString).getAsJsonObject();
    }
}
