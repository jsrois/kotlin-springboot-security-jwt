package net.jsrois.api

import net.jsrois.api.controllers.LoginRequest
import net.jsrois.api.controllers.LoginResponse
import net.jsrois.api.repositories.Product
import net.jsrois.api.repositories.ProductRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.*

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ApiTest {

    @Autowired
    private lateinit var api: TestRestTemplate

    @Autowired
    private lateinit var repository: ProductRepository

    @Test
    fun `returns existing products`() {

        val products = repository.saveAll(listOf(
                Product(id = UUID.randomUUID(), name = "tootbrush"),
                Product(id = UUID.randomUUID(), name = "toothpaste")
        ))

        val response = api.getForEntity("/api/products", Array<Product>::class.java)

        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        assertThat(response.body?.toList(), equalTo(products))

    }

    @Test
    fun `allows authenticated users to create new products`() {
        val product = Product(id = UUID.randomUUID(), name = "ball")

        val request = LoginRequest("user", "password")
        val loginResponse = api.postForEntity("/api/auth/login", request, LoginResponse::class.java)
        val token = loginResponse.body?.token

        assertThat(loginResponse.statusCode, equalTo(HttpStatus.OK))
        assertThat(token, notNullValue())

        val newProductRequest = HttpEntity<Product>(
                product,
                HttpHeaders()
                        .apply {
                            contentType = MediaType.APPLICATION_JSON
                            setBearerAuth(token!!)
                        })

        val response = api.postForEntity("/api/products", newProductRequest, LoginResponse::class.java)
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
    }


}


