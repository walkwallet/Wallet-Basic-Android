package systems.v.wallet.basic.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {

    public static String SHA256(final String text)
    {
        return SHA(text, "SHA-256");
    }

    /**
     *
     * @param text
     * @param type SHA-256 , SHA-512
     * @return
     */
    private static String SHA(final String text, final String type) {
        String strResult = null;

        if (text != null && text.length() > 0) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(type);
                messageDigest.update(text.getBytes());
                byte byteBuffer[] = messageDigest.digest();
                StringBuilder strHexString = new StringBuilder();
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }
}
