/*----------------------------------------------------------------------------*/
/* Source File:   TESTCONTAINERCONSTANTS.JAVA                                 */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.orderservice.common.consts;

/**
 * Defines common TestContainer Constants.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class TestContainerConstants {

    public static String POSTGRESQL_DOCKER_VERSION = "postgres:16.0";

    /**
     * This is a utility class thus we must avoid to instantiate this.
     */
    private TestContainerConstants() {
    }
}
