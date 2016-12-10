package com.gether.bigdata.thrift;//package com.gether.bigdata;
//
//import org.apache.thrift.TProcessor;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TCompactProtocol;
//import org.apache.thrift.protocol.TProtocolFactory;
//import org.apache.thrift.server.THsHaServer;
//import org.apache.thrift.server.TNonblockingServer;
//import org.apache.thrift.server.TServer;
//import org.apache.thrift.server.TSimpleServer;
//import org.apache.thrift.server.TThreadPoolServer;
//import org.apache.thrift.server.TThreadedSelectorServer;
//import org.apache.thrift.transport.TFramedTransport;
//import org.apache.thrift.transport.TNonblockingServerSocket;
//import org.apache.thrift.transport.TServerSocket;
//import org.apache.thrift.transport.TTransportException;
//
///**
// * 采用TThreadedSelectorServer方式
// * Created by qmd2750 on 2016/11/28.
// */
//public class PDServerTest {
//	public static final int PD_PORT = 9090;
//	public static final ServerType serverType = ServerType.TNONBLOCKING;
//
//	enum ServerType {
//		SIMPLE, TNONBLOCKING, TTHREADPOOL, THSHA, TTSELECTOR
//	}
//
//	public static void main(String[] args) {
//		PDServerTest pdServer = new PDServerTest();
//
//		//采用二进制协议
//		TProtocolFactory factory = new TBinaryProtocol.Factory();
//		//压缩编码格式协议
////		TProtocolFactory factory = new TCompactProtocol.Factory();
//
//		switch (serverType) {
//			case SIMPLE: //简单的
//				System.out.println("Hello TSimpleServer...");
//				pdServer.startSimpleServer(factory);
//				break;
//			case TNONBLOCKING: //非阻塞式I/O
//				System.out.println("Hello TNOServer...");
//				pdServer.startTNonblockingServer(factory);
//				break;
//			case TTHREADPOOL: //线程池服务阻塞I/O
//				System.out.println("Hello TTHREADPOOLServer");
//				pdServer.startTTreadPoolServer(factory);
//				break;
//			case THSHA: //半同步半异步的服务端模型
//				System.out.println("Hello THsHaServer...");
//				pdServer.startTHsHaServer(factory);
//				break;
//			case TTSELECTOR: //多线程半同步半异步服务模型
//				System.out.println("Hello TThreadedSelectorServer...");
//				pdServer.startTThreadSelectorServer(factory);
//				break;
//			default:
//				break;
//		}
//	}
//
//	/**
//	 * simple server
//	 * 简单的单线程服务模型，一般用于测试
//	 * @param  factory
//	 */
//	public void startSimpleServer(TProtocolFactory factory) {
//		TServerSocket serverSocket = null;
//		try {
//			TProcessor tprocessor = new PDService.Processor<PDService.Iface>(new PDServiceImpl());
//			serverSocket = new TServerSocket(PD_PORT);
//			TServer.Args args = new TServer.Args(serverSocket);
//			args.processor(tprocessor);
//			args.protocolFactory(factory);
//			TServer server = new TSimpleServer(args.processor(tprocessor));
//			System.out.println("SimpleServer started...........");
//			server.serve();
//		} catch (TTransportException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * TTreadPoolServer
//	 * 线程池服务模型， 使用标准的阻塞式I/O，预先创建一组线程处理请求
//	 * @param factory
//	 */
//	private void startTTreadPoolServer(TProtocolFactory factory) {
//		try {
//			TProcessor tprocessor = new PDService.Processor<PDService.Iface>(new PDServiceImpl());
//			TServerSocket serverTransport = new TServerSocket(PD_PORT);
//			TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport);
//			tArgs.processor(tprocessor);
//			tArgs.protocolFactory(factory);
//
//			TServer server = new TThreadPoolServer(tArgs.processor(tprocessor));
//			System.out.println("TThreadPoolServer started...........");
//			server.serve();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * TNonblockingServer
//	 * 使用非阻塞式I/O，服务端和客户端需要指定TFrameTransport
//	 * 采用单线程顺序处理完成，多个调用的请求还是顺序的一个接着一个的执行
//	 * @param factory
//	 */
//	public void startTNonblockingServer (TProtocolFactory factory) {
//		try {
//			TProcessor tprocessor = new PDService.Processor<PDService.Iface>(new PDServiceImpl());
//			TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(PD_PORT);
//			TNonblockingServer.Args tArgs = new TNonblockingServer.Args(tnbSocketTransport);
//			tArgs.processor(tprocessor);
//			//需要指定TFrameTransport
//			tArgs.transportFactory(new TFramedTransport.Factory());
//			tArgs.protocolFactory(factory);
//			TServer server = new TNonblockingServer(tArgs);
//			System.out.println("TNonblockingServer started...........");
//			server.serve();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	/**
//	 * TThreadedSelectorServer
//	 * 多线程半同步半异步服务模型
//	 * @param factory
//	 */
//	public void startTThreadSelectorServer(TProtocolFactory factory) {
//		try {
//			TProcessor tprocessor = new PDService.Processor<PDService.Iface>(new PDServiceImpl());
//			//传统方式，非阻塞方式
//			TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(PD_PORT);
//			//多线程半同步半异步
//			TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
//			tArgs.processor(tprocessor);
//			tArgs.transportFactory(new TFramedTransport.Factory());
//			tArgs.protocolFactory(factory);
//			//多线程 半同步半异步
//			TServer server = new TThreadedSelectorServer(tArgs);
//			System.out.println("TThreadedSelectorServer started...........");
//			server.serve();
//		} catch (TTransportException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 半同步半异步的服务端模型，需要指定为： TFramedTransport 数据传输的方式。
//	 *
//	 * @param factory
//	 */
//	public void startTHsHaServer(TProtocolFactory factory) {
//		try {
//			TProcessor tprocessor = new PDService.Processor<PDService.Iface>(new PDServiceImpl());
//			TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(PD_PORT);
//			THsHaServer.Args tArgs = new THsHaServer.Args(tnbSocketTransport);
//			tArgs.processor(tprocessor);
//			//需要指定TFrameTransport
//			tArgs.transportFactory(new TFramedTransport.Factory());
//			tArgs.protocolFactory(factory);
//			TServer server = new THsHaServer(tArgs);
//			System.out.println("THsHaServer started...........");
//			server.serve();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
