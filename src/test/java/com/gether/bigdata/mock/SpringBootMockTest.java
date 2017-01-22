package com.gether.bigdata.mock;

import com.gether.bigdata.domain.user.User;
import com.gether.bigdata.service.UserService;
import com.gether.bigdata.web.view.UserController;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by myp on 2017/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
public class SpringBootMockTest {

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
    private UserService userService;

    //@MockBean
    @SpyBean
    private UserController userController;

    @Test
    public void getUserList() throws Exception {
        Long id = 589231890816389120L;
        List<User> users = new ArrayList<>();
        User u = new User();
        u.setId(99999999L);
        u.setAge(100);
        u.setName("测试");
        users.add(u);
        when(userService.list()).thenReturn(users);
        // 部分mock，部分可以用
        //given(userController.getUser(id)).willReturn(u);

        MvcResult listresult = mockMvc.perform(get("/users/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andReturn();
        String content = new String(listresult.getResponse().getContentAsByteArray());
        System.out.println(content);

        //JSONArray listArray = JSONObject.parseArray(content);
        //Long id = ((JSONObject) listArray.get(0)).getLong("id");
        MvcResult getuserbyIdresult = mockMvc.perform(get("/users/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andReturn();
        System.out.println(getuserbyIdresult.getResponse().getContentAsString());
    }

    @Test
    public void userBatchAdd() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/batchAdd")
                //.param("a", "10")
                //.param("b", "20")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("success")))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        //[{"id":589231890816389120,"name":"123123","age":1},{"id":589246697630547968,"name":"123sadasd","age":1},{"id":589246698037395456,"name":"name2","age":2},{"id":589246698041589760,"name":"name3","age":3},{"id":589246698045784064,"name":"name4","age":4},{"id":589246698054172672,"name":"name5","age":5},{"id":589248285322686464,"name":"123sadasd","age":1},{"id":589248285687590912,"name":"name2","age":2},{"id":589248549121363968,"name":"123sadasd","age":1},{"id":589248549498851328,"name":"name2","age":2},{"id":589248858893717504,"name":"123sadasd","age":1},{"id":589248859564806144,"name":"name2","age":2},{"id":589248859581583360,"name":"name3","age":3},{"id":589248859589971968,"name":"name4","age":4},{"id":589248859598360576,"name":"name5","age":5}]
        MvcResult listresult = mockMvc.perform(get("/users/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("589231890816389120"))
                .andReturn();
        String content = new String(listresult.getResponse().getContentAsByteArray());
        System.out.println(content);
    }
}