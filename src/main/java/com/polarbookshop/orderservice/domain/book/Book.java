/*----------------------------------------------------------------------------*/
/* Source File:   BOOK.JAVA                                                   */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.domain.book;

/**
 * Defines the entity for Books.
 * <p>The field {@code isbn} is the primary key (natural key or business key).</p>
 * <p>The field {@code id} is a technical key (or surrogate key).</p>
 * <p>The field {@code version} is used to control a concurrent record update.</p>
 *
 * @param id     Represents the technical key.
 * @param isbn   Represents the primary key.
 * @param title  Represents the given name for the book.
 * @param author Represents the writer of the book.
 * @param price  Indicates the value of the book.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record Book(
    Long id,
    String isbn,
    String title,
    String author,
    Double price) {
}
