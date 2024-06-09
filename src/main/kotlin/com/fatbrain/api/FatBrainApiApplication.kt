package com.fatbrain.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = [
    "com.fatbrain.api.config.properties",
])
class FatBrainApiApplication

fun main(args: Array<String>) {
    runApplication<FatBrainApiApplication>(*args)
}
