package systems.v.wallet.basic.wallet;

import java.util.HashMap;
import java.util.Map;

import vsys.Vsys;


public class Contract {
    public static final long DEFAULT_FEE = Vsys.DefaultTxFee;
    public static final short DEFAULT_FEE_SCALE = Vsys.DefaultFeeScale;

    private int contractActionType;
    private String contract;
    private String senderPublicKey;
    private long fee = DEFAULT_FEE;
    private short feeScale = DEFAULT_FEE_SCALE;
    private long timestamp;
    private String data;
    private String description;
    private String signature;

    private String contractId;
    private int funcIdx;

    public Map<String, Object> toRequestBody() {
        Map<String, Object> map = new HashMap<>();
        map.put("senderPublicKey", senderPublicKey);
        map.put("fee", fee);
        map.put("feeScale", feeScale);
        map.put("timestamp", timestamp * 1000000);
//        switch (contractActionType) {
//            case PAYMENT:
//                map.put("recipient", recipient);
//                map.put("amount", amount);
//                map.put("attachment", attachment);
//                break;
//            case LEASE:
//                map.put("recipient", recipient);
//                map.put("amount", amount);
//                break;
//            case CANCEL_LEASE:
//                map.put("recipient", recipient);
//                map.put("txId", txId);
//                break;
//        }
        map.put("signature", signature);
        return map;
    }
}
