package com.gether.bigdata.thrift;

import com.gether.bigdata.redis.JedisService;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

public class WaterMarkClient {

	private Logger log = LoggerFactory.getLogger(WaterMarkClient.class);

	private GenericObjectPoolConfig thriftPoolConfig;

	private ObjectPool<TProtocol> pool;

	private String hostport;
	private int timeout;
	private JedisService jedisService;

	public void setJedisService(JedisService jedisService) {
		this.jedisService = jedisService;
	}

	public void setThriftPoolConfig(GenericObjectPoolConfig thriftPoolConfig) {
		this.thriftPoolConfig = thriftPoolConfig;
	}

	public void setHostport(String hostport) {
		this.hostport = hostport;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@PostConstruct
	public void WaterMarkClientInit() {
		//String[] hostports = hostport.split(":");
		//pool = new GenericObjectPool<TProtocol>(
		//		new TProtocolFactory(hostports[0], Integer.valueOf(hostports[1]), true, timeout), thriftPoolConfig);
	}

	public byte[] getWaterMarkByte(String shopName, int fontSize, String ttfPath) throws Exception {
		//TProtocol protocol = null;
		try {
			//protocol = null;
			List<String> resultList = Lists.newArrayList();
			String[] hostports = hostport.split(":");
			TTransport tTransport = new TFramedTransport(new TSocket(hostports[0], Integer.valueOf(hostports[1]), timeout));
			TBinaryProtocol protocol = new TBinaryProtocol(tTransport);
			tTransport.open();
			Watermark.Client client = new Watermark.Client(protocol);
			for (int i = 0; i < shopName.length(); i++) {
				//加入缓存判断//需要根据 字，字的大小，ttf路径或则相应的字体进行缓存
				//将字和字的大小组成一个 field
				long unicode = Long.valueOf(UnicodeUtils.str2UnicodeInt(shopName.charAt(i) + "") + "");
				String result = jedisService.hget(String.valueOf(unicode), String.valueOf(fontSize));
				if (StringUtils.isBlank(result)) {
					//if (null == protocol) {
					//	protocol = pool.borrowObject();
					//}
					//protocol = new TProtocolFactory(hostports[0], Integer.valueOf(hostports[1]), true, timeout);
					result = client.pdProcess(ttfPath, fontSize, unicode);
					if (StringUtils.isNotBlank(result) && !StringUtils.equalsIgnoreCase("create new face falied!", result)) {
						jedisService.hset(String.valueOf(unicode), String.valueOf(fontSize), result, null);
					}
				}
				resultList.add(result);
			}
			return ByteUtils.latticeByte(resultList, fontSize);
		} finally {
			//try {
			//	if (null != protocol) {
			//		pool.returnObject(protocol);
			//	}
			//} catch (Exception e) {
			//	log.error("thrift pool return error", e);
			//}
		}
	}
}