package net.jsrois.api.repositories

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table()
data class Product(
        val name: String,
        @Id
        val id: UUID
)
