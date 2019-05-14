package systems.v.wallet.basic.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;

public class CoinUtil {

    private static final String UNIT = "VSYS";
    private static final BigDecimal MAX_LONG = new BigDecimal(Long.MAX_VALUE).movePointLeft(8);

    public static String format(long amount) {
        BigDecimal decimal = new BigDecimal(amount).movePointLeft(8);
        if (decimal.compareTo(BigDecimal.ZERO) == 0) {
            decimal = decimal.setScale(2);
        }
        return decimal.stripTrailingZeros().toPlainString();
    }

    public static String format(long amount, long unity){
        BigDecimal decimal = new BigDecimal(amount).divide(new BigDecimal(unity));
        if (decimal.compareTo(BigDecimal.ZERO) == 0) {
            decimal = decimal.setScale(2);
        }
        return decimal.stripTrailingZeros().toPlainString();
    }

    public static String formatWithUnit(long amount) {
        return format(amount) + " " + UNIT;
    }

    public static long parse(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return 0;
        }
        amount = amount.replace(UNIT, "").trim();
        if (!validate(amount)) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(amount).movePointRight(8);
        return decimal.longValueExact();
    }

    public static long parse(String amount, long unity) {
        if (TextUtils.isEmpty(amount)) {
            return 0;
        }
        amount = amount.replace(UNIT, "").trim();
        if (!validate(amount)) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(amount).multiply(new BigDecimal(unity));
        return decimal.longValue();
    }

    public static long formatLong(long amount, long unity) {
        if(amount < 0 || unity < 0){
            return 0;
        }

        BigDecimal decimal = new BigDecimal(amount).divide(new BigDecimal(unity));
        return decimal.longValue();
    }

    public static boolean validate(String amount) {
        try {
            BigDecimal decimal = new BigDecimal(amount);
            return decimal.compareTo(MAX_LONG) <= 0 &&
                    decimal.compareTo(BigDecimal.ZERO) >= 0 && decimal.scale() <= 8;
        } catch (Exception e) {
            return false;
        }
    }
}
