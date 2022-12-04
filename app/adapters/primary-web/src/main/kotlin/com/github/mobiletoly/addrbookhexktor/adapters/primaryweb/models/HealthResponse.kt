/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.github.mobiletoly.addrbookhexktor.adapters.primaryweb.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Contextual

/**
 * Organization info item
 *
 * @param status overall system health
 * @param version build version
 * @param remoteService address book remote service health
 * @param database database health
 */
@Serializable
data class HealthResponse (

    /* overall system health */
    @SerialName(value = "status")
    val status: kotlin.String,

    /* build version */
    @SerialName(value = "version")
    val version: kotlin.String,

    /* address book remote service health */
    @SerialName(value = "remoteService")
    val remoteService: kotlin.String? = null,

    /* database health */
    @SerialName(value = "database")
    val database: kotlin.String? = null
)
