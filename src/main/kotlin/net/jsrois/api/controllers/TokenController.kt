package net.jsrois.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.stream.Collectors


@RestController
class TokenController {

    @Autowired
    private lateinit var encoder: JwtEncoder

    @PostMapping("/api/auth/login")
    fun login(authentication: Authentication): LoginResponse {
        val now = Instant.now()
        val expiry = 36000L

        val scope = authentication.authorities.stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(" "))
        val claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.name)
                .claim("scope", scope)
                .build()

        return LoginResponse(token = encoder.encode(JwtEncoderParameters.from(claims)).tokenValue)
    }
}