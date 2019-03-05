package org.chronopolis.bridge

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.chronopolis.bridge.config.ConfigDto
import org.junit.jupiter.api.Test
import java.io.File

class YamlTest {

    @Test
    fun testLoad() {
        val input = File("/narahomes/shake/git/aqueduct/restore.yaml")

        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())

        val dto = mapper.readValue(input.bufferedReader(), ConfigDto::class.java)
        println("db.url: ${dto.db.url()}")
        println("db.username: ${dto.db.username()}")
        println("db.password: ${dto.db.password()}")
    }
}

