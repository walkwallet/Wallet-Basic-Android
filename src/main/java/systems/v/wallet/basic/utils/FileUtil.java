package systems.v.wallet.basic.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import systems.v.vsys.Vsys;
import systems.v.wallet.basic.wallet.Wallet;

public class FileUtil {
    private final static String TAG = "FileUtil";

    public static final String WALLET_FILE_NAME = "wallet.dat";
    private final static String BACKUP_SDCARD_DIR = "Wallet";
    private final static int HASHING_ITERATION_COUNT = 9999;
    private final static int KEY_LENGTH = 256;

    private static HashMap<HashKey, byte[]> mCachedKeys = new HashMap<>();

    private static class HashKey {
        private String password;
        private String salt;
        private int iterationCount;

        private HashKey(String password, String salt, int iterationCount) {
            this.password = password;
            this.salt = salt;
            this.iterationCount = iterationCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashKey hashKey = (HashKey) o;
            return iterationCount == hashKey.iterationCount &&
                    Objects.equals(password, hashKey.password) &&
                    Objects.equals(salt, hashKey.salt);
        }

        @Override
        public int hashCode() {

            return Objects.hash(password, salt, iterationCount);
        }
    }

    /**
     * Save to data dir
     */
    public static boolean save(Context context, Wallet wallet) {
        File folder;
        PrintWriter file = null;
        String password = wallet.getPassword();
        if (password == null) {
            Log.e(TAG, "Wallet save failed, password is empty");
            return false;
        }
        String salt = wallet.getSalt();
        if (salt == null) {
            Log.e(TAG, "Wallet save failed, salt is empty");
            return false;
        }

        try {
            String path = getWalletPath(context.getFilesDir().getPath());
            folder = new File(path).getParentFile();

            if (folder != null && !folder.exists()) {
                folder.mkdirs();
            }

            file = new PrintWriter(path);
            String walletStr = wallet.toWalletStr();
            Map<String, String> data = new HashMap<>();
            data.put("salt", salt);
            data.put("wallet", encrypt(prepareKey(password, salt, HASHING_ITERATION_COUNT), walletStr));
            String savedStr = JSON.toJSONString(data);
            file.write(savedStr);
            Log.d(TAG, "Saved wallet string: " + walletStr);
            Log.d(TAG, "Wallet saved");
            return true;
        } catch (IOException e) {
            Log.d(TAG, "Writing wallet to file error: " + e.getMessage());
            return false;
        } finally {
            if (file != null) {
                file.close();
            }
        }
    }


