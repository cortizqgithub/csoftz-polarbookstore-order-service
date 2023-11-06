/*----------------------------------------------------------------------------*/
/* Source File:   ORDER.JAVA                                                  */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.domain.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Defines the entity for Orders.
 * <p>The field {@code id} is the primary key (natural key or business key).</p>
 *
 * @param id               Represents the primary key.
 * @param bookIsbn         Indicates the {@code isbn} for the requested book.
 * @param bookName         Indicates the {@code title/author} of the book ordered.
 * @param bookPrice        Indicates the {@code price} of the book ordered.
 * @param quantity         Indicates the number of books ordered.
 * @param status           Indicates the order status.
 * @param createdDate      Audits record creation.
 * @param lastModifiedDate Audits record update.
 * @param version          Controls optimistic lock for concurrent updates.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Table("orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Order(

    @Id
    Long id,

    String bookIsbn,
    String bookName,
    Double bookPrice,
    Integer quantity,
    OrderStatus status,

    @CreatedDate
    Instant createdDate,

    @LastModifiedDate
    Instant lastModifiedDate,

    @Version
    int version) {

    /**
     * @param bookIsbn  Indicates the {@code isbn} for the requested book.
     * @param bookName  Indicates the {@code title/author} of the book ordered.
     * @param bookPrice Indicates the {@code price} of the book ordered.
     * @param quantity  Indicates the number of books ordered.
     * @param status    Indicates the order status.
     * @return Stored information.
     */
    public static Order of(String bookIsbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status) {
        return new Order(null, bookIsbn, bookName, bookPrice, quantity, status, null, null, 0);
    }
}
