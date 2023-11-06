/*----------------------------------------------------------------------------*/
/* Source File:   ORDERREQUEST.JAVA                                           */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.domain.order.request;

import static com.polarbookshop.orderservice.common.config.consts.OrderValidationConstants.ORDER_BOOK_ISBN_MUST_BE_DEFINED;
import static com.polarbookshop.orderservice.common.config.consts.OrderValidationConstants.ORDER_MAX_QUANTITY_LIMIT;
import static com.polarbookshop.orderservice.common.config.consts.OrderValidationConstants.ORDER_MIN_QUANTITY_LIMIT;
import static com.polarbookshop.orderservice.common.config.consts.OrderValidationConstants.ORDER_QUANTITY_MUST_BE_DEFINED;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Defines information required for the order in a web controller.
 *
 * @param isbn     Represents the id of the book ordered.
 * @param quantity The amount of books ordered.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record OrderRequest(
    @NotBlank(message = ORDER_BOOK_ISBN_MUST_BE_DEFINED)
    String isbn,

    @NotNull(message = ORDER_QUANTITY_MUST_BE_DEFINED)
    @Min(value = 1, message = ORDER_MIN_QUANTITY_LIMIT)
    @Max(value = 5, message = ORDER_MAX_QUANTITY_LIMIT)
    Integer quantity) {
}
