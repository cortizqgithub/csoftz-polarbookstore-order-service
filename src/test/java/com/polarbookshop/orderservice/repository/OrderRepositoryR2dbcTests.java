/*----------------------------------------------------------------------------*/
/* Source File:   ORDERREPOSITORYR2DBCTESTS.JAVA                              */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.repository;

import static com.polarbookshop.orderservice.common.consts.TestContainerConstants.POSTGRESQL_DOCKER_VERSION;

import com.polarbookshop.orderservice.common.config.DataConfig;
import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

/**
 * Spring Data R2DBC sliced integration test using TestContainers for integration to the backend data service (PostgreSQL).
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Testcontainers
@DataR2dbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryR2dbcTests {
    private static final long BOOK_ID_NOT_EXIST = 394L;

    private static final int EXPECTED_NEXT_COUNT = 0;
    private static final int BOOK_QUANTITY = 3;

    private static final String ISBN = "1234567890";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRESQL_DOCKER_VERSION);

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Verify when finding a not existing book, then return empty.")
    void shouldFindOrderByIdWhenNotExisting() {
        StepVerifier.create(orderRepository.findById(BOOK_ID_NOT_EXIST))
            .expectNextCount(EXPECTED_NEXT_COUNT)
            .verifyComplete();
    }

    @Test
    @DisplayName("Verify when no found, then reject entry is returned.")
    void createRejectedOrder() {
        var rejectedOrder = buildRejectedOrder(ISBN, BOOK_QUANTITY);

        StepVerifier.create(orderRepository.save(rejectedOrder))
            .expectNextMatches(order -> order.status().equals(OrderStatus.REJECTED))
            .verifyComplete();
    }

    private Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}