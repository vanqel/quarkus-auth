package io.diplom.cart.repository

import io.diplom.cart.model.CartEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CartPanacheRepository : PanacheRepository<CartEntity>
