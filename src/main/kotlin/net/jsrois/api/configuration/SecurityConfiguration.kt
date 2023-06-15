package net.jsrois.api.configuration

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey


@Configuration
class SecurityConfiguration {
    @Value("\${jwt.public.key}")
    var key: RSAPublicKey? = null

    @Value("\${jwt.private.key}")
    var priv: RSAPrivateKey? = null

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .authorizeHttpRequests { authorize ->
                    authorize
                            .requestMatchers(HttpMethod.GET, "/api/products").anonymous()
                            .requestMatchers(HttpMethod.POST, "/api/auth/login").anonymous()
                            .requestMatchers(HttpMethod.POST, "/api/products").hasRole("MANAGER")
                            .anyRequest().authenticated()
                }
                .csrf { csrf -> csrf.disable() }
                .httpBasic(withDefaults())
                .oauth2ResourceServer { oauth2ResourceServer ->
                    oauth2ResourceServer.jwt { jwt -> jwt.decoder(jwtDecoder()) }
                }
                .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .exceptionHandling { exceptions ->
                    exceptions
                            .authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                            .accessDeniedHandler(BearerTokenAccessDeniedHandler())
                }
        // @formatter:on
        return http.build()
    }

    @Bean
    fun users(): UserDetailsService {
        // @formatter:off
        return InMemoryUserDetailsManager(
                User.withUsername("user")
                        .password("{noop}password")
                        .roles("MANAGER")
                        .authorities("app")
                        .build()
        )
        // @formatter:on
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(key).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(key).privateKey(priv).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }
}