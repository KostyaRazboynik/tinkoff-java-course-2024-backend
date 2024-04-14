package edu.java.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkValidator {

    private static final String GITHUB_URL = "https://github.com/";
    private static final String STACKOVERFLOW_URL = "https://stackoverflow.com/questions/";
    private static final int GITHUB_URL_PARTS_SIZE = 5;
    private static final int STACKOVERFLOW_URL_PARTS_SIZE = 6;

    public static boolean isValidLink(String link) {
        return isGitHubLink(link) || isStackOverflowLink(link);
    }

    @SuppressWarnings("MagicNumber")
    private static boolean isGitHubLink(String link) {
        String[] parts = link.split("/");
        if (parts.length != GITHUB_URL_PARTS_SIZE) {
            return false;
        }
        return link.startsWith(GITHUB_URL)
            && !parts[3].isEmpty()
            && !parts[4].isEmpty();
    }

    @SuppressWarnings("MagicNumber")
    private static boolean isStackOverflowLink(String link) {
        String[] parts = link.split("/");
        if (parts.length != STACKOVERFLOW_URL_PARTS_SIZE) {
            return false;
        }
        return link.startsWith(STACKOVERFLOW_URL)
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
