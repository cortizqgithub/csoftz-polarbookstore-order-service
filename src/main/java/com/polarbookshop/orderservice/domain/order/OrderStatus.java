/*----------------------------------------------------------------------------*/
/* Source File:   ORDERSTATUS.JAVA                                            */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.domain.order;

/**
 * Indicates the Book status.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public enum OrderStatus {
    ACCEPTED,
    REJECTED,
    DISPATCHED
}
