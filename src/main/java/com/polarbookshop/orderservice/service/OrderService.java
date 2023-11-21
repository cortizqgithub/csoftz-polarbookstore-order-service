/*----------------------------------------------------------------------------*/
/* Source File:   ORDERSERVICE.JAVA                                           */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.service;

import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.message.OrderDispatchedMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Defines the contract for the operations to handle Orders.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 * @see Order
 */
public interface OrderService {
    /**
     * Retrieves a list (Stream) of registered Orders.
     *
     * @return An Order List (Stream).
     */
    Flux<Order> getAllOrders();

    /**
     * Stores an Order in the backend data service (PostgreSQL database).
     *
     * @param isbn     Represents the id of the book ordered.
     * @param quantity The amount of books ordered.
     * @return Saved Order information.
     */
    Mono<Order> submitOrder(String isbn, int quantity);

    /**
     * Adds the processing to consume the message received from the message queue.
     *
     * @param flux Includes a stream of data to be processed.
     * @return The order being processed.
     */
    Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux);
}
