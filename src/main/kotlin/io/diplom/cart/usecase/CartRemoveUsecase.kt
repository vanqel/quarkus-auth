package io.diplom.cart.usecase

import io.diplom.cart.model.CartEntity
import io.diplom.cart.repository.CartPanacheRepository
import io.diplom.common.security.configurator.getUser
import io.diplom.exception.AuthException
import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CartRemoveUsecase(
    private val cartRepository: CartPanacheRepository,
    private val cartFetchUsecase: CartFetchUsecase,
    private val securityIdentity: SecurityIdentity,
) {

    fun delete(id: Long): Uni<List<CartEntity>> {
        return cartRepository.findById(id).flatMap { entity ->
            if (entity.userId != securityIdentity.getUser().id ||
                entity.status == CartEntity.Status.PAYMENT
            ) Uni.createFrom().failure { AuthException() }
            else cartRepository.delete(entity)
        }.flatMap {
            cartFetchUsecase.findForUserCart()
        }
    }

}
