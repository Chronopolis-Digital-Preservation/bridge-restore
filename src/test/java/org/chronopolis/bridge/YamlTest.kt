package org.chronopolis.bridge

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions
import org.chronopolis.bridge.config.ConfigDto
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class YamlTest {

    private val yamlPath = "src/test/resources/restore.yaml"

    // these are all pulled from the yamlPath
    private val jooqUser = "jooq"
    private val bridgeUser = "bridge"
    private val smtpTo = "unit-test@bridge-restore.umiacs.umd.edu"
    private val storageChronopolis = "/scratch0/testing/restore/chronopolis"

    @Test
    fun testLoad() {
        // use relative path to test resources restore.yml
        val input = File(yamlPath)

        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())

        val dto = mapper.readValue(input.bufferedReader(), ConfigDto::class.java)

        Assertions.assertThat(dto.smtp.to()).isEqualTo(smtpTo)
        Assertions.assertThat(dto.db.username()).isEqualTo(jooqUser)
        Assertions.assertThat(dto.bridge.bridgeUsername()).isEqualTo(bridgeUser)
        Assertions.assertThat(dto.storage.chronopolis()).isEqualTo(Paths.get(storageChronopolis))
    }
}

