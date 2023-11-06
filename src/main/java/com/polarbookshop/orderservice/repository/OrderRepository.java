/*----------------------------------------------------------------------------*/
/* Source File:   ORDERREPOSITORY.JAVA                                        */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.repository;

import com.polarbookshop.orderservice.domain.order.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Defines the operations to access data for {@code Orders}.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}
