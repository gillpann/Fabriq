package com.fabriq.fabriq.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtil {

    private static final Locale ID = Locale.forLanguageTag("id-ID");

    private MoneyUtil() {
    }

    // Contoh: 185000 -> "Rp 185.000"
    public static String format(double amount) {
        NumberFormat format = NumberFormat.getNumberInstance(ID);
        format.setMaximumFractionDigits(0);
        return "Rp " + format.format(amount);
    }
}