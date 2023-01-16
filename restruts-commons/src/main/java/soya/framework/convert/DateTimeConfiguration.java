package soya.framework.convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeConfiguration {
    private String[] patterns;
    private String displayPatterns;
    private Locale locale;
    private TimeZone timeZone;
    private boolean useLocaleFormat;
    
    public String[] getPatterns() {
        return patterns;
    }

    public String getDisplayPatterns() {
        return displayPatterns;
    }

    public Locale getLocale() {
        return locale;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public boolean isUseLocaleFormat() {
        return useLocaleFormat;
    }

    public DateTimeConfiguration setPatterns(String... patterns) {
        this.patterns = patterns;
        if (patterns != null && patterns.length > 1) {
            final StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < patterns.length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                buffer.append(patterns[i]);
            }
            displayPatterns = buffer.toString();
        }
        this.useLocaleFormat = true;
        return this;
    }

    public DateTimeConfiguration setDisplayPatterns(String displayPatterns) {
        this.displayPatterns = displayPatterns;
        return this;
    }

    public DateTimeConfiguration setLocale(Locale locale) {
        this.locale = locale;
        this.useLocaleFormat = true;
        return this;
    }

    public DateTimeConfiguration setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public DateTimeConfiguration setUseLocaleFormat(boolean useLocaleFormat) {
        this.useLocaleFormat = useLocaleFormat;
        return this;
    }

    public DateFormat getFormat(final Locale locale, final TimeZone timeZone) {
        DateFormat format = null;
        if (locale == null) {
            format = DateFormat.getDateInstance(DateFormat.SHORT);
        } else {
            format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        }
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        return format;
    }

    public DateFormat getFormat(final String pattern) {
        final DateFormat format = new SimpleDateFormat(pattern);
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        return format;
    }


}
