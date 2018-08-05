package com.gether.bigdata.service.impl;

import com.gether.bigdata.dao.dataobject.Device;
import com.gether.bigdata.dao.dataobject.DeviceExample;
import com.gether.bigdata.dao.mapper.DeviceMapper;
import com.gether.bigdata.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by myp on 2017/1/23.
 */
@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

  @Autowired
  private DeviceMapper deviceMapper;

  @Override
  public void addDevice(boolean rollback) {
    Device record = new Device();
    record.setDeviceId("deviceid1");
    record.setName("name1");
    record.setUid(1000L);
    record.setComment("comment");
    deviceMapper.insertSelective(record);

    record.setDeviceId("deviceid2");
    record.setName("name");
    deviceMapper.insertSelective(record);
    if (rollback) {
      throw new RuntimeException("need rollback");
    }
  }

  @Override
  public List<Device> getDeviceList() {
    DeviceExample example = new DeviceExample();
    return deviceMapper.selectByExample(example);
  }
}