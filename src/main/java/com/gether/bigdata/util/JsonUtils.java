package com.gether.bigdata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

	public static String toJsonStrWithoutNull(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static String toJsonStrWithoutNull(Object obj,
			PropertyFilter excludePf) {
		return JSON.toJSONString(obj, excludePf);
	}

	public static String toJsonStrWithNull(Object obj) {
		return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty);
	}

	public static String toJsonStrWithNull(Object obj, PropertyFilter excludePf) {
		return JSON.toJSONString(obj, excludePf,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty);
	}

	public static String toJsonStrWithNullDateFormat(Object obj,
			String dateformate) {
		return JSON.toJSONStringWithDateFormat(obj, dateformate,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty);
	}
}
