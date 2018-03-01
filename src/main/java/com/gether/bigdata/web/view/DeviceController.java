package com.gether.bigdata.web.view;

import com.gether.bigdata.dao.dataobject.Device;
import com.gether.bigdata.service.DeviceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: myp
 * @date: 16/10/22
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @RequestMapping(value = "/batchAdd", method = RequestMethod.GET)
    public String devicebatchAdd(@RequestParam(name = "rollback", required = false) boolean rollback) {
        deviceService.addDevice(rollback);
        return "success";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Device> devicelist() {
        return deviceService.getDeviceList();
    }
}