package systems.v.wallet.basic.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

public class CoinUtil {

    private static final String UNIT = " VSYS";

    public static String format(long amount) {
        BigDecimal decimal = new BigDecimal(amount).movePointLeft(8);
        if (decimal.compareTo(BigDecimal.ZERO) == 0) {
            decimal = decimal.setScale(2);
        }
        return decimal.stripTrailingZeros().toPlainString();
    }

    public static String formatWithUnit(long amount) {
        return format(amount) + UNIT;
    }

    public static long parse(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return 0;
        }
        amount = amount.replace(UNIT, "");
        if (!validate(amount)) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(amount).movePointRight(8);
        BigDecimal maxLong = new BigDecimal(Long.MAX_VALUE);
        if (decimal.compareTo(maxLong) > 0) {
            return 0;
        }
        return decimal.longValueExact();
    }

    public static boolean validate(String amount) {
        try {
            BigDecimal decimal = new BigDecimal(amount);
            return decimal.compareTo(BigDecimal.ZERO) >= 0 && decimal.scale() <= 6;
        } catch (Exception e) {
            return false;
        }
    }
}
