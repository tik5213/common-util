package top.ftas.util.generictype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by tik on 17/6/28.
 * Gson对泛型的支持
 */

public class GenericTypesUtil {

    /**
// 另外一种 gson type 的传递方式
//		Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
//		ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
     */
	public static ParameterizedType type(final Class raw, final Type... genericArgs) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return genericArgs;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}

}
