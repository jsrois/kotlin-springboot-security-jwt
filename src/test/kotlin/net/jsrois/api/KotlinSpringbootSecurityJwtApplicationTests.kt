package net.jsrois.api

import net.jsrois.api.repositories.Product
import net.jsrois.api.repositories.ProductRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
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

}
