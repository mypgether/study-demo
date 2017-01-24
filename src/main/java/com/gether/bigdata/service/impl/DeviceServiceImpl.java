package com.gether.bigdata.service.impl;

import com.gether.bigdata.dao.dataobject.ODevice;
import com.gether.bigdata.dao.dataobject.ODeviceExample;
import com.gether.bigdata.dao.mapper.ODeviceMapper;
import com.gether.bigdata.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myp on 2017/1/23.
 */
@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private ODeviceMapper oDeviceMapper;

    @Override
    public void addDevice(boolean rollback) {
        ODevice record = new ODevice();
        record.setDeviceid("deviceid1");
        record.setStatus(0);
        record.setRegistertime(new Date());
        record.setDevicename("devicename1");
        record.setModelid("modelid");
        record.setCloudid(1);
        record.setUid(1000L);
        oDeviceMapper.insertSelective(record);

        record.setDeviceid("deviceid2");
        record.setDevicename("devicename2");
        oDeviceMapper.insertSelective(record);
        if (rollback) {
            throw new RuntimeException("need rollback");
        }
    }

    @Override
    public List<ODevice> getDeviceList() {
        ODeviceExample example = new ODeviceExample();
        return oDeviceMapper.selectByExample(example);
    }
}