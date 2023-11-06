/*----------------------------------------------------------------------------*/
/* Source File:   APPLICATIONTESTS.JAVA                                       */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.04/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice;

import static com.polarbookshop.orderservice.common.consts.TestContainerConstants.POSTGRESQL_DOCKER_VERSION;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Unit tests for Application class.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ApplicationTests {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRESQL_DOCKER_VERSION);

    /**
     * Load Spring Boot contexts to validate it can run the application.
     * Somehow, as some dependencies such as Backend Data Service or another component, it
     * is required that we use Testcontainers in order to boostrap that dependency.
     * As this backend service requires PostgreSQL, we use a docker container to
     * fill the requirement, and actually use the same server as in prod. Here, we
     * use Testcontainers to wire that context test. NOTE: Other containers should be
     * added when required.
     */
    @Test
    void contextLoads() {
    }
}
