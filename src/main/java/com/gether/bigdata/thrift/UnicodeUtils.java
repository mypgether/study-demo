package com.gether.bigdata.thrift;

public class UnicodeUtils {
	
	public static int str2UnicodeInt(String str) {
		str = (str == null ? "" : str); 
		String tmp; 
		StringBuffer sb = new StringBuffer(1000); 
		char c; 
		int j; 
		sb.setLength(0); 
		c = str.charAt(0); 
		j = (c >>>8); //取出高8位 
		tmp = Integer.toHexString(j); 
		if (tmp.length() == 1) 
			sb.append("0"); 
		sb.append(tmp); 
		j = (c & 0xFF); //取出低8位 
		tmp = Integer.toHexString(j); 
		if (tmp.length() == 1) 
			sb.append("0"); 
		sb.append(tmp); 
		return Integer.parseInt(new String(sb),16);
	}
}
