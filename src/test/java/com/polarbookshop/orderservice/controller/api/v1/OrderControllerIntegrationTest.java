/*----------------------------------------------------------------------------*/
/* Source File:   ORDERCONTROLLERINTEGRATIONTESTS.JAVA                        */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.20/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.controller.api.v1;

import static com.polarbookshop.orderservice.common.config.consts.GlobalConstants.SPACE_HYPHEN_SPACE;
import static com.polarbookshop.orderservice.common.consts.TestContainerConstants.POSTGRESQL_DOCKER_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.orderservice.common.infrastructure.http.client.BookHttpClient;
import com.polarbookshop.orderservice.domain.book.Book;
import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.OrderStatus;
import com.polarbookshop.orderservice.domain.order.message.OrderAcceptedMessage;
import com.polarbookshop.orderservice.domain.order.request.OrderRequest;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

/**
 * Performs Full Integration Test (uses the whole Spring Context) for {@link OrderController}.
 * <p>When using {@link SpringBootTest}, this annotation loads all registered beans in the application, even those not needed
 * for the {@link OrderController}</p>
 * <p><b>Path:</b>{@code api/v1/orders}</p>
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderControllerIntegrationTest {
    private static final int BOOK_QUANTITY_1 = 1;
    private static final int BOOK_QUANTITY_3 = 3;

    public static final String BOOK_TITLE = "Title";
    public static final String BOOK_AUTHOR = "Author";

    public static final double BOOK_PRICE = 9.90;

    private Long BOOK_ID_1 = 1L;
    private Long BOOK_ID_2 = 2L;

    private static final String ISBN_1 = "1234567893";
    private static final String ISBN_2 = "1234567899";
    private static final String ISBN_3 = "1234567894";
    private static final String API_VI_ORDER_PATH = "/api/v1/orders";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRESQL_DOCKER_VERSION);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutputDestination output;

    @MockBean
    private BookHttpClient bookClient;

    @Test
    @DisplayName("Verify we retrieve Orders.")
    void whenGetOrdersThenReturn() throws IOException {
        var bookIsbn = ISBN_1;
        var book = new Book(BOOK_ID_1, bookIsbn, BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE);
        var orderRequest = new OrderRequest(bookIsbn, BOOK_QUANTITY_1);

        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.just(book));

        var expectedOrder = webTestClient.post().uri(API_VI_ORDER_PATH)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class).returnResult().getResponseBody();
        assertThat(expectedOrder).isNotNull();
        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderAcceptedMessage.class))
            .isEqualTo(new OrderAcceptedMessage(expectedOrder.id()));

        webTestClient.get().uri(API_VI_ORDER_PATH)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(Order.class).value(orders -> {
                assertThat(orders.stream().filter(order -> order.bookIsbn().equals(bookIsbn)).findAny()).isNotEmpty();
            });
    }

    @Test
    @DisplayName("Verify we can place an Order (Accepted).")
    void whenPostRequestAndBookExistsThenOrderAccepted() throws IOException {
        var bookIsbn = ISBN_2;
        var book = new Book(BOOK_ID_2, bookIsbn, BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE);
        var orderRequest = new OrderRequest(bookIsbn, BOOK_QUANTITY_3);

        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.just(book));

        Order createdOrder = webTestClient.post().uri(API_VI_ORDER_PATH)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class).returnResult().getResponseBody();

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.bookIsbn()).isEqualTo(orderRequest.isbn());
        assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        assertThat(createdOrder.bookName()).isEqualTo(book.title() + SPACE_HYPHEN_SPACE + book.author());
        assertThat(createdOrder.bookPrice()).isEqualTo(book.price());
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.ACCEPTED);

        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderAcceptedMessage.class))
            .isEqualTo(new OrderAcceptedMessage(createdOrder.id()));
    }

    @Test
    @DisplayName("Verify Order rejected because the book does not exist.")
    void whenPostRequestAndBookNotExistsThenOrderRejected() {
        var bookIsbn = ISBN_3;
        var orderRequest = new OrderRequest(bookIsbn, BOOK_QUANTITY_3);

        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.empty());

        var createdOrder = webTestClient.post().uri(API_VI_ORDER_PATH)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class).returnResult().getResponseBody();

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.bookIsbn()).isEqualTo(orderRequest.isbn());
        assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }
}
