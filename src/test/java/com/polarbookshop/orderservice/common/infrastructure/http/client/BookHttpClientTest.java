/*----------------------------------------------------------------------------*/
/* Source File:   BOOKHTTPCLIENTTESTS.JAVA                                    */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.infrastructure.http.client;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

/**
 * Unit tests for BookHttpClient component.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
class BookHttpClientTest {
    private static final int EXPECTED_NEXT_COUNT = 0;

    private static final String BOOK_AVI_V1_PATH = "/api/v1/books/";
    private static final String ISBN_EXIST = "1234567890";
    private static final String ISBN_NOT_EXIST = "1234567891";
    private static final String BOOK_RESPONSE = """
        	{
        		"isbn": %s,
        		"title": "Title",
        		"author": "Author",
        		"price": 9.90,
        		"publisher": "Polarsophia"
        	}
        """;

    private MockWebServer mockWebServer;
    private BookHttpClient bookHttpClient;

    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        var webClient = WebClient.builder()
            .baseUrl(mockWebServer.url(BOOK_AVI_V1_PATH).uri().toString())
            .build();
        this.bookHttpClient = new BookHttpClient(webClient);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Verify when Book exists then return book.")
    void whenBookExistsThenReturnBook() {
        var bookIsbn = ISBN_EXIST;

        var mockResponse = new MockResponse()
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(BOOK_RESPONSE.formatted(bookIsbn));

        mockWebServer.enqueue(mockResponse);

        var book = bookHttpClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(book)
            .expectNextMatches(b -> b.isbn().equals(bookIsbn))
            .verifyComplete();
    }

    @Test
    @DisplayName("Verify when Book does not exist then return empty.")
    void whenBookNotExistsThenReturnEmpty() {
        var bookIsbn = ISBN_NOT_EXIST;

        var mockResponse = new MockResponse()
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setResponseCode(HttpStatus.NOT_FOUND.value());

        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(bookHttpClient.getBookByIsbn(bookIsbn))
            .expectNextCount(EXPECTED_NEXT_COUNT)
            .verifyComplete();
    }
}