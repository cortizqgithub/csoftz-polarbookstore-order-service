/*----------------------------------------------------------------------------*/
/* Source File:   ORDERREQUEST.JAVA                                           */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.controller.api.v1;

import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.request.OrderRequest;
import com.polarbookshop.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Exposes as a REST ApI the Book Ordering.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Constructor with params.
     *
     * @param orderService Holds a reference to the order management service.
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all registered orders.
     *
     * @return A stream of Order items.
     * @see Order
     */
    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Stores a new {@code Order} in the backedn data service (PostgreSQL).
     *
     * @param orderRequest
     * @return Saved Order information
     * @see Order
     */
    @PostMapping
    public Mono<Order> submitOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
    }
}
