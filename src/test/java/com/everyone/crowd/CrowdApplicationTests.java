package com.everyone.crowd;

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
}
