package systems.v.wallet.basic.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        if (unity == 0){
            return new BigDecimal(amount).toPlainString();
        }
        try {
            BigDecimal decimal = new BigDecimal(amount).divide(new BigDecimal(unity));
            if (decimal.compareTo(BigDecimal.ZERO) == 0) {
                decimal = decimal.setScale(2);
            }
            return decimal.stripTrailingZeros().toPlainString();
        }catch (Exception e){
            BigDecimal decimal = new BigDecimal(amount).divide(new BigDecimal(unity), 12, RoundingMode.FLOOR);
            if (decimal.compareTo(BigDecimal.ZERO) == 0) {
                decimal = decimal.setScale(2);
            }
            return decimal.stripTrailingZeros().toPlainString();
        }
    }

    public static String formatWithUnit(long amount) {
        return format(amount) + " " + UNIT;
    }

    public static String formatWithUnit(long amount, long unity, String unit) {
        return format(amount, unity) + " " + unit;
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

        return decimal.longValue();
    }

    public static long parse(String amount, long unity) {
        BigDecimal decimal = parseBigDecimal(amount, unity);

        if (decimal != null){
            return decimal.longValue();
        }
        return 0;
    }

    public static BigDecimal parseBigDecimal(String amount, long unity){
        if (TextUtils.isEmpty(amount)) {
            return null;
        }
        amount = amount.replace(UNIT, "").trim();
        BigDecimal decimal = new BigDecimal(amount).multiply(new BigDecimal(unity));
        if (!validate(amount, unity)) {
            return null;
        }

        return decimal;
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

    public static boolean validate(String amount, long unity) {
        try {
            int scale = Double.valueOf(Math.log10(unity)).intValue();
            BigDecimal maxLong = new BigDecimal(Long.MAX_VALUE).movePointLeft(scale);
            BigDecimal decimal = new BigDecimal(amount);
            return decimal.compareTo(maxLong) <= 0 &&
                    decimal.compareTo(BigDecimal.ZERO) >= 0 && decimal.scale() <= scale;
        } catch (Exception e) {
            return false;
        }
    }
}
