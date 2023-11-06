/*----------------------------------------------------------------------------*/
/* Source File:   BOOKHTTPCLIENT.JAVA                                         */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.infrastructure.http.client;

import com.polarbookshop.orderservice.domain.book.Book;
import java.time.Duration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * Defines HTTP Client to communicate to {@code Catalog Service}.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Component
public class BookHttpClient {
    private static final int THREE_SECONDS = 3;
    private static final int THREE_TIMES = 3;
    private static final int HUNDRED_MILLIS = 100;

    private static final String BOOKS_ROOT_API = "/api/v1/books/";

    private final WebClient webClient;

    /**
     * Constructor with parameters.
     *
     * @param webClient Reference that Spring Framework builds in its Dependency Container.
     */
    public BookHttpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Calls Catalog Servie Endpoint {@code GET /api/v1/books/{isbn}}
     *
     * @param isbn Indicates the Unique book identifier to look for.
     * @return Requested information for the book associated with the {@code isbn}.
     * @see Book
     */
    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
            .get()
            .uri(BOOKS_ROOT_API + isbn)
            .retrieve()
            .bodyToMono(Book.class)
            .timeout(Duration.ofSeconds(THREE_SECONDS), Mono.empty())
            .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty())
            .retryWhen(Retry.backoff(THREE_TIMES, Duration.ofMillis(HUNDRED_MILLIS)))
            .onErrorResume(Exception.class, exception -> Mono.empty());
    }
}
