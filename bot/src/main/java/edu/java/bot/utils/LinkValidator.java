package edu.java.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkValidator {

    public static boolean isValidLink(String link) {
        return isGitHubLink(link) || isStackOverflowLink(link);
    }

    @SuppressWarnings("MagicNumber")
    private static boolean isGitHubLink(String link) {
        String[] parts = link.split("/");
        if (parts.length != 5) {
            return false;
        }
        return link.startsWith("https://github.com/")
            && !parts[3].isEmpty()
            && !parts[4].isEmpty();
    }

    @SuppressWarnings("MagicNumber")
    private static boolean isStackOverflowLink(String link) {
        String[] parts = link.split("/");
        if (parts.length != 6) {
            return false;
        }
        return link.startsWith("https://stackoverflow.com/questions/")
            && !parts[4].isEmpty()
            && isNumeric(parts[4])
            && !parts[5].isEmpty();
    }

    private static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
