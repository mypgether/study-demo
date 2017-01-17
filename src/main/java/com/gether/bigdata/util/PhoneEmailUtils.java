package com.gether.bigdata.util;

import org.apache.commons.lang.StringUtils;

public class PhoneEmailUtils {
	private static final String PHONE_REGEX = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
	private static final String EMAIL_REGEX = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

	public static boolean isPhone(String phone) {
		if (StringUtils.isBlank(phone))
			return false;
		return phone.matches(PHONE_REGEX);
	}

	public static boolean isEmail(String email) {
		if (StringUtils.isBlank(email))
			return false;
		return email.matches(EMAIL_REGEX);
	}
}
