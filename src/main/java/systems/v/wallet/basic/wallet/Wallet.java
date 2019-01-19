package systems.v.wallet.basic.wallet;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.ArrayList;

import systems.v.vsys.Vsys;

public class Wallet {

    public static final String TEST_NET = Vsys.NetworkTestnet;
    public static final String MAIN_NET = Vsys.NetworkMainnet;

    private String seed = "";
    private long nonce = 0;
    private String agent;
    private String network = MAIN_NET;
    private String password;
    private String salt;

    @JSONField(serialize = false)
    private ArrayList<Account> accounts = new ArrayList<>();

    @JSONField(serialize = false)
    private ArrayList<Account> coldAccounts = new ArrayList<>();

    private static final String TAG = "Wallet";

    private static final SimplePropertyPreFilter JSON_FILTER = new SimplePropertyPreFilter("seed", "nonce", "network", "agent");

    public Wallet() {
    }

    public Wallet(String seed, String network, Agent agent) {
        this(seed, 0, network, agent);
    }

    public Wallet(String seed, long nonce, String network, Agent agent) {
        this.network = ensureNetwork(network);
        this.seed = seed;
        this.nonce = nonce;
        this.agent = agent.toString();
        Log.d(TAG, this.agent);
    }

    /**
     * only use for backup or save
     */
    public String toWalletStr() {
        return JSON.toJSONString(this, JSON_FILTER);
    }

    public void append(long num) {
        if (num > 0) {
            for (long i = nonce; i < nonce + num; i++) {
                systems.v.vsys.Wallet wallet = Vsys.newWallet(seed, network);
                Account account = new Account(seed, nonce, network, wallet.generateAccount(i));
                Log.d(TAG, account.toString());
                accounts.add(account);
            }
            nonce += num;
        } else {
            Log.d(TAG, "Invalid append");
        }
    }

    public ArrayList<Account> generateAccounts() {
        systems.v.vsys.Wallet wallet = Vsys.newWallet(seed, network);
        accounts.clear();
        for (long i = 0; i < nonce; i++) {
            Account account = new Account(seed, nonce, network, wallet.generateAccount(i));
            Log.d(TAG, account.toString());
            accounts.add(account);
        }
        return accounts;
    }

    /**
     * ensure network is MAIN_NET or TEST_NET
     */
    public static String ensureNetwork(String network) {
        if (MAIN_NET.equals(network) || TEST_NET.equals(network)) {
            return network;
        }
        return MAIN_NET;
    }

    public static String getAddress(String network, String publicKey) {
        return Vsys.newAccount(network, publicKey).address();
    }

    public static boolean validateAddress(String address) {
        return Vsys.validateAddress(address);
    }

    public static boolean validateSeedPhrase(String seed) {
        return Vsys.validatePhrase(seed);
    }

    /**
     * Generates a 15-word random seed. This method implements the BIP-39 algorithm with 160 bits of entropy.
     *
     * @return the seed as a String
     */
    public static String generateSeed() {
        return Vsys.generateSeed();
    }

    public Account getAccount(String publicKey) {
        for (Account account : accounts) {
            String pk = account.getPublicKey();
            if (pk.equals(publicKey)) {
                return account;
            }
        }
        for (Account account : coldAccounts) {
            String pk = account.getPublicKey();
            if (pk.equals(publicKey)) {
                return account;
            }
        }
        return null;
    }

    public Account getAccountByAddress(String address) {
        for (Account account : accounts) {
            String pk = account.getAddress();
            if (pk.equals(address)) {
                return account;
            }
        }
        for (Account account : coldAccounts) {
            String pk = account.getAddress();
            if (pk.equals(address)) {
                return account;
            }
        }
        return null;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public ArrayList<Account> getColdAccounts() {
        return coldAccounts;
    }

    public void setColdAccounts(ArrayList<Account> coldAccounts) {
        this.coldAccounts = coldAccounts;
    }
}
