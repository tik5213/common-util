package cn.dxy.util.generictype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by tik on 17/6/28.
 * Gson对泛型的支持
 */

public class GenericTypesUtil {

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
