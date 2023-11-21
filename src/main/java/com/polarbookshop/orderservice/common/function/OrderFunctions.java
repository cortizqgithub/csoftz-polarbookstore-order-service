/*----------------------------------------------------------------------------*/
/* Source File:   ORDERFUNCTIONS.JAVA                                         */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.18/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.function;

import com.polarbookshop.orderservice.domain.order.message.OrderDispatchedMessage;
import com.polarbookshop.orderservice.service.OrderService;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * Creates a function to be used ina Pub/sub model to accomplish the Order flow for the Polar Bookstore.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
public class OrderFunctions {
    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    public Consumer<Flux<OrderDispatchedMessage>> dispatchOrder(OrderService orderService) {
        return flux -> orderService.consumeOrderDispatchedEvent(flux)
            .doOnNext(order -> log.info("The order with id {} is dispatched", order.id()))
            .subscribe();
    }
}
