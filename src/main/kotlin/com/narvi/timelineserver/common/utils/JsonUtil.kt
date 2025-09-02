package com.narvi.timelineserver.common.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.narvi.timelineserver.common.logging.Logging
import org.springframework.stereotype.Component

@Component
class JsonUtil {

    private val logger = Logging.getLogger(JsonUtil::class.java)

    private val objectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModules(Jdk8Module(), JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // 사람이 읽을 수 있는 날짜 형식으로 변환

    // JSON 문자열 → 객체
    fun <T> fromJson(json: String, clazz: Class<T>): T? = Logging.logFor(logger) { log ->
        runCatching {
            objectMapper.readValue(json, clazz)
        }.getOrElse {
            log["Failed JSON to Object"] = it.message ?: "json parse error"
            it.printStackTrace()
            null
        }
    }


    fun <T> fromJsonToList(json: String, clazz: Class<T>): List<T> = Logging.logFor(logger) { log ->
        runCatching {
//        val listType: CollectionType = objectMapper.typeFactory.constructCollectionType(MutableList::class.java, clazz)
//        return objectMapper.readValue(json, listType)
            objectMapper.readerForListOf(clazz).readValue<List<T>>(json)
        }.getOrElse {
            log["Failed JSON to List<Object>:"] = "${it.message}"
            it.printStackTrace()
            emptyList()
        }
    }

    // 객체 → JSON 문자열
    fun <T> toJson(obj: T): String? = Logging.logFor(logger) { log ->
        runCatching {
            objectMapper.writeValueAsString(obj)
        }.getOrElse {
            log["Failed Object to JSON"] = "${it.message}"
            null
        }
    }

    fun addValue(json: String, key: String, value: String): String? = Logging.logFor(logger) { log ->
        runCatching {
            val node = objectMapper.readTree(json) as ObjectNode
            node.put(key, value)

            objectMapper.writeValueAsString(node)
        }.getOrElse {
            null
        }
    }
}