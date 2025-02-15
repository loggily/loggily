package com.loggily.loggily

import com.loggily.loggily.rest.ReadableLog
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.function.Function

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Import(TestcontainersConfiguration::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggilyEndToEndTest {
    @LocalServerPort
    private val serverPort: Int = 0
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:$serverPort").build()
    }

    @Order(1)
    @Test
    fun `it should be possible to post logs`() {

        val payload = ClassPathResource("otel-payload.json").file.readText()

        webTestClient.post().uri("/v1/logs")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .exchange().expectStatus().isCreated
    }

    @Order(2)
    @Test
    fun `it should be possible to search environment names`() {
        val result = webTestClient.get().uri("/api/dashboard/environments/name?contains=vice")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<String>::class.java)
            .returnResult().responseBody

        assertThat(result).containsExactly("animal-services-dev", "birds-services-dev", "demo-services-dev")
    }

    @Order(2)
    @Test
    fun `it should be possible to find unique and alphabetically sorted application names for an environment name`() {
        val environmentName = "birds-services-dev"
        val result = webTestClient.get()
            .uri {
                it.path("/api/dashboard/applications/name")
                    .queryParam("environmentName", environmentName)
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<String>::class.java)
            .returnResult().responseBody

        assertThat(result).containsExactly("crow", "peacock")
    }

    @Order(2)
    @Test
    fun `it should be possible to find unique and alphabetically sorted host names for an application in an environment`() {
        val environmentName = "birds-services-dev"
        val applicationName = "crow"
        val result = webTestClient.get()
            .uri {
                it.path("/api/dashboard/hosts/name")
                    .queryParam("environmentName", environmentName)
                    .queryParam("applicationName", applicationName)
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<String>::class.java)
            .returnResult().responseBody

        assertThat(result).containsExactly("crow-7f87799957-gwhsq")
    }

    @Order(2)
    @Test
    fun `it should be possible to find logs by environment and application`() {
        val environmentName = "birds-services-dev"
        val applicationName = "peacock"
        val result = webTestClient.get()
            .uri {
                it.path("/api/dashboard/logs")
                    .queryParam("environmentName", environmentName)
                    .queryParam("applicationName", applicationName)
                    .build()
            }
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<ReadableLog>::class.java)
            .returnResult().responseBody

        assertThat(result).extracting(Function { it.structuredLog.body }).containsExactly("1", "2", "3", "4")
    }

    @Order(2)
    @Test
    fun `it should be possible to find logs by environment, application and host`() {
        val environmentName = "birds-services-dev"
        val applicationName = "peacock"
        val host = "peacock-7f87799957-gwhsq"
        val result = webTestClient.get()
            .uri {
                it.path("/api/dashboard/logs")
                    .queryParam("environmentName", environmentName)
                    .queryParam("applicationName", applicationName)
                    .queryParam("hostName", host)
                    .build()
            }
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<ReadableLog>::class.java)
            .returnResult().responseBody

        assertThat(result).extracting(Function { it.structuredLog.body }).containsExactly("1", "3", "4")
    }

    @Order(2)
    @Test
    fun `it should be possible to find logs by paging over it`() {
        val environmentName = "birds-services-dev"
        val applicationName = "peacock"
        val host = "peacock-7f87799957-gwhsq"

        webTestClient.get()
            .uri {
                it.path("/api/dashboard/logs")
                    .queryParam("environmentName", environmentName)
                    .queryParam("applicationName", applicationName)
                    .queryParam("hostName", host)
                    .queryParam("limit", 2)
                    .queryParam("offset", 0)
                    .build()
            }
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<ReadableLog>::class.java)
            .returnResult().responseBody.also { result ->
                assertThat(result).extracting(Function { it.structuredLog.body }).containsExactly("1", "3")
            }


        webTestClient.get()
            .uri {
                it.path("/api/dashboard/logs")
                    .queryParam("environmentName", environmentName)
                    .queryParam("applicationName", applicationName)
                    .queryParam("hostName", host)
                    .queryParam("limit", 2)
                    .queryParam("offset", 2)
                    .build()
            }
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<ReadableLog>::class.java)
            .returnResult().responseBody.also { result ->
                assertThat(result).extracting(Function { it.structuredLog.body }).containsExactly("4")
            }

        webTestClient.get()
            .uri {
                it.path("/api/dashboard/logs")
                    .queryParam("environmentName", environmentName)
                    .queryParam("applicationName", applicationName)
                    .queryParam("hostName", host)
                    .queryParam("limit", 2)
                    .queryParam("offset", 3)
                    .build()
            }
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<ReadableLog>::class.java)
            .returnResult().responseBody.also { result ->
                assertThat(result).extracting(Function { it.structuredLog.body }).isEmpty()
            }
    }
}