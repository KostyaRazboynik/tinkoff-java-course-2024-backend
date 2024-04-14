package edu.java.utils;

public enum LinkType {
    GITHUB_LINK(0),
    STACKOVERFLOW_LINK(1),
    UNKNOWN_LINK(-1);

    public final int type;

    LinkType(int type) {
        this.type = type;
    }
}
