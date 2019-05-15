package systems.v.wallet.basic.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import systems.v.wallet.basic.wallet.Operation;

public class QRCodeUtil {
    private static final String TAG = "QRCodeUtil";
    public static final int pageSize = 4;

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
        op.set("invoice", "");
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

    public static List<Bitmap> generateQRCodes(String message, int width) {
        List<Bitmap> qrCodes = new ArrayList<>();
        try {
            message = new String(message.getBytes(), "ISO-8859-1");
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            List<String> pageMessages = formatPageMessages(message);
            for (int i = 0;i < pageMessages.size();i++){
                qrCodes.add(barcodeEncoder.encodeBitmap(message, BarcodeFormat.QR_CODE, width, width));
            }
        } catch (Exception e) {
            qrCodes.add(Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888));
        }
        return qrCodes;
    }

    public static List<String> formatPageMessages(String message){
        List<String> result = new ArrayList<>();
        if(message == null){
            return null;
        }
        String checksum = SHA.SHA256(message).substring(0,4);

        int total = message.length() / pageSize + 1;
        for (int i = 0;i < total;i++){
            int beginIndex = i * pageSize;
            int endIndex   = (i + 1) * pageSize;
            if (endIndex > message.length()){
                endIndex = message.length();
            }
            String body = message.substring(beginIndex, endIndex);
            StringBuilder builder = new StringBuilder();
            builder.append("Seg/");
            builder.append(i + 1);
            builder.append("/");
            builder.append(total);
            builder.append("/");
            builder.append(checksum);
            builder.append("/");
            builder.append(body);
            result.add(builder.toString());
        }

        return result;
    }

    public static String parsePageMessages(@NonNull List<String> list)throws Exception{
        StringBuilder result = new StringBuilder();
        String checksum = "";
        for(int i=0;i < list.size();i++){
            QRCode qrCode = getPageMessage(list.get(i));
            if(qrCode == null){
                throw new Exception("Parse code failed: " + list.get(i));
            }
            if(i == 0){
                checksum = qrCode.checksum;
            }else{
                if(!checksum.equals(qrCode.checksum))
                    throw new Exception("Not same checksum");
            }
            result.append(qrCode.getBody());
        }
        return result.toString();
    }

    public static QRCode getPageMessage(String msg){
        String[] datas = msg.split("/");
        if (datas.length != 4){
            return null;
        }
        QRCode qrcode = new QRCode();
        qrcode.setCurrent(Integer.parseInt(datas[0]));
        qrcode.setTotal(Integer.parseInt(datas[1]));
        qrcode.setChecksum(datas[2]);
        qrcode.setBody(datas[3]);
        return qrcode;
    }

    public static void main(String[] args){
        String t = "123456789";
        List list = formatPageMessages(t);
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }

    public static class QRCode{
        private int current;
        private int total;
        private String checksum;
        private String body;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
