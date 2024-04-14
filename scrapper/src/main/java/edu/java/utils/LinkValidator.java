package edu.java.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkValidator {

    private static final String GITHUB_URL = "https://github.com/";
    private static final String STACKOVERFLOW_URL = "https://stackoverflow.com/questions/";
    private static final int GITHUB_URL_PARTS_SIZE = 5;
    private static final int STACKOVERFLOW_URL_PARTS_SIZE = 6;


    public static LinkType getLinkType(String link) {
        if (isStackOverflowLink(link)) {
            return LinkType.STACKOVERFLOW_LINK;
        } else if (isGitHubLink(link)) {
            return LinkType.GITHUB_LINK;
        } else {
            return LinkType.UNKNOWN_LINK;
        }
    }

    public static boolean isValidLink(String link) {
        return isGitHubLink(link) || isStackOverflowLink(link);
    }

    @SuppressWarnings("MagicNumber")
    public static boolean isGitHubLink(String link) {
        String[] parts = link.split("/");
        if (parts.length != GITHUB_URL_PARTS_SIZE) {
            return false;
        }
        return link.startsWith(GITHUB_URL)
            && !parts[3].isEmpty()
            && !parts[4].isEmpty();
    }

    @SuppressWarnings("MagicNumber")
    public static boolean isStackOverflowLink(String link) {
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
