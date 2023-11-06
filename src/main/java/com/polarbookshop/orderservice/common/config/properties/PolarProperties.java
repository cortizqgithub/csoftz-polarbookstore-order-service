/*----------------------------------------------------------------------------*/
/* Source File:   CLIENTPROPERTIES.JAVA                                       */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.config.properties;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Groups properties (key/value).
 * <p>As we use a prefix here, the property key is 'polar.greeting'.</p>
 * <p>Add the {@code org.springframework.boot:spring-boot-configuration-processor} to
 * generate metadata for the properties so the IDE can give you hints.</p>
 *
 * @param catalogServiceUri Config the external service endpoint for <b>Catalog Service</b>.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@ConfigurationProperties(prefix = "polar")
public record PolarProperties(@NotNull URI catalogServiceUri) {
}
