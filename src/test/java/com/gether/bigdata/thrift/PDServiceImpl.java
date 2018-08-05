package com.gether.bigdata.thrift;//package com.gether.bigdata;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//import net.sf.json.JSONObject;
//import org.apache.thrift.TException;
//import sun.misc.BASE64Decoder;
//
///**
// * pdService 模拟实现
// */
//public class PDServiceImpl implements PDService.Iface {
//
//	@Override
//	public String pdProcess(String paramJson) throws TException {
//		System.out.println("pdProcess start");
//		System.out.println("params = " + paramJson);
//
//		try {
//			JSONObject json = JSONObject.fromObject(paramJson);
//			String imageStr = json.getString("pdFile");
//			generateImage(imageStr);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("pdProcess return");
//		System.out.println("-----------------------------------------------------------");
//		return "ok";
//	}
//
//	/**
//	 * 对str进行解码
//	 *
//	 * @param imageStr
//	 */
//	public void generateImage(String imageStr) {
//		if (imageStr == null) {
//			return;
//		}
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
////			base64解码
////			int num = 0;
//			byte[] bytes = decoder.decodeBuffer(imageStr);
//			for (int i = 0; i < bytes.length; i++) {
//				if (bytes[i] < 0) {//调整异常数据
//					bytes[i] += 256;
////					num++;
//				}
//			}
////			System.out.println("num = " + num);
//			writeFile(bytes);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 生成jpg图片
//	 *
//	 * @param bytes
//	 */
//	public void writeFile(byte[] bytes) {
//		try {
//			OutputStream fos = new FileOutputStream(new File("1.jpg"));
//			fos.write(bytes);
//			fos.flush();
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
