package com.gether.bigdata.distributelock.zookeeper;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistributedLock implements ZookeeperWrapper.ZookeeperNodeListener {

  private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);
  private ZookeeperWrapper zookeeperWrapper;
  private String root = "/distributedLock";// 根

  private String lockName;// 竞争资源的标志
  private String waitNode;// 等待前一个锁
  private String myZnode;// 当前锁
  private CountDownLatch waitLockLatch;// 计数器
  private volatile boolean releaseLock = false;

  /**
   * 创建分布式锁,使用前请确认config配置的zookeeper服务可用
   *
   * @param zookeeperWrapper 最好保证是单例
   */
  public DistributedLock(ZookeeperWrapper zookeeperWrapper) {
    this.zookeeperWrapper = zookeeperWrapper;
    this.zookeeperWrapper.addListener(this);
  }

  @Override
  public void onWatchEvent(WatchedEvent event) {
    // 如果是节点删除并且是我等待的节点，那么就表示获取锁
    if (event.getType() == EventType.NodeDeleted
        && event.getPath().contains(waitNode)) {
      if (!releaseLock && null != this.waitLockLatch) {
        this.waitLockLatch.countDown();
      }
    }
  }

  /**
   * 获取分布式锁
   *
   * @param lockname 锁的名称
   * @param waitTimeout 等待时长
   * @return true获取锁
   */
  public boolean lock(String lockname, long waitTimeout) {
    try {
      this.lockName = lockname;
      this.releaseLock = false;
      if (this.tryLock()) {
        return true;
      } else {
        return waitForLock(waitNode, waitTimeout);// 等待锁
      }
    } catch (KeeperException e) {
      throw new LockException(e);
    } catch (InterruptedException e) {
      throw new LockException(e);
    }
  }

  private boolean tryLock() {
    try {
      String splitStr = "_lock_";
      if (lockName.contains(splitStr)) {
        throw new LockException("lockName can not contains \\u000B");
      }
      // 创建临时子节点
      myZnode = zookeeperWrapper.getZk().create(
          root + "/" + lockName + splitStr, new byte[0],
          ZooDefs.Ids.OPEN_ACL_UNSAFE,
          CreateMode.EPHEMERAL_SEQUENTIAL);
      // 取出所有子节点
      List<String> subNodes = zookeeperWrapper.getZk().getChildren(root,
          false);
      // 取出所有lockName的锁
      List<String> lockObjNodes = new ArrayList<String>();
      for (String node : subNodes) {
        String _node = node.split(splitStr)[0];
        if (_node.equals(lockName)) {
          lockObjNodes.add(node);
        }
      }
      Collections.sort(lockObjNodes);
      if (myZnode.equals(root + "/" + lockObjNodes.get(0))) {
        // 如果是最小的节点,则表示取得锁
        return true;
      }
      // 如果不是最小的节点，找到比自己小1的节点
      String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
      waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
    } catch (KeeperException e) {
      throw new LockException(e);
    } catch (InterruptedException e) {
      throw new LockException(e);
    }
    return false;
  }

  private boolean waitForLock(String lower, long waitTime)
      throws InterruptedException, KeeperException {
    Stat stat = zookeeperWrapper.getZk().exists(root + "/" + lower, true);
    // 判断比自己小一个数的节点是否存在,如果不存在则无需等待锁,同时注册监听
    if (stat != null) {
      this.waitLockLatch = new CountDownLatch(1);
      return this.waitLockLatch.await(waitTime, TimeUnit.MILLISECONDS);
    }
    return true;
  }

  public void unlock() {
    try {
      this.releaseLock = true;
      this.waitLockLatch = null;
      this.waitNode = null;

      zookeeperWrapper.removeListener(this);
      if (StringUtils.isNotBlank(myZnode)) {
        Stat stat = zookeeperWrapper.getZk().exists(myZnode, false);
        if (stat != null) {
          zookeeperWrapper.getZk().delete(myZnode, -1);
        }
      }
      this.myZnode = null;
    } catch (InterruptedException e) {
      logger.error("zookeeper unlock InterruptedException error,", e);
    } catch (KeeperException e) {
      logger.error("zookeeper unlock KeeperException error,", e);
    } catch (Throwable e) {
      logger.error("zookeeper unlock Throwable error,", e);
    }
  }

  public class LockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LockException(String e) {
      super(e);
    }

    public LockException(Exception e) {
      super(e);
    }
  }
}