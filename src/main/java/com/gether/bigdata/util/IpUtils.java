package com.gether.bigdata.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

	private static final ThreadLocal<String> currentIp = new ThreadLocal<String>();

	public static String getIp() {
		if (null != currentIp) {
			return currentIp.get();
		}
		return "";
	}

	public static void setIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		currentIp.set(ip);
	}
}