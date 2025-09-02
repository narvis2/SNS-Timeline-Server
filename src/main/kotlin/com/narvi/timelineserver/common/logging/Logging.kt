package com.narvi.timelineserver.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Logging {
    fun <T : Any> getLogger(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

    fun <T> logFor(log: Logger, isError: Boolean = false, function: (MutableMap<String, Any?>) -> T?): T {
        val logInfo = mutableMapOf<String, Any?>()
        logInfo["start_at"] = now()

        val result = function.invoke(logInfo)

        logInfo["end_at"] = now()

        if (isError) {
            log.error("⚠️ $logInfo")
        } else {
            log.info("📌 $logInfo")
        }

        return result ?: throw RuntimeException("failed to invoke in logger")
    }

    private fun now(): Long = System.currentTimeMillis()
}