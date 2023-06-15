package net.jsrois.api.controllers

import net.jsrois.api.repositories.ProductRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val productRepository: ProductRepository) {
    @GetMapping("/api/products")
    fun allProducts() = productRepository.findAll()
}