package io.diplom.works.api.http

import io.diplom.works.dto.inp.AttachImagesWorkDTO
import io.diplom.works.dto.inp.ChangeStatusWorkDTO
import io.diplom.works.dto.inp.CreateWorkDTO
import io.diplom.works.models.WorkEntity
import io.diplom.works.usecase.WorkCreateUsecase
import io.diplom.works.usecase.WorkStatusUsecase
import io.quarkus.vertx.web.Body
import io.quarkus.vertx.web.Param
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import io.quarkus.vertx.web.RoutingExchange
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@RouteBase(path = "/work")
class WorksApi(
    private val workCreateUsecase: WorkCreateUsecase,
    private val workStatusUsecase: WorkStatusUsecase
) {
    @Route(
        path = "create",
        methods = [Route.HttpMethod.POST],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun create(
        @Body input: CreateWorkDTO
    ): Uni<WorkEntity> = workCreateUsecase.create(input)

    @Route(
        path = "images",
        methods = [Route.HttpMethod.POST],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun attachImages(
        @Param("id") id: Long?,
        ex: RoutingExchange
    ): Uni<WorkEntity> = workCreateUsecase
        .attachImages(AttachImagesWorkDTO(ex.context().fileUploads(), id!!))


    @Route(
        path = "change-state",
        methods = [Route.HttpMethod.PUT],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun changeState(
        @Body input: ChangeStatusWorkDTO
    ): Uni<WorkEntity> = workStatusUsecase.changeStatus(input)


}
