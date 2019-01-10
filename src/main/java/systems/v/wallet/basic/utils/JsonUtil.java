package systems.v.wallet.basic.utils;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

    public static boolean isJsonString(String str) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
