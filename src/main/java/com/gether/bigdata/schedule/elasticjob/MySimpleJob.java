package com.gether.bigdata.schedule.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @author: myp
 * @date: 16/8/20
 */
public class MySimpleJob implements SimpleJob {

  static int time = 0;

  static long lastTime = System.currentTimeMillis();


  public void execute(ShardingContext shardingContext) {
    int shardingItem = shardingContext.getShardingItem();
    switch (shardingItem) {
      case 0:
        // do something by sharding items 0
        break;
      case 1:
        // do something by sharding items 1
        break;
      case 2:
        // do something by sharding items 2
        break;
      // case n: ...
    }
    time = time + 1;
    System.out.println("jobStart,times:" + time + "====>" + shardingContext.toString());
    long now = System.currentTimeMillis();
    System.err.println("time span:" + (now - lastTime) / 1000);
    lastTime = now;
  }
}