/*----------------------------------------------------------------------------*/
/* Source File:   ORDERREQUESTJSONTESTS.JAVA                                  */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.domain.order.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbookshop.orderservice.domain.order.Order;
import com.polarbookshop.orderservice.domain.order.OrderStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

/**
 * Perform JSON validation on the Order Request model.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonTest
public class OrderRequestJsonTests {
    public static final long ORDER_ID = 394L;
    public static final double BOOK_PRICE = 9.90;
    public static final int BOOK_ORDERED_QUANTITY = 1;
    public static final int ORDER_RECORD_VERSION = 21;

    public static final String BOOK_ISBN = "1234567890";
    public static final String BOOK_NAME = "Book Name";
    public static final String ORDER_ID_JPATH = "@.id";
    public static final String ORDER_BOOK_ISBN_JPATH = "@.bookIsbn";
    public static final String ORDER_BOOK_NAME_JPATH = "@.bookName";
    public static final String ORDER_BOOK_PRICE_JPATH = "@.bookPrice";
    public static final String ORDER_BOOK_QUANTITY_JPATH = "@.quantity";
    public static final String ORDER_STATUS_JPATH = "@.status";
    public static final String ORDER_CREATED_DATE_JPATH = "@.createdDate";
    public static final String ORDER_LAST_MODIFIED_DATE_JPATH = "@.lastModifiedDate";
    public static final String ORDER_RECORD_VERSION_JPATH = "@.version";

    @Autowired
    private JacksonTester<Order> json;

    @Test
    @DisplayName("Verify we can serialize to JSON.")
    void testSerialize() throws Exception {
        var order = new Order(ORDER_ID, BOOK_ISBN, BOOK_NAME, BOOK_PRICE, BOOK_ORDERED_QUANTITY, OrderStatus.ACCEPTED, Instant.now(), Instant.now(), ORDER_RECORD_VERSION);
        var jsonContent = json.write(order);
        assertThat(jsonContent).extractingJsonPathNumberValue(ORDER_ID_JPATH)
            .isEqualTo(order.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue(ORDER_BOOK_ISBN_JPATH)
            .isEqualTo(order.bookIsbn());
        assertThat(jsonContent).extractingJsonPathStringValue(ORDER_BOOK_NAME_JPATH)
            .isEqualTo(order.bookName());
        assertThat(jsonContent).extractingJsonPathNumberValue(ORDER_BOOK_PRICE_JPATH)
            .isEqualTo(order.bookPrice());
        assertThat(jsonContent).extractingJsonPathNumberValue(ORDER_BOOK_QUANTITY_JPATH)
            .isEqualTo(order.quantity());
        assertThat(jsonContent).extractingJsonPathStringValue(ORDER_STATUS_JPATH)
            .isEqualTo(order.status().toString());
        assertThat(jsonContent).extractingJsonPathStringValue(ORDER_CREATED_DATE_JPATH)
            .isEqualTo(order.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue(ORDER_LAST_MODIFIED_DATE_JPATH)
            .isEqualTo(order.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue(ORDER_RECORD_VERSION_JPATH)
            .isEqualTo(order.version());
    }

    @Test
    @DisplayName("Verify we can deserialize from JSON.")
    void testDeserialize() throws Exception {
        var instantNow = Instant.now();
        var instantNowStr = instantNow.toString();
        var content = """
            {
            	"id": 394,
            	"bookIsbn": "1234567890",
            	"bookName": "Book Name",
            	"bookPrice": 9.9,
            	"quantity": 1,
            	"status": "ACCEPTED",
            	"createdDate": "%s",
            	"lastModifiedDate": "%s",
            	"version": 21
            }
            """.formatted(instantNowStr, instantNowStr);

        assertThat(json.parse(content))
            .usingRecursiveComparison()
            .isEqualTo(new Order(ORDER_ID, BOOK_ISBN, BOOK_NAME, BOOK_PRICE, BOOK_ORDERED_QUANTITY, OrderStatus.ACCEPTED, instantNow, instantNow, ORDER_RECORD_VERSION));
    }
}
