package com.gether.bigdata.device;

import com.gether.bigdata.dao.dataobject.Device;
import com.gether.bigdata.service.DeviceService;
import com.gether.bigdata.web.view.DeviceController;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by myp on 2017/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
public class DeviceMockTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    //@SpyBean 部分mock
    //@MockBean
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //@MockBean
    @SpyBean
    private DeviceService deviceService;

    //@MockBean
    @SpyBean
    private DeviceController deviceController;

    @Test
    public void getDeviceList() throws Exception {
        List<Device> devices = Lists.newArrayList();
        Device d = new Device();
        d.setName("name1");
        d.setDeviceId("deviceid1");
        devices.add(d);
        when(deviceController.devicelist()).thenReturn(devices);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/device")).andDo(MockMvcResultHandlers.print()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}