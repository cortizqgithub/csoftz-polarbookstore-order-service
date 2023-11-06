/*----------------------------------------------------------------------------*/
/* Source File:   ORDERSERVICE.JAVA                                           */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.service;

import static com.polarbookshop.orderservice.common.config.consts.GlobalConstants.SPACE_HYPHEN_SPACE;

import com.polarbookshop.orderservice.common.infrastructure.http.client.BookHttpClient;
import com.polarbookshop.orderservice.domain.book.Book;
import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.OrderStatus;
import com.polarbookshop.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implements the contract for the operations to handle Orders.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final BookHttpClient bookHttpClient;
    private final OrderRepository orderRepository;

    /**
     * Constructor with parameters.
     *
     * @param bookHttpClient  Reference to the HTTP Client that talks to {@code Catalog Service}
     * @param orderRepository Reference to the data management for Orders.
     */
    public OrderServiceImpl(BookHttpClient bookHttpClient, OrderRepository orderRepository) {
        this.bookHttpClient = bookHttpClient;
        this.orderRepository = orderRepository;
    }

    @Override
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookHttpClient.getBookByIsbn(isbn)
            .map(book -> buildAcceptedOrder(book, quantity))
            .flatMap(orderRepository::save);
    }

    private Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(), book.title() + SPACE_HYPHEN_SPACE + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    private Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
