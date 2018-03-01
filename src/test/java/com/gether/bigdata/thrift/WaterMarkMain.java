package com.gether.bigdata.thrift;

import com.google.common.collect.Lists;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class WaterMarkMain {
	public static final String SERVER_IP = "localhost";
	public static final int SERVER_PORT = 9098;
	public static final int TIMEOUT = 30000;
	public static final int HASH_LENGTH = 32;

	@Test
	public void testBIO() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(200);
		poolConfig.setMinIdle(5);//最小空闲数
		poolConfig.setMaxIdle(10);//最大空闲数
		poolConfig.setBlockWhenExhausted(true);
		poolConfig.setMaxWaitMillis(2000);//最长等待时间
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		ObjectPool<TProtocol> pool = new GenericObjectPool<>(
				new TProtocolFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
		Watermark.Client client;
		TProtocol protocol = null;
		long startTime = System.currentTimeMillis();
		for (int j = 0; j <= 10000; j++) {
			try {
				Thread.sleep(1000);
				protocol = null;
				protocol = pool.borrowObject();
				System.out.println("active:" + pool.getNumActive());
				System.out.println(protocol.toString());
				long startTime2 = System.currentTimeMillis();
				client = new Watermark.Client(protocol);
				System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

				List<String> resultList = Lists.newArrayList();
				String name = "（";
				for (int i = 0; i < name.length(); i++) {
					long startTime1 = System.currentTimeMillis();
					int fontSize = 16;
					String result = client.pdProcess("/home/SourceHanSansK-Regular.ttf", fontSize, Long.valueOf(UnicodeUtils.str2UnicodeInt(name.charAt(i) + "") + ""));
					resultList.add(result);
					System.out.println("cost1:" + (System.currentTimeMillis() - startTime1));
					print(result, fontSize);
					//System.out.println(result.getBytes().length);
					System.out.println("Thrify client result =: " + result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != protocol) {
						pool.returnObject(protocol);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("cost:" + (System.currentTimeMillis() - startTime));
	}

	@Test
	public void testNIO() {
		long startTime = System.currentTimeMillis();
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(200);
		poolConfig.setMinIdle(5);//最小空闲数
		poolConfig.setMaxIdle(10);//最大空闲数
		poolConfig.setBlockWhenExhausted(true);
		poolConfig.setMaxWaitMillis(2000);//最长等待时间
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		ObjectPool<TProtocol> pool = new GenericObjectPool<>(
				new TProtocolFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
		Watermark.Client client;
		TProtocol protocol = null;
		for (int j = 0; j <= 10000; j++) {
			try {
				Thread.sleep(1000);
				long startTime2 = System.currentTimeMillis();
				protocol = null;
				protocol = pool.borrowObject();
				//System.out.println("active:" + pool.getNumActive());
				client = new Watermark.Client(protocol);
				//System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

				List<String> resultList = Lists.newArrayList();
				String name = "你好好呵呵好";
				for (int i = 0; i < name.length(); i++) {
					long startTime1 = System.currentTimeMillis();
					int fontSize = 1032;
					String unicode = UnicodeUtils.str2UnicodeInt(name.charAt(i) + "") + "";
					String result = client.pdProcess("/home/SourceHanSansK-Regular.ttf", fontSize, Long.valueOf(unicode));
					resultList.add(result);
					System.out.println("cost1:" + (System.currentTimeMillis() - startTime1));
					//System.out.println(result.getBytes().length);
					//System.out.println("Thrify client result =: " + result);
					//print(result, fontSize);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (TException e) {
				if (protocol != null) {
					protocol.getTransport().close();
				}
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != protocol) {
						pool.returnObject(protocol);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("cost:" + (System.currentTimeMillis() - startTime));
	}

	@Test
	public void testNIOWithTrasnportPoll() {
		long startTime = System.currentTimeMillis();
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(200);
		poolConfig.setMinIdle(5);//最小空闲数
		poolConfig.setMaxIdle(10);//最大空闲数
		poolConfig.setBlockWhenExhausted(true);
		poolConfig.setMaxWaitMillis(2000);//最长等待时间
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		ObjectPool<TTransport> pool = new GenericObjectPool<>(
				new TTransportFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
		Watermark.Client client;
		TTransport transport = null;
		for (int j = 0; j <= 1000; j++) {

			try {
				Thread.sleep(1000);
				long startTime2 = System.currentTimeMillis();
				transport = null;
				transport = pool.borrowObject();
				System.out.println("active:" + pool.getNumActive());
				System.out.println("cost3:" + (System.currentTimeMillis() - startTime2));
				TBinaryProtocol protocol = new TBinaryProtocol(transport);
				client = new Watermark.Client(protocol);
				System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

				List<String> resultList = Lists.newArrayList();
				String name = "你好好呵呵好";
				for (int i = 0; i < name.length(); i++) {
					long startTime1 = System.currentTimeMillis();
					int fontSize = 32;
					String unicode = UnicodeUtils.str2UnicodeInt(name.charAt(i) + "") + "";
					String result = client.pdProcess("/home/SourceHanSansK-Regular.ttf", fontSize, Long.valueOf(unicode));
					resultList.add(result);
					//System.out.println("cost1:" + (System.currentTimeMillis() - startTime1));
					//System.out.println(result.getBytes().length);
					//System.out.println("Thrify client result =: " + result);
					//print(result, fontSize);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (TException e) {
				if (transport != null) {
					transport.close();
				}
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != transport) {
						pool.returnObject(transport);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("cost:" + (System.currentTimeMillis() - startTime));
	}

	@Test
	public void testNIOWithClienPoll() {
		long startTime = System.currentTimeMillis();
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(200);
		poolConfig.setMinIdle(5);//最小空闲数
		poolConfig.setMaxIdle(10);//最大空闲数
		poolConfig.setBlockWhenExhausted(true);
		poolConfig.setMaxWaitMillis(2000);//最长等待时间
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		ObjectPool<Watermark.Client> pool = new GenericObjectPool<>(
				new TWatermarkClientFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
		Watermark.Client client = null;
		for (int j = 0; j <= 1000; j++) {

			try {
				Thread.sleep(1000);
				long startTime2 = System.currentTimeMillis();
				client = null;
				client = pool.borrowObject();
				System.out.println("active:" + pool.getNumActive());
				System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

				List<String> resultList = Lists.newArrayList();
				String name = "你好好呵呵好";
				for (int i = 0; i < name.length(); i++) {
					long startTime1 = System.currentTimeMillis();
					int fontSize = 32;
					String unicode = UnicodeUtils.str2UnicodeInt(name.charAt(i) + "") + "";
					String result = client.pdProcess("/home/SourceHanSansK-Regular.ttf", fontSize, Long.valueOf(unicode));
					resultList.add(result);
					//System.out.println("cost1:" + (System.currentTimeMillis() - startTime1));
					//System.out.println(result.getBytes().length);
					//System.out.println("Thrify client result =: " + result);
					//print(result, fontSize);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (TException e) {
				if (client != null) {
					TTransport t1 = client.getInputProtocol().getTransport();
					TTransport t2 = client.getOutputProtocol().getTransport();
					System.out.println(t1 == t2);
					client.getInputProtocol().getTransport().close();
					client.getOutputProtocol().getTransport().close();
				}
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != client) {
						pool.returnObject(client);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("cost:" + (System.currentTimeMillis() - startTime));
	}

	private static void print(String sb, int fontSize) {
		int count = 0;
		for (int j = 0; j < fontSize; j++) {
			for (int i = 0; i < fontSize; i++) {
				if (sb.charAt(count) == '0') {
					System.out.print("-");
				} else if (sb.charAt(count) == '1') {
					System.out.print("*");
				}
				count++;
			}
			System.out.println("");
		}
	}

	@Test
	public void getWatermark() throws IOException {
		FileOutputStream file = new FileOutputStream("/Users/myp/file.out", false);
		InputStream inputStream = loadFileFromURL("");
		OutputStream os = new FileOutputStream("/Users/myp/file.out", false);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		inputStream.close();
	}

	/**
	 * 作用：实现网络访问文件，将获取到数据储存在文件流中
	 *
	 * @param url ：访问网络的url地址
	 * @return inputstream
	 */
	public static InputStream loadFileFromURL(String url) {
		// 创建HttpClient对象：通过实例化DefaultHttpClient获得；
		HttpClient httpClient = new DefaultHttpClient();
		// 创建HttpGet或HttpPost对象：通过实例化 HttpGet或HttpPost
		// 获得，而构造方法的参数是urlstring（即需要访问的网络url地址）。也可以通过调用setParams()方法来添加请求参数；
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
			httppost.setEntity(uefEntity);
			// 调用HttpClient对象的execute()方法，参数是刚才创建的 HttpGet或HttpPost对象
			// ，返回值是HttpResponse对象；
			HttpResponse response = httpClient.execute(httppost);
			// 通过response对象中的getStatusLine()方法和getStatusCode()方法获取服务器响应状态。
			if (response.getStatusLine().getStatusCode() == 200) {
				// response对象的getEntity()方法，返回HttpEntity对象。该对象中包含了服务器页面body体之内的内容。
				HttpEntity entity = response.getEntity();
				// entity对象的getContent()方法将从服务器中获取到所有内容放到inputstream对象中。
				return entity.getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}