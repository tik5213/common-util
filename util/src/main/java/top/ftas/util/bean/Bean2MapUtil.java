package top.ftas.util.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-25 20:04
 */
public class Bean2MapUtil {

    /**
     * 将一个JavaBean转换成Map对象
     * TODO 有时间可以提升下效率
     * https://blog.csdn.net/q358543781/article/details/50176953
     */
    public static Map<String,String> bean2Map(Object obj){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(obj);
        Type mapType = new TypeToken<HashMap<String, String>>() {
        }.getType();
        Map<String, String> paramMap = gson.fromJson(jsonStr, mapType);
        return paramMap;
    }
}
