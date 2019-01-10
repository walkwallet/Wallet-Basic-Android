package systems.v.wallet.basic.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import systems.v.wallet.basic.wallet.Operation;

public class QRCodeUtil {
    private static final String TAG = "QRCodeUtil";

    public static Bitmap generateQRCode(String message, int width) {
        Bitmap qrCode;
        try {
            message = new String(message.getBytes(), "ISO-8859-1");
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrCode = barcodeEncoder.encodeBitmap(message, BarcodeFormat.QR_CODE, width, width);
        } catch (Exception e) {
            qrCode = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        }
        return qrCode;
    }

    public static String getReceiveAddressStr(String address, long amount) {
        Operation op = new Operation(Operation.ACCOUNT);
        op.set("address", address);
        op.set("amount", amount);
        return op.toStr();
    }

    public static String getSignatureStr(String signature) {
        Operation op = new Operation(Operation.SIGNATURE);
        op.set("signature", signature);
        return op.toStr();
    }

    public static String getExportAddressStr(String address, String publicKey) {
        Operation op = new Operation(Operation.ACCOUNT);
        op.set("address", address);
        op.set("publicKey", publicKey);
        return op.toStr();
    }

    public static String getSeedStr(String seed) {
        Operation op = new Operation(Operation.SEED);
        op.set("seed", seed);
        return op.toStr();
    }
}
