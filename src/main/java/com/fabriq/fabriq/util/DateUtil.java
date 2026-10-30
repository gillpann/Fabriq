package com.fabriq.fabriq.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public final class DateUtil {

    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private DateUtil() {
    }

    public static String format(LocalDate date) {
        return FORMAT.format(date);
    }

    // Mengembalikan null jika teks bukan tanggal yang valid (misal 31-02-2026)
    public static LocalDate parse(String text) {
        try {
            return LocalDate.parse(text.trim(), FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}