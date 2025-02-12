package com.loggily.loggily

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

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
}