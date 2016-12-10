package com.gether.bigdata.thrift;//package com.gether.bigdata;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import net.sf.json.JSONObject;
//import org.apache.log4j.Logger;
//import org.apache.thrift.TException;
//import org.apache.thrift.async.AsyncMethodCallback;
//import org.apache.thrift.async.TAsyncClientManager;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TCompactProtocol;
//import org.apache.thrift.protocol.TProtocol;
//import org.apache.thrift.protocol.TProtocolFactory;
//import org.apache.thrift.transport.TFramedTransport;
//import org.apache.thrift.transport.TNonblockingSocket;
//import org.apache.thrift.transport.TNonblockingTransport;
//import org.apache.thrift.transport.TSocket;
//import org.apache.thrift.transport.TTransport;
//import org.junit.Test;
//import sun.misc.BASE64Encoder;
//
///**
// *
// * Created by qmd2750 on 2016/11/28.
// */
//
//public class PDClientTest {
//	private static final Logger logger = Logger.getLogger(PDClientTest.class);
//	//	private static final String PD_IP = "172.29.30.237";
//	private static final String PD_IP = "127.0.0.1";
//	private static final int PD_PORT = PDServerTest.PD_PORT;
//	private static final int TIMEOUT = 3000;
//	private static final PDServerTest.ServerType serverType = PDServerTest.serverType;
//
//	private String filePath = "G:\\001.jpg";
//	private double hit_threshold = 1.1;
//	private String paramJson = paramJson(filePath, hit_threshold);
//
//	public static void main(String[] args) {
//		for (int i = 0; i < 1; i++) {
//			Thread t = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					PDClientTest client = new PDClientTest();
//					switch (serverType) {
//						case SIMPLE:
//						case TTHREADPOOL:
//							client.startSimpleClient();
//							break;
//						case TNONBLOCKING:
//						case THSHA:
//						case TTSELECTOR:
//							//非阻塞式I/O
//							client.startTNoBlockingClient();
//							//异步式
////				client.startAsynClient();
//							break;
//						default:
//							break;
//					}
//				}
//			});
//			t.setName("i" + i);
//			t.start();
//			System.out.println(i);
//		}
//
//	}
//
//	/**
//	 * 开启简单处理的客户端
//	 */
//	public void startSimpleClient() {
//		TTransport transport = null;
//		try {
//			transport = new TSocket(PD_IP, PD_PORT, TIMEOUT);
//			TProtocol protocol = new TBinaryProtocol(transport);
//			PDService.Client client = new PDService.Client(protocol);
//			transport.open();
//
//			String result = client.pdProcess(filePath);
//			System.out.println("result = " + result);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("get Client exception ", e);
//		} finally {
//			if (null != transport) {
//				transport.close();
//			}
//		}
//	}
//
//	/**
//	 * 使用非阻塞式I/O，服务端和客户端需要指定TFrameTransport
//	 */
//	public void startTNoBlockingClient() {
//		TTransport transport = null;
//		try {
//			//需要指定TFrameTransport
//			transport = new TFramedTransport(new TSocket(PD_IP, PD_PORT));
//			// 协议要和服务端一致
//			TProtocol protocol = new TBinaryProtocol(transport);
//			PDService.Client client = new PDService.Client(protocol);
//			transport.open();
////			while (true) {
//				long startTime = System.currentTimeMillis();
//				String result = client.pdProcess(paramJson.toString());
//				System.out.println(String.format("Thrify client result = %s, startTime = %s, duration = %s",
//						result, startTime, (System.currentTimeMillis() - startTime)));
//				try {
//					Thread.sleep(10);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (null != transport) {
//				transport.close();
//			}
//		}
//	}
//
//	/**
//	 * 异步客户端
//	 */
//	public void startAsynClient() {
//		try {
//			TAsyncClientManager clientManager = new TAsyncClientManager();
//			TNonblockingTransport transport = new TNonblockingSocket(PD_IP,
//					PD_PORT, TIMEOUT);
//
//			TProtocolFactory tprotocol = new TBinaryProtocol.Factory();
//			PDService.AsyncClient asyncClient = new PDService.AsyncClient(
//					tprotocol, clientManager, transport);
//			System.out.println("Client start .....");
//
//			CountDownLatch latch = new CountDownLatch(1);
//			AsynCallback callBack = new AsynCallback(latch);
//			System.out.println("call method pdProcess start ...");
//			asyncClient.pdProcess(filePath, callBack);
//			System.out.println("call method pdProcess .... end");
//			boolean wait = latch.await(4, TimeUnit.SECONDS);
//			System.out.println("latch.await " + wait);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("startClient end.");
//	}
//
//	/**
//	 * 生成json参数
//	 * @param file
//	 * @param hit_threshold
//	 * @return
//	 */
//	private String paramJson(String file, double hit_threshold) {
//		JSONObject paramJson = new JSONObject();
//		paramJson.put("pd_file", getImageStr(file));
//		paramJson.put("hit_threshold", hit_threshold);
//		System.out.println(paramJson.toString());
//		return paramJson.toString();
//	}
//
//	/**
//	 * base64加密
//	 * @param filePath
//	 * @return
//	 */
//	public String getImageStr(String filePath) {
//		byte[] bytes = getFileBytes(filePath);
//		String encodeStr = "";
//		BASE64Encoder encoder = new BASE64Encoder();
//		try {
//			encodeStr = encoder.encode(bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return encodeStr;
//	}
//
//	/**
//	 * 加载本地文件转换成二进制数组
//	 * @return
//	 */
//	private byte[] getFileBytes(String fileName) {
//		File file = new File(fileName);
//		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//		FileInputStream fis = null;
//		byte[] data = null;
//		try {
//			 fis = new FileInputStream(file);
//			byte[] buffer = new byte[1024];
//			int len = -1;
//			while ((len = fis.read(buffer)) != -1) {
//				byteArrayOut.write(buffer, 0, len);
//			}
//			data = byteArrayOut.toByteArray() ;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (null != byteArrayOut) {
//					byteArrayOut.close();
//				}
//			} catch (IOException e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		return data;
//	}
//
//}
//
///**
// * 异常信息异步回调
// */
//class AsynCallback implements AsyncMethodCallback<PDService.AsyncClient.pdProcess_call> {
//	private CountDownLatch latch;
//
//	public AsynCallback(CountDownLatch latch) {
//		this.latch = latch;
//	}
//
//	/**
//	 * 处理完成，返回结果
//	 *
//	 * @param pdProcess_call
//	 */
//	@Override
//	public void onComplete(PDService.AsyncClient.pdProcess_call pdProcess_call) {
//		System.out.println("complete");
//		try {
//			System.out.println(pdProcess_call.getResult().toString());
//		} catch (TException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 返回异常
//	 *
//	 * @param exception
//	 */
//	@Override
//	public void onError(Exception exception) {
//		System.out.println("onError :" + exception.getMessage());
//		latch.countDown();
//	}
//}
