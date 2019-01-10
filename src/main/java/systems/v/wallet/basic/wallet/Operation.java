package systems.v.wallet.basic.wallet;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import systems.v.vsys.Vsys;

public class Operation extends HashMap<String, Object> {

    public static final String ACCOUNT = Vsys.OpcTypeAccount;
    public static final String SIGNATURE = Vsys.OpcTypeSignature;
    public static final String SEED = Vsys.OpcTypeSeed;
    public static final String TRANSACTION = Vsys.OpcTypeTransction;

    private static final List<String> OPCS = Collections.unmodifiableList(Arrays.asList(
            ACCOUNT, SIGNATURE, SEED, TRANSACTION));

    private static final String KEY_PROTOCOL = "protocol";
    private static final String KEY_API = "api";
    private static final String KEY_OPC = "opc";

    public Operation() {
        setProtocol(Vsys.Protocol);
        setApi((int) Vsys.Api);
    }

    public Operation(String opc) {
        this();
        setOpc(opc);
    }

    public Operation(Map<String, Object> map) {
        this();
        putAll(map);
    }

    public boolean validate() {
        String opc = getOpc();
        if (TextUtils.isEmpty(opc)) {
            return false;
        }
        return OPCS.contains(opc);
    }

    public boolean validate(String opc) {
        return validate() && getOpc().equals(opc);
    }

    public String getString(String key) {
        Object o = get(key);
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    public long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public void set(String key, Object value) {
        put(key, value);
    }

    public String toStr() {
        return JSON.toJSONString(this);
    }

    public static Operation parse(String str) {
        try {
            return JSON.parseObject(str, Operation.class);
        } catch (Exception e) {

        }
        return null;
    }

    public String getProtocol() {
        return getString(KEY_PROTOCOL);
    }

    public void setProtocol(String protocol) {
        put(KEY_PROTOCOL, protocol);
    }

    public int getApi() {
        return getInt(KEY_API);
    }

    public void setApi(int api) {
        put(KEY_API, api);
    }

    public String getOpc() {
        return getString(KEY_OPC);
    }

    public void setOpc(String opc) {
        put(KEY_OPC, opc);
    }
}
