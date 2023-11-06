/*----------------------------------------------------------------------------*/
/* Source File:   CLIENTCONFIG.JAVA                                           */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.config;

import com.polarbookshop.orderservice.common.config.properties.PolarProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring WebClient configuration beans.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
public class ClientConfig {

    /**
     * Conigures an HTTP Web Client to communicate to {@code Catalog Service} endpoints.
     *
     * @param polarProperties  Includes the Polar properties.
     * @param webClientBuilder Includes a reference to the configured Web Client Instance.
     * @return WebClient to communicate to {@code Catalog Service} via HTTP.
     */
    @Bean
    public WebClient webClient(PolarProperties polarProperties, WebClient.Builder webClientBuilder) {
        return webClientBuilder
            .baseUrl(polarProperties.catalogServiceUri().toString())
            .build();
    }
}
