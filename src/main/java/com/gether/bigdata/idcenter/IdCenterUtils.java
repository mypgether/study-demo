package com.gether.bigdata.idcenter;

import com.sohu.idcenter.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IdCenterUtils extends IdCenter {

  private static Logger log = LoggerFactory.getLogger(IdCenterUtils.class);

  private IdCenterUtils() {
    idWorker = new IdWorker(getWorkerId(), getDatacenterId(), 0);
    log.info("workerId: {}", idWorker.getWorkerId());
  }

  private static IdCenterUtils instance = new IdCenterUtils();

  public synchronized static Long getId() {
    if (instance == null) {
      instance = new IdCenterUtils();
    }
    long id = instance.getGlobalId();
    return id;
  }
}