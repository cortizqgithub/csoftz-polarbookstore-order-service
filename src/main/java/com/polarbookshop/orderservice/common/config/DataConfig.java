/*----------------------------------------------------------------------------*/
/* Source File:   DATACONFIG.JAVA                                             */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

/**
 * Spring Data R2DBC configuration.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
@EnableR2dbcAuditing
public class DataConfig {
}
