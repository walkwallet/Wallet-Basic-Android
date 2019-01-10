package systems.v.wallet.basic.wallet;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import systems.v.vsys.Vsys;
import systems.v.wallet.basic.utils.TxUtil;

public class Transaction {
    public static final String TAG = "Transaction";

    public static final int PAYMENT = (int) Vsys.TxTypePayment;
    public static final int LEASE = (int) Vsys.TxTypeLease;
    public static final int CANCEL_LEASE = (int) Vsys.TxTypeCancelLease;
    public static final int MINTING = 5;
    public static final long DEFAULT_FEE = Vsys.DefaultTxFee;
    public static final short DEFAULT_FEE_SCALE = Vsys.DefaultFeeScale;

    private int transactionType;
    private String senderPublicKey;
    private String recipient;
    private long amount;
    private long fee = DEFAULT_FEE;
    private short feeScale = DEFAULT_FEE_SCALE;
    private long timestamp;
    private String attachment;
    private String txId;
    private String signature;

    public Transaction() {
    }

    public static boolean validate(int type) {
        return type == PAYMENT || type == LEASE || type == CANCEL_LEASE;
    }

    public void sign(Account sender) {
        systems.v.vsys.Transaction tx = null;
        switch (transactionType) {
            case PAYMENT: {
                tx = Vsys.newPaymentTransaction(recipient, amount);
                if (attachment != null) {
                    tx.setAttachment(TxUtil.decodeAttachment(attachment).getBytes());
                }
            }
            break;
            case LEASE: {
                tx = Vsys.newLeaseTransaction(recipient, amount);
            }
            break;
            case CANCEL_LEASE: {
                tx = Vsys.newCancelLeaseTransaction(txId);
            }
            break;
        }
        if (tx != null) {
            tx.setFee(fee);
            tx.setFeeScale(feeScale);
            tx.setTimestamp(timestamp * 1000000);
            signature = sender.getSignature(tx.buildTxData());
        }
    }

    public Map<String, Object> toRequestBody() {
        Map<String, Object> map = new HashMap<>();
        map.put("senderPublicKey", senderPublicKey);
        map.put("transactionType", transactionType);
        map.put("fee", fee);
        map.put("feeScale", feeScale);
        map.put("timestamp", timestamp * 1000000);
        switch (transactionType) {
            case PAYMENT:
                map.put("recipient", recipient);
                map.put("amount", amount);
                map.put("attachment", attachment);
                break;
            case LEASE:
                map.put("recipient", recipient);
                map.put("amount", amount);
                break;
            case CANCEL_LEASE:
                map.put("recipient", recipient);
                map.put("txId", txId);
                break;
        }
        map.put("signature", signature);
        return map;
    }

    // for cold wallet scan
    public String toTxString() {
        Map<String, Object> map = new HashMap<>();
        map.put("senderPublicKey", senderPublicKey);
        map.put("transactionType", transactionType);
        map.put("fee", fee);
        map.put("feeScale", feeScale);
        map.put("timestamp", timestamp);
        switch (transactionType) {
            case PAYMENT:
                map.put("recipient", recipient);
                map.put("amount", amount);
                map.put("attachment", attachment);
                break;
            case LEASE:
                map.put("recipient", recipient);
                map.put("amount", amount);
                break;
            case CANCEL_LEASE:
                map.put("recipient", recipient);
                map.put("txId", txId);
                break;
        }
        Operation op = new Operation(map);
        op.setOpc(Operation.TRANSACTION);
        return JSON.toJSONString(op);
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int type) {
        this.transactionType = type;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public short getFeeScale() {
        return feeScale;
    }

    public void setFeeScale(short feeScale) {
        this.feeScale = feeScale;
    }

    public long getTimestamp() {
        if (String.valueOf(timestamp).length() == 19) {
            return timestamp / 1000000;
        }
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        if (String.valueOf(timestamp).length() == 19) {
            this.timestamp = timestamp / 1000000;
        } else {
            this.timestamp = timestamp;
        }
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
