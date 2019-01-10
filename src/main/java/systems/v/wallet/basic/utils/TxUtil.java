package systems.v.wallet.basic.utils;

import android.content.Context;
import android.text.TextUtils;

import systems.v.wallet.basic.R;
import systems.v.wallet.basic.wallet.Transaction;

public class TxUtil {

    public static String getTypeText(Context context, int type) {
        int textId = 0;
        switch (type) {
            case Transaction.PAYMENT:
                textId = R.string.tx_type_payment;
                break;
            case Transaction.LEASE:
                textId = R.string.tx_type_lease;
                break;
            case Transaction.CANCEL_LEASE:
                textId = R.string.tx_type_cancel_lease;
                break;
        }
        if (textId != 0) {
            return context.getString(textId);
        }
        return "";
    }

    public static String encodeAttachment(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                return Base58.encode(s.getBytes());
            } catch (Exception ignore) {

            }
        }
        return "";
    }

    public static String decodeAttachment(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                return new String(Base58.decode(s));
            } catch (Exception ignore) {

            }
        }
        return s;
    }
}
