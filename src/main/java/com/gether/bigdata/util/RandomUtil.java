package com.gether.bigdata.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 生成随机验证码工具类.
 * 
 * @author hq6830
 * 
 */
public class RandomUtil {

	public static final String RANDAMGEN = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String KEYGEN = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String NUMBERONLY_GEN = "0123456789";


	/**
	 * 根据位数生成验证码.
	 *
	 * @param length    位数
	 * @param speString 验证码生成的字串
	 * @return
	 */
	public static String randomGen(int length, String speString) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(RandomStringUtils.random(1, speString));
		}
		return sb.toString();
	}
}