package com.gether.bigdata.thrift;

import java.util.List;

public class ByteUtils {

  public static byte[] latticeByte(List<String> listString, int fontSize) {
    StringBuffer sb = new StringBuffer();
    for (int j = 0; j < fontSize; j++) {
      int length = sb.length();//
      for (int i = 0; i < listString.size(); i++) {
        sb.append(listString.get(i).substring(fontSize * j, fontSize * (j + 1)));
      }
      //System.out.println("");
      //for(int i =length;i<sb.length();i++) {
      //	if(sb.charAt(i)=='0') {
      //		System.out.print("-");
      //	}else{
      //		System.out.print("*");
      //	}
      //}
    }
    //将二进制字符串截取成字节
    int byteLength = sb.length() / 8;
    int byteY = sb.length() % 8;
    if (byteY != 0) {
      byteLength += 1;
    }
    byteLength += 36;
    byte[] byteArray = new byte[byteLength];
    //加入两个字节的头信息
    short wShort = Short.valueOf(listString.size() * fontSize + "");
    short hShort = Short.valueOf(fontSize + "");
    for (int i = 32; i < 34; i++) {
      byteArray[i] = new Integer(wShort & 0xff).byteValue();// 将最低位保存在最低位
      wShort = (short) (wShort >> 8); // 向右移8位
    }
    for (int i = 34; i < 36; i++) {
      byteArray[i] = new Integer(hShort & 0xff).byteValue();// 将最低位保存在最低位
      hShort = (short) (hShort >> 8); // 向右移8位
    }
    for (int k = 36; k < byteArray.length - 1; k++) {
      byteArray[k] = bit2byte(sb.substring((k - 36) * 8, (k - 35) * 8));
    }
    int lastByte = byteArray.length - 1;
    if (byteY == 0) {
      byteArray[lastByte] = bit2byte(sb.substring((lastByte - 36) * 8, (lastByte - 35) * 8));
    } else {
      String s = sb.substring((lastByte - 36) * 8, sb.length());
      byteArray[lastByte] = bit2byte(s + String.format("%1$0" + (8 - s.length()) + "d", 0));
    }
    return byteArray;
  }

  public static byte bit2byte(String bString) {
    byte result = 0;
    for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
      result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
    }
    return result;
  }
}