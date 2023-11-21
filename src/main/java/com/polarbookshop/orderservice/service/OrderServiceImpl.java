/*----------------------------------------------------------------------------*/
/* Source File:   ORDERSERVICE.JAVA                                           */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.service;

import static com.polarbookshop.orderservice.common.config.consts.GlobalConstants.ACCEPT_ORDER_BINDING;
import static com.polarbookshop.orderservice.common.config.consts.GlobalConstants.SPACE_HYPHEN_SPACE;

import com.polarbookshop.orderservice.common.infrastructure.http.client.BookHttpClient;
import com.polarbookshop.orderservice.domain.book.Book;
import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.OrderStatus;
import com.polarbookshop.orderservice.domain.order.message.OrderAcceptedMessage;
import com.polarbookshop.orderservice.domain.order.message.OrderDispatchedMessage;
import com.polarbookshop.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implements the contract for the operations to handle Orders.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private static final String SENDING_ORDER_ACCEPTED_EVENT = "Sending order accepted event with id: {}";
    private static final String SENDING_DATA_RESULT = "Result of sending data for order with id {}: {}";

    private final BookHttpClient bookHttpClient;
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    /**
     * Constructor with parameters.
     *
     * @param bookHttpClient  Reference to the HTTP Client that talks to {@code Catalog Service}
     * @param orderRepository Reference to the data management for Orders.
     * @param streamBridge
     */
    public OrderServiceImpl(BookHttpClient bookHttpClient, OrderRepository orderRepository, StreamBridge streamBridge) {
        this.bookHttpClient = bookHttpClient;
        this.orderRepository = orderRepository;
        this.streamBridge = streamBridge;
    }

    @Override
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    @Override
    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookHttpClient.getBookByIsbn(isbn)
            .map(book -> buildAcceptedOrder(book, quantity))
            .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
            .flatMap(orderRepository::save)
            .doOnNext(this::publishOrderAcceptedEvent);
    }

    @Override
    public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux) {
        return flux
            .flatMap(message -> orderRepository.findById(message.orderId()))
            .map(this::buildDispatchedOrder)
            .flatMap(orderRepository::save);
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.status().equals(OrderStatus.ACCEPTED)) {
            return;
        }

        var orderAcceptedMessage = new OrderAcceptedMessage(order.id());
        var result = streamBridge.send(ACCEPT_ORDER_BINDING, orderAcceptedMessage);
        
        log.info(SENDING_ORDER_ACCEPTED_EVENT, order.id());
        log.info(SENDING_DATA_RESULT, order.id(), result);
    }

    private Order buildDispatchedOrder(Order existingOrder) {
        return new Order(
            existingOrder.id(),
            existingOrder.bookIsbn(),
            existingOrder.bookName(),
            existingOrder.bookPrice(),
            existingOrder.quantity(),
            OrderStatus.DISPATCHED,
            existingOrder.createdDate(),
            existingOrder.lastModifiedDate(),
            existingOrder.version()
        );
    }

    private Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(), book.title() + SPACE_HYPHEN_SPACE + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    private Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
