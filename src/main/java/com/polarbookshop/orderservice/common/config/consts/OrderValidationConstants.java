/*----------------------------------------------------------------------------*/
/* Source File:   ORDERVALIDATIONCONSTANTS.JAVA                               */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.config.consts;

/**
 * Order Validation Constants.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class OrderValidationConstants {
    public static final String ORDER_BOOK_ISBN_MUST_BE_DEFINED = "The book ISBN must be defined.";

    public static final String ORDER_QUANTITY_MUST_BE_DEFINED = "The book quantity must be defined.";
    public static final String ORDER_MIN_QUANTITY_LIMIT = "You must order at least 1 item.";
    public static final String ORDER_MAX_QUANTITY_LIMIT = "You cannot order more than 5 items.";

    /**
     * This is a utility class thus we must avoid to instantiate this.
     */
    public OrderValidationConstants() {
    }
}
