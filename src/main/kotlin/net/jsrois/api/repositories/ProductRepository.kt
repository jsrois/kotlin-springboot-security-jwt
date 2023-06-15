package net.jsrois.api.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepository : CrudRepository<Product, UUID>