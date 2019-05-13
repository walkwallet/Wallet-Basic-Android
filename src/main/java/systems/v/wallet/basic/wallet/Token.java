package systems.v.wallet.basic.wallet;

import java.util.HashMap;
import java.util.Map;

public class Token {
    private String Icon;
    private String Name;
    private String tokenId;
    private long   Balance;
    private long   unity;
    private long   max;
    private ContractFunc[] funcs;
    private String issuer;
    private String registerTime;
    private long   issuedAmount;
    private String Description;

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getBalance() {
        return Balance;
    }

    public void setBalance(long balance) {
        Balance = balance;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public long getUnity() {
        return unity;
    }

    public void setUnity(long unity) {
        this.unity = unity;
    }

    public ContractFunc[] getFuncs() {
        return funcs;
    }

    public void setFuncs(ContractFunc[] funcs) {
        this.funcs = funcs;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public long getIssuedAmount() {
        return issuedAmount;
    }

    public void setIssuedAmount(long issuedAmount) {
        this.issuedAmount = issuedAmount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}