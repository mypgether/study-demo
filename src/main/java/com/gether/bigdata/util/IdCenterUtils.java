package com.gether.bigdata.util;

import com.sohu.idcenter.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IdCenterUtils extends IdCenter {

	private static Logger log = LoggerFactory.getLogger(IdCenterUtils.class);

	// 慎用idepo参数，发布代码后， 发现较多的Id冲突，结果定位是每次指定了idepo导致
	private IdCenterUtils() {
		/*final long idepo = System.currentTimeMillis() - 3600 * 1000L;*/
		idWorker = new IdWorker(getWorkerId(), getDatacenterId(), 0 /*, idepo */);
		//log.info("workerId: {}", idWorker.getWorkerId());
	}

	private static IdCenterUtils instance = new IdCenterUtils();

	public synchronized static Long getId() {
		if (instance == null) {
			instance = new IdCenterUtils();
		}
		long eventId = instance.getGlobalId();
		return eventId;
	}
}