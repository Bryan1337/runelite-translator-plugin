package util;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JSONHelper {

    public static Map<String, String> getJsonMapFromPath(String path) {
        try {
            // Read the JSON file using FileReader and BufferedReader
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                stringBuilder.append(line);
            }

            bufferedReader.close();
            // Get the JSON content as a string
            String jsonContent = stringBuilder.toString();

            return parseJsonToHashMap(jsonContent);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return new HashMap<>();
    }

    private static Map<String, String> parseJsonToHashMap(String json) {
        Map<String, String> hashMap = new HashMap<>();

        String patternString = "\"(.*?)\"\\s*:\\s*\"(.*?)\"";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(json);

        // Iterate through matches and populate the HashMap
        while (matcher.find()) {

            String key = matcher.group(1); // Extract content within the first set of quotes
            String value = matcher.group(2); // Extract content within the second set of quotes
            hashMap.put(key, value);
        }

        return hashMap;
    }

}
