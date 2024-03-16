package edu.java.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkValidator {

    public static LinkType getLinkType(String link) {
        if (isStackOverflowLink(link)) {
            return LinkType.STACKOVERFLOW_LINK;
        } else if (isGitHubLink(link)) {
            return LinkType.GITHUB_LINK;
        } else {
            return LinkType.UNKNOWN_LINK;
        }
    }

    @SuppressWarnings("MagicNumber")
    public static boolean isGitHubLink(String link) {
        String[] parts = link.split("/");
        if (parts.length != 5) {
            return false;
        }
        return link.startsWith("https://github.com/")
            && !parts[3].isEmpty()
            && !parts[4].isEmpty();
    }

    @SuppressWarnings("MagicNumber")
    public static boolean isStackOverflowLink(String link) {
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
