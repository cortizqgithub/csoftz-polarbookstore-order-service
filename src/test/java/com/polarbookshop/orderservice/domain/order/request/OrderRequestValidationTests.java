/*----------------------------------------------------------------------------*/
/* Source File:   ORDERREQUESTVALIDATIONTESTS.JAVA                            */
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
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Performs OrderRequest Validation tests.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
class OrderRequestValidationTests {
    private static final int BOOK_QUANTITY = 1;
    private static final int BOOK_QUANTITY_ZERO = 0;
    private static final int BOOK_QUANTITY_SEVEN = 7;

    private static final String ISBN = "1234567890";
    private static final String ISBN_EMPTY = "";

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Verify there are no validation errors.")
    void whenAllFieldsCorrectThenValidationSucceeds() {
        assertThat(validator.validate(new OrderRequest(ISBN, BOOK_QUANTITY))).isEmpty();
    }

    @Test
    @DisplayName("Verify ISBN is not defined.")
    void whenIsbnNotDefinedThenValidationFails() {
        var violations = validator.validate(new OrderRequest(ISBN_EMPTY, BOOK_QUANTITY));

        assertThat(violations).hasSize(BOOK_QUANTITY);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo(ORDER_BOOK_ISBN_MUST_BE_DEFINED);
    }

    @Test
    @DisplayName("Verify Quantiy is not defined.")
    void whenQuantityIsNotDefinedThenValidationFails() {
        var violations = validator.validate(new OrderRequest(ISBN, null));

        assertThat(violations).hasSize(BOOK_QUANTITY);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo(ORDER_QUANTITY_MUST_BE_DEFINED);
    }

    @Test
    @DisplayName("Verify Quantity is lower than minimum.")
    void whenQuantityIsLowerThanMinThenValidationFails() {
        var violations = validator.validate(new OrderRequest(ISBN, BOOK_QUANTITY_ZERO));

        assertThat(violations).hasSize(BOOK_QUANTITY);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo(ORDER_MIN_QUANTITY_LIMIT);
    }

    @Test
    @DisplayName("Verify Quantity is greater than maximum.")
    void whenQuantityIsGreaterThanMaxThenValidationFails() {
        var violations = validator.validate(new OrderRequest(ISBN, BOOK_QUANTITY_SEVEN));

        assertThat(violations).hasSize(BOOK_QUANTITY);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo(ORDER_MAX_QUANTITY_LIMIT);
    }
}