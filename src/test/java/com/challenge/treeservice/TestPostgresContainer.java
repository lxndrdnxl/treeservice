package com.challenge.treeservice;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgresContainer extends PostgreSQLContainer<TestPostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:alpine";
    private static TestPostgresContainer container;

    private TestPostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static TestPostgresContainer getInstance() {
        if (null == container) {
            container = new TestPostgresContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }
}
