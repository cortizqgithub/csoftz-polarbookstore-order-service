/*----------------------------------------------------------------------------*/
/* Source File:   ORDERACCEPTEDMESSAGE.JAVA                                   */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.18/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.domain.order.message;

/**
 * Holds the Order Accepted message in a PUb/Sub Model.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record OrderAcceptedMessage(Long orderId) {
}
