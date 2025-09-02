package com.narvi.timelineserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SnsTimelineServerApplication

fun main(args: Array<String>) {
    runApplication<SnsTimelineServerApplication>(*args)
}
