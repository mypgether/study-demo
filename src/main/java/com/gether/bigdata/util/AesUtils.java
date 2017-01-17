package com.gether.bigdata.util;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AesUtils {
  private static Logger log = LoggerFactory.getLogger(AesUtils.class);

  private static final String IV_PARAM = "dda639ffb96d8724";
  private static final String PASSWORD = "1290c17708b0351c";

  /**
   * AES加密字符串
   *
   * @param content  需要被加密的字符串
   * @param password 加密需要的密码
   * @return 密文
   */
  public static String encrypt(String content) {
    Cipher cipher;
    try {
      cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      SecretKeySpec key = new SecretKeySpec(PASSWORD.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV_PARAM.getBytes("UTF-8")));
      return Base64.encodeBase64String(cipher.doFinal(content.getBytes("UTF-8")));
    } catch (Exception e) {
      log.error("AesUtils encrypt error", e);
    }
    return null;
  }

  /**
   * 解密AES加密过的字符串
   *
   * @param encrypted AES加密过过的内容
   * @param password  加密时的密码
   * @return 明文
   */
  public static String decrypt(String encrypted) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      SecretKeySpec key = new SecretKeySpec(PASSWORD.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV_PARAM.getBytes("UTF-8")));
      byte[] result = cipher.doFinal(Base64.decodeBase64(encrypted));
      return new String(result, Charset.forName("UTF-8")); // 明文
    } catch (Exception e) {
      log.error("AesUtils decrypt error", e);
    }
    return null;
  }
}