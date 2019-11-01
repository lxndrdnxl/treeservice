package com.challenge.treeservice.node;


import com.challenge.treeservice.TestPostgresContainer;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class AbstractTreeserviceIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = TestPostgresContainer.getInstance();

    static {
        postgreSQLContainer.start();
    }

    @Autowired
    JdbcTemplate jdbcTemplate;


    @BeforeEach
    public void setUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "nodes");
        jdbcTemplate.execute("ALTER SEQUENCE seq_node_id RESTART WITH 1");
    }

}
