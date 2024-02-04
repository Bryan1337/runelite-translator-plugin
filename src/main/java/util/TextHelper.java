package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelper {

    public static String extractTextBetweenTags(String input) {

        String pattern = "<col=\\w+>(.*?)</col>";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(input);

        if (matcher.find()) {

            return matcher.group(1);
        }

        return "";
    }

    public static String reinsertTextBetweenTags(String input, String modifiedText) {

        String pattern = "(<col=\\w+>)(.*?)(</col>)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
            return input.replace(matcher.group(2), modifiedText);
        }
        return input;
    }


    public static String extractTextBetweenColTags(String input) {
        // Define regular expression to match the text between specific tags with variable colors
        String regex = "<col=[a-fA-F0-9]{6}>(.*?)<col=[a-fA-F0-9]{6}>";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1); // Extracted text between <col> tags
        } else {
            return ""; // Return an empty string if no match is found
        }
    }

    public static String replaceTextBetweenColTags(String input, String oldValue, String newValue) {
        // Define regular expression to match the text between specific tags with variable colors
        String regex = "(<col=[a-fA-F0-9]{6}>)(.*?)(<col=[a-fA-F0-9]{6}>)";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Replace the text between tags with new text
        StringBuilder replacedText = new StringBuilder();
        int lastIndex = 0;
        while (matcher.find()) {
            replacedText.append(input, lastIndex, matcher.start(2)); // Append text before extracted part
            String extractedText = matcher.group(2); // Extracted text between <col> tags

            // Replace oldValue with newValue
            String replacedTextSegment = extractedText.replace(oldValue, newValue);
            replacedText.append(replacedTextSegment); // Append replaced text
            lastIndex = matcher.end(2); // Update lastIndex to point after the extracted part
        }
        replacedText.append(input.substring(lastIndex)); // Append remaining text

        return replacedText.toString();
    }

    public static ArrayList<String> extractWordBetweenTags(String inputString) {

        String[] words = inputString.split("<[^>]+>");

        ArrayList<String> wordList = new ArrayList<>();

        for (String word : words) {
            if (!word.trim().isEmpty()) {

                wordList.add(word.trim());
            }
        }

        return wordList;
    }
}
