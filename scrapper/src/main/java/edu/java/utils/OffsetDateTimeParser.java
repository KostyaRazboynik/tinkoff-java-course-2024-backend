package edu.java.utils;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OffsetDateTimeParser {

    public static OffsetDateTime timestampToOffsetDateTime(Timestamp timestamp) {
        return OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of("Z"));
    }
}
