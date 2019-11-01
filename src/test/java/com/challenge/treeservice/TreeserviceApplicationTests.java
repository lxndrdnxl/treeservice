package com.challenge.treeservice;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@ActiveProfiles("test")
class TreeserviceApplicationTests {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = TestPostgresContainer.getInstance();

    static {
        postgreSQLContainer.start();
    }

    @Test
    void contextLoads() {
    }

}
