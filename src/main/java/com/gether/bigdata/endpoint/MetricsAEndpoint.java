package com.gether.bigdata.endpoint;

import com.gether.bigdata.service.DeviceService;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

public class MetricsAEndpoint extends AbstractEndpoint<String> {
    private final DeviceService deviceService;

    MetricsAEndpoint(DeviceService deviceService) {
        super("metrics");
        this.deviceService = deviceService;
    }

    public String invoke() {
        return String.valueOf(deviceService.getDeviceList().size());
    }
}