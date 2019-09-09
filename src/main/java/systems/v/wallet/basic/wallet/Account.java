package systems.v.wallet.basic.wallet;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Account implements AccountBalance {
    private long nonce;
    private String alias;
    private String network;
    private String seed;
    private String address;
    private String publicKey;
    private List<Token> watchedTokens;

    @JSONField(serialize = false)
    private vsys.Account account;

    // balance info
    private long regular; // total balance
    private long mintingAverage;
    private long available;
    private long effective;
    private int height;

    public Account() {
    }

    public Account(String seed, long nonce, String network, vsys.Account account,String alias ) {
        this.alias = alias;
        this.seed = seed;
        this.nonce = nonce;
        this.network = network;
        this.account = account;
        this.address = account.address();
        this.publicKey = account.publicKey();
    }

    public void updateBalance(AccountBalance balance) {
        this.regular = balance.getRegular();
        this.mintingAverage = balance.getMintingAverage();
        this.available = balance.getAvailable();
        this.effective = balance.getEffective();
        this.height = balance.getHeight();
    }

    public String getAccountSeed() {
        if (account == null) {
            return "";
        }
        return account.accountSeed();
    }

    public String getPrivateKey() {
        if (account == null) {
            return "";
        }
        return account.privateKey();
    }

    public String getSignature(byte[] data) {
        if (account == null) {
            return "";
        }
        return account.signData(data);
    }

    public boolean isColdAccount() {
        return account == null;
    }

    @Override
    public String toString() {
        return "Nonce: " + nonce + "\nAccount seed: " + getAccountSeed() + "\nPrivate Key: "
                + getPrivateKey() + "\nPublic Key: " + getPublicKey() + "\nAddress: " + getAddress();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public vsys.Account getAccount() {
        return account;
    }

    public void setAccount(vsys.Account account) {
        this.account = account;
    }

    public long getRegular() {
        return regular;
    }

    public void setRegular(long regular) {
        this.regular = regular;
    }

    public long getMintingAverage() {
        return mintingAverage;
    }

    public void setMintingAverage(long mintingAverage) {
        this.mintingAverage = mintingAverage;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    public long getEffective() {
        return effective;
    }

    public void setEffective(long effective) {
        this.effective = effective;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public List<Token> getWatchedTokens() {
        return watchedTokens;
    }

    public void setWatchedTokens(List<Token> watchedTokens) {
        this.watchedTokens = watchedTokens;
    }
}
