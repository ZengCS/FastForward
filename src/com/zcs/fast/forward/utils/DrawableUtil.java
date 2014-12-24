package com.zcs.fast.forward.utils;

import java.lang.reflect.Field;

public class DrawableUtil {
	/**
	 * 根据资源名称获取资源ID
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static int getDrawableIdByName(String name) throws Exception {
		Field field = Class.forName("com.zcs.fast.forward.R$drawable").getField(name);
		return field.getInt(field);
	}
}
