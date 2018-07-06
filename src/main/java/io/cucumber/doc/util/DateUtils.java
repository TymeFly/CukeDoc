package io.cucumber.doc.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.annotation.Nonnull;

/**
 * Time/date utilities
 */
public class DateUtils {
    private DateUtils() {
    }


    /**
     * Returns the current time/date in the local timezone
     * @return the current time/date in the local timezone
     */
    @Nonnull
    public static Date localDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneOffset zone = OffsetDateTime.now().getOffset();

        return Date.from(localDateTime.toInstant(zone));
    }
}
