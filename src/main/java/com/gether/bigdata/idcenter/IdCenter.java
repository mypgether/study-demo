package com.gether.bigdata.idcenter;

import com.sohu.idcenter.IdWorker;
import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * 
 * 算法： https://github.com/twitter/snowflake
 * 
 * http://sail-y.github.io/2015/04/22/-%E8%BD%AC-Twitter%E7%9A%84%E5%88%86%E5%B8%83%E5%BC%8F%E8%87%AA%E5%A2%9EID%E7%AE%97%E6%B3%95Snowflake%E5%AE%9E%E7%8E%B0%E5%88%86%E6%9E%90%E5%8F%8A%E5%85%B6Java%E3%80%81Php%E5%92%8CPython%E7%89%88/
 * 
 * 在分布式系统中，需要生成全局UID的场合还是比较多的，twitter的snowflake解决了这种需求，实现也还是很简单的，除去配置信息，核心代码就是毫秒级时间41位+机器ID 10位+毫秒内序列12位。
 * 核心代码为其IdWorker这个类实现，其原理结构如下，我分别用一个0表示一位，用—分割开部分的作用：
 *	1	0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---0000000000 00
 * 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），接下来的41位为毫秒级时间，然后5位datacenter标识位，5位机器ID（并不算标识符，实际是为线程标识），然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。
 * 这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 * 
 * java版本是实现：https://github.com/adyliu/idcenter
 * 
 * @author myp
 * @since
 *
 */
public abstract class IdCenter {
	
//	protected static final long ID_CENTER_RAWID 		= 1l;
//	protected static final long ID_CENTER_EVENTID 		= 2l;
//	protected static final long ID_CENTER_SECTIONID 	= 3l; //cassandra情况下可能废弃
	
	private static final Random r = new Random();
	
	protected IdWorker idWorker = null;
	
	protected static long getWorkerId(){
		 String workerId = System.getProperty("workerId");
		 long workerIdLong = r.nextInt(32);
		 if(StringUtils.isNotBlank(workerId)){
			 workerIdLong = Long.valueOf(workerId);
		 }
		 return workerIdLong;
	}

	protected static long getDatacenterId(){
		String datacenterId = System.getProperty("datacenterId");
		long datacenterIdLong = r.nextInt(32);
		if(StringUtils.isNotBlank(datacenterId)){
			datacenterIdLong = Long.valueOf(datacenterId);
		}
		return datacenterIdLong;
	}
	
	protected long getGlobalId(){
		if(idWorker != null){
			return idWorker.getId();
		}
		return -1L;
	}
}