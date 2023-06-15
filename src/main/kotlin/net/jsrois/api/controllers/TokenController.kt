package net.jsrois.api.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TokenController {
    @GetMapping("/api/login")
    fun login(loginRequest: LoginRequest): Unit = TODO()
}