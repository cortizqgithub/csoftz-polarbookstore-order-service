/*----------------------------------------------------------------------------*/
/* Source File:   APPLICATIONTESTS.JAVA                                       */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.04/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit tests for Application class.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@SpringBootTest
class ApplicationTests {

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
