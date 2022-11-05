package cchet.app.microservice.store.warehouse.products;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import cchet.app.microservice.store.warehouse.products.application.Product;
import cchet.app.microservice.store.warehouse.products.application.ProductCommandHandler;
import cchet.app.microservice.store.warehouse.products.application.ProductQuery;
import cchet.app.microservice.store.warehouse.products.application.Type;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.security.Authenticated;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;

@RequestScoped
@Path("/product")
@Authenticated
@SecurityRequirement(name = "Keycloak")
public class ProductResource {

    @Inject
    ProductQuery query;

    @Inject
    ProductCommandHandler commandHandler;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<List<ProductJson>> list() {
        return query.list().onItem().transform(this::transfromProductToProductJson);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<ProductJson> find(@NotEmpty @PathParam("id") final String id) {
        return query.findById(id)
                .onFailure().transform(e -> new NotFoundException("No Product found for id=" + id, e))
                .onItem().transform(ProductJson::new);
    }

    @POST
    @Path("/search/id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<List<ProductJson>> findByIds(@NotEmpty final List<String> ids) {
        return query.findByIds(ids)
                .onItem().transform(this::transfromProductToProductJson);
    }

    @GET
    @Path("/search/type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<List<ProductJson>> findForType(@PathParam("type") @NotNull final Type type) {
        return query.findByType(type)
                .onItem().transform(this::transfromProductToProductJson);
    }

    @POST
    @Path("/pull")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<List<ProductJson>> pull(@NotEmpty final Map<String, Integer> idWIthCount) {
        return commandHandler.pull(idWIthCount)
                .onFailure(IllegalStateException.class).transform(e -> new NotFoundException("No entries found", e))
                .onItem().transform(this::transfromProductToProductJson);
    }

    @POST
    @Path("/push/{id}/{count}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<ProductJson> push(@NotEmpty @PathParam("id") final String id,
            @NotNull @Min(1) @PathParam("count") final Integer count) {
        return commandHandler.push(id, count)
                .onFailure().transform(e -> new NotFoundException("No Product found for id=" + id, e))
                .onItem().transform(ProductJson::new);
    }

    private List<ProductJson> transfromProductToProductJson(final List<Product> produts) {
        return produts.stream().map(ProductJson::new).collect(Collectors.toList());
    }
}
