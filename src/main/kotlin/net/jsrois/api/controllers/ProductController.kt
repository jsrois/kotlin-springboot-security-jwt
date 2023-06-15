package net.jsrois.api.controllers

import net.jsrois.api.repositories.Product
import net.jsrois.api.repositories.ProductRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val productRepository: ProductRepository) {
    @GetMapping("/api/products")
    fun allProducts() = productRepository.findAll()


    @PostMapping("/api/products")
    fun addProduct(@RequestBody product: Product) = productRepository.save(product)
}