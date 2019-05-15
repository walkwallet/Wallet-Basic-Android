package systems.v.wallet.basic.wallet;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import systems.v.wallet.basic.utils.Base58;
import systems.v.wallet.basic.utils.TxUtil;
import vsys.Contract;
import vsys.Vsys;

public class Transaction {
    public static final String TAG = "Transaction";

    public static final int PAYMENT = (int) Vsys.TxTypePayment;
    public static final int LEASE = (int) Vsys.TxTypeLease;
    public static final int CANCEL_LEASE = (int) Vsys.TxTypeCancelLease;
    public static final int MINTING = (int) Vsys.TxTypeMining;
    public static final int CONTRACT_REGISTER = (int) Vsys.TxTypeContractRegister;
    public static final int CONTRACT_EXECUTE = (int) Vsys.TxTypeContractExecute;

    public static final long DEFAULT_FEE = Vsys.DefaultTxFee;
    public static final short DEFAULT_FEE_SCALE = Vsys.DefaultFeeScale;
    public static final long DEFAULT_CREATE_TOKEN_FEE = 100 * Vsys.VSYS;
    public static final long DEFAULT_TOKEN_TX_FEE = 3 * Vsys.DefaultTxFee;

    private int transactionType;
    private String senderPublicKey;
    private String address;

    private String recipient;
    private long amount;
    private long fee = DEFAULT_FEE;
    private short feeScale = DEFAULT_FEE_SCALE;
    private long timestamp;
    private String attachment = "";
    private String txId;
    private String signature;
    private Contract contractObj;
    private String actionCode; //functionId may change, use actionCode to mark specific execution

    private String description;
    private String contract;
    private String contractId;
    private String contractInit;
    private String contractInitTextual;
    private String contractInitExplain;

    private short  functionId;
    private String function;
    private String functionTextual;
    private String functionExplain;

    public Transaction() {
    }

    public static boolean validate(int type) {
        return type == PAYMENT || type == LEASE || type == CANCEL_LEASE ||
                type == CONTRACT_REGISTER || type == CONTRACT_EXECUTE;
    }

    public void sign(Account sender) {
        vsys.Transaction tx = null;
        Log.d(TAG, JSON.toJSONString(this));
        switch (transactionType) {
            case PAYMENT: {
                tx = Vsys.newPaymentTransaction(recipient, amount);
                if (attachment != null) {
                    tx.setAttachment(attachment.getBytes());
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
            case CONTRACT_REGISTER: {
                tx = Vsys.newRegisterTransaction(Base58.decode(contract), Base58.decode(contractInit), description);
            }
            break;
            case CONTRACT_EXECUTE: {
                tx = Vsys.newExecuteTransaction(contractId, Base58.decode(function), functionId,  attachment);
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
                map.put("attachment", TxUtil.encodeAttachment(attachment));
                break;
            case LEASE:
                map.put("recipient", recipient);
                map.put("amount", amount);
                break;
            case CANCEL_LEASE:
                map.put("recipient", recipient);
                map.put("txId", txId);
                break;
            case CONTRACT_REGISTER:
                map.put("contract", contract);
                map.put("initData", contractInit);
                map.put("description", description);
                break;
            case CONTRACT_EXECUTE:
                map.put("contractId", contractId);
                map.put("functionIndex", functionId);
                map.put("functionData",  function);
                map.put("attachment", TxUtil.encodeAttachment(attachment));
                break;
        }
        map.put("signature", signature);
        return map;
    }

    // for cold wallet scan
    public String toTxString() {
        Operation op = new Operation(Operation.TRANSACTION);
        op.put("senderPublicKey", senderPublicKey);
        op.put("transactionType", transactionType);
        op.put("fee", fee);
        op.put("feeScale", feeScale);
        op.put("timestamp", timestamp);
        switch (transactionType) {
            case PAYMENT:
                op.put("recipient", recipient);
                op.put("amount", amount);
                op.put("attachment", attachment);
                break;
            case LEASE:
                op.put("recipient", recipient);
                op.put("amount", amount);
                break;
            case CANCEL_LEASE:
                op.put("recipient", recipient);
                op.put("txId", txId);
                break;
            case CONTRACT_REGISTER:
                op.put("contract", contract);
                op.put("description", attachment);
                op.put("contractInit", contractInit);
                op.put("contractInitTextual", contractInitTextual);
                op.put("contractInitExplain", contractInitExplain);
                op.put("address", address);
                op.setOpc(Operation.CONTRACT);
                break;
            case CONTRACT_EXECUTE:
                op.put("attachment", TxUtil.encodeAttachment(attachment));
                op.put("contractId", contractId);
                op.put("functionId", functionId);
                op.put("function", function);
                op.put("functionTextual", functionTextual);
                op.put("functionExplain", functionExplain);
                op.setOpc(Operation.FUNCTION);
                break;
        }

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

    public short getFunctionId() {
        return functionId;
    }

    public void setFunctionId(short functionId) {
        this.functionId = functionId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getContractInit() {
        return contractInit;
    }

    public void setContractInit(String contractInit) {
        this.contractInit = contractInit;
    }

    public String getContractInitTextual() {
        return contractInitTextual;
    }

    public void setContractInitTextual(String contractInitTextual) {
        this.contractInitTextual = contractInitTextual;
    }

    public String getContractInitExplain() {
        return contractInitExplain;
    }

    public void setContractInitExplain(String contractInitExplain) {
        this.contractInitExplain = contractInitExplain;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunctionTextual() {
        return functionTextual;
    }

    public void setFunctionTextual(String functionTextual) {
        this.functionTextual = functionTextual;
    }

    public String getFunctionExplain() {
        return functionExplain;
    }

    public void setFunctionExplain(String functionExplain) {
        this.functionExplain = functionExplain;
    }

    public Contract getContractObj() {
        return contractObj;
    }

    public void setContractObj(Contract contractObj) {
        this.contractObj = contractObj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
