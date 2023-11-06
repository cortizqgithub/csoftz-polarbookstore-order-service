/*----------------------------------------------------------------------------*/
/* Source File:   ORDERCONTROLLERWEBFLUXTESTS.JAVA                            */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.controller.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.OrderStatus;
import com.polarbookshop.orderservice.domain.order.request.OrderRequest;
import com.polarbookshop.orderservice.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 * Unit Test/Integration for OrderController.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@WebFluxTest(OrderController.class)
class OrderControllerWebFluxTests {
    private static final int BOOK_QUANTITY = 3;

    private static final String ISBN = "1234567890";
    private static final String API_VI_ORDER_PATH = "/api/v1/orders";

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("Verify when Book not available then reject order")
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest(ISBN, BOOK_QUANTITY);
        var expectedOrder = buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());

        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity())).willReturn(Mono.just(expectedOrder));

        webClient
            .post()
            .uri(API_VI_ORDER_PATH)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class).value(actualOrder -> {
                assertThat(actualOrder).isNotNull();
                assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
            });
    }

    private Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}