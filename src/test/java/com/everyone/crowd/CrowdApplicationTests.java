package com.everyone.crowd;

import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrowdApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testDataSource() {
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getClass().getName());

        DataSourceProperties properties = context.getBean(DataSourceProperties.class);
        System.out.println(properties.getUrl());
    }

    @Test
    public void testUserService() {
        UserService userService = context.getBean(UserService.class);

        // 测试注册
        User userToRegister = new User();
        userToRegister.setUsername("user1");
        userToRegister.setPassword("123456");
        userToRegister.setEmail("user1@example.com");
        userService.register(userToRegister);

        // 测试激活账号

    }

}