    /**
     * Load wallet from data dir
     */
    public static Wallet load(Context context, String password) {
        FileInputStream inputStream;
        String walletStr;
        if (password == null) {
            return null;
        }
        try {
            String path = getWalletPath(context.getFilesDir().getPath());
            inputStream = new FileInputStream(path);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            Wallet wallet;
            try {
                JSONObject obj = JSON.parseObject(new String(bytes));
                String salt = obj.getString("salt");
                if (TextUtils.isEmpty(salt)) {
                    Log.e(TAG, "Load wallet failed, salt is empty");
                    return null;
                }
                String encryptedStr = obj.getString("wallet");
                walletStr = decrypt(prepareKey(password, salt, HASHING_ITERATION_COUNT), encryptedStr);
                wallet = JSON.parseObject(walletStr, Wallet.class);
                wallet.setPassword(password);
                wallet.setSalt(salt);
                Log.d(TAG, "Loaded wallet string: " + walletStr);
                Log.d(TAG, "Wallet loaded");
            } catch (Exception e) {
                wallet = null;
                Log.e(TAG, "Load wallet parseJSON error: " + e.getMessage());
            } finally {
                inputStream.close();
            }
            return wallet;
        } catch (IOException e) {
            Log.e(TAG, "Load wallet file error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Backup wallet to external storage
     */
    public static boolean backup(Wallet wallet) {
        if (sdCardMountedExists()) {
            File folder;
            PrintWriter file = null;
            String password = wallet.getPassword();
            if (password == null) {
                Log.e(TAG, "Wallet backup failed, password is empty");
                return false;
            }
            String salt = wallet.getSalt();
            if (salt == null) {
                Log.e(TAG, "Wallet save failed, salt is empty");
                return false;
            }

            try {
                String path = getWalletPath(getBackupDir().getPath());
                folder = new File(path).getParentFile();

                if (folder != null && !folder.exists()) {
                    folder.mkdirs();
                }

                file = new PrintWriter(path);
                String walletStr = wallet.toWalletStr();
                Map<String, String> data = new HashMap<>();
                data.put("salt", salt);
                data.put("wallet", encrypt(prepareKey(password, salt, HASHING_ITERATION_COUNT), walletStr));
                String backupStr = JSON.toJSONString(data);
                file.write(backupStr);
                Log.d(TAG, "Backup wallet string: " + walletStr);
                Log.d(TAG, "Wallet backup saved");
                return true;
            } catch (IOException e) {
                Log.d(TAG, "Writing wallet to backup file error: " + e.getMessage());
                return false;
            } finally {
                if (file != null) {
                    file.close();
                }
            }
        }
        Log.e(TAG, "Saved wallet to backup file, but SD Card not found.");
        return false;
    }


    /**
     * Load backup
     */
    public static Wallet loadBackup(String password) {
        FileInputStream inputStream;
        String walletStr;
        if (password == null) {
            return null;
        }
        try {
            String backupPath = getWalletPath(getBackupDir().getPath());
            inputStream = new FileInputStream(backupPath);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            Wallet wallet;
            try {
                JSONObject obj = JSON.parseObject(new String(bytes));
                String salt = obj.getString("salt");
                if (TextUtils.isEmpty(salt)) {
                    Log.e(TAG, "Load backup wallet failed, salt is empty");
                    return null;
                }
                String encryptedStr = obj.getString("wallet");
                walletStr = decrypt(prepareKey(password, salt, HASHING_ITERATION_COUNT), encryptedStr);
                wallet = JSON.parseObject(walletStr, Wallet.class);
                wallet.setPassword(password);
                wallet.setSalt(salt);
                Log.d(TAG, "Loaded backup wallet string: " + walletStr);
                Log.d(TAG, "Backup wallet loaded");
            } catch (Exception e) {
                wallet = null;
                Log.e(TAG, "Load wallet parseJSON error: " + e.getMessage());
            } finally {
                inputStream.close();
            }
            return wallet;
        } catch (IOException e) {
            Log.e(TAG, "Load backup wallet file error: " + e.getMessage());
            return null;
        }
    }


    public static String getWalletPath(String dir) {
        return dir + "/" + WALLET_FILE_NAME;
    }

    public static boolean walletExists(Context context) {
        File walletFile = new File(getWalletPath(context.getFilesDir().getPath()));
        return walletFile.exists();
    }

    public static boolean backupExists() {
        File backupFile = new File(getWalletPath(getBackupDir().getPath()));
        return backupFile.exists();
    }

    public static boolean sdCardMountedExists() {
        String storageState = Environment.getExternalStorageState();

        if (TextUtils.isEmpty(storageState)) {
            return false;
        }
        return storageState.equals(Environment.MEDIA_MOUNTED);
    }

    private static File getBackupDir() {
        File sdCardDir = Environment.getExternalStorageDirectory();
        File backupDir = new File(sdCardDir, BACKUP_SDCARD_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
        return backupDir;
    }

    private static byte[] prepareKey(String password, String salt, int iterationCount) {
        HashKey hashKey = new HashKey(password, salt, iterationCount);
        byte[] key = mCachedKeys.get(hashKey);
        if (key != null && key.length > 0) {
            return key;
        }
        key = hashPassword(password.getBytes(), salt.getBytes(), iterationCount);
        mCachedKeys.put(hashKey, key);
        return key;
    }

    private static byte[] hashPassword(byte[] password, byte[] salt, int iterationCount) {
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
        gen.init(password, salt, iterationCount);
        return ((KeyParameter) gen.generateDerivedParameters(KEY_LENGTH)).getKey();
    }

    private static String encrypt(byte[] key, String value) {
        try {
            return Base64.encode(Vsys.aesEncrypt(key, value.getBytes()));
        } catch (Exception e) {
            Log.e(TAG, "AES encrypt failed: " + e.getMessage());
            return "";
        }
    }

    private static String decrypt(byte[] key, String encryptedValue) {
        try {
            return new String(Vsys.aesDecrypt(key, Base64.decode(encryptedValue)));
        } catch (Exception e) {
            Log.e(TAG, "Failed decrypt failed: " + e.getMessage());
            return "";
        }
    }


    public static void deleteWallet(Context context) {
        File walletFile = new File(getWalletPath(context.getFilesDir().getPath()));
        if (walletFile.exists()) {
            walletFile.delete();
        }
    }
}
