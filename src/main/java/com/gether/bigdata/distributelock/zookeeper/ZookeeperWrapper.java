package com.gether.bigdata.distributelock.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ZookeeperWrapper implements Watcher, InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(ZookeeperWrapper.class);
  private ZooKeeper zk;
  private String root = "/distributedLock";// 根
  private String hostport = "127.0.0.1";
  private int sessionTimeout = 20000; //ms
  private boolean init = false;

  private List<ZookeeperNodeListener> nodeListenerList;

  public ZooKeeper getZk() {
    return zk;
  }

  public synchronized void addListener(ZookeeperNodeListener listener) {
    try {
      if (null == nodeListenerList) {
        nodeListenerList = new CopyOnWriteArrayList<ZookeeperNodeListener>();
      }
      nodeListenerList.add(listener);
    } catch (Exception e) {
      logger.error("zookeeper addListener error", e);
    }
  }

  public synchronized void removeListener(ZookeeperNodeListener listener) {
    try {
      if (null != nodeListenerList) {
        nodeListenerList.remove(listener);
      }
    } catch (Exception e) {
      logger.error("zookeeper addListener error", e);
    }
  }

  /**
   * zookeeper节点的监视器
   */
  @Override
  public void process(WatchedEvent event) {
    if (null != nodeListenerList && nodeListenerList.size() > 0) {
      for (ZookeeperNodeListener listener : nodeListenerList) {
        listener.onWatchEvent(event);
      }
    }
    if (event.getType() == EventType.None && event.getState() == KeeperState.SyncConnected) {
      registerZookeeper();
    } else if (event.getType() == EventType.None && event.getState() == KeeperState.Expired) {
      logger.error("zookeeper session expired");
      initZookeeper();
    }
  }

  private void registerZookeeper() {
    try {
      if (zk != null) {
        if (zk.exists(root, false) == null) {
          // 创建根节点
          zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
      } else {
        logger.error("zookeeper registerZookeeper zk should not be null");
      }
    } catch (Exception e) {
      logger.error("registerZookeeper error,", e);
    }
  }

  private void initZookeeper() {
    try {
      if (null == nodeListenerList) {
        nodeListenerList = new CopyOnWriteArrayList<ZookeeperNodeListener>();
      }
      zk = new ZooKeeper(hostport, sessionTimeout, this);
      logger.info("zookeeper init SUCCESS.");
    } catch (Exception e) {
      logger.error("zookeeper init connect fail error,", e);
      throw new RuntimeException("zookeeper init connect fail" + e.getMessage());
    }
  }

  /**
   * Callback when zookeeper node changed
   */
  public interface ZookeeperNodeListener {

    public void onWatchEvent(WatchedEvent event);
  }

  public void setHostport(String hostport) {
    this.hostport = hostport;
  }

  public void setSessionTimeout(int sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (init) {
      initZookeeper();
    }
  }
}