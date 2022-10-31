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

import cchet.app.microservice.store.warehouse.products.application.ProductCommandHandler;
import cchet.app.microservice.store.warehouse.products.application.ProductQuery;
import cchet.app.microservice.store.warehouse.products.application.Type;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.security.Authenticated;
import io.smallrye.common.constraint.NotNull;

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
    public List<ProductJson> list() {
        return query.list().stream().map(ProductJson::new).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public ProductJson find(@NotEmpty @PathParam("id") final String id) {
        return query.findById(id)
                .map(ProductJson::new)
                .orElseThrow(() -> new NotFoundException("No Product found for id=" + id));
    }

    @POST
    @Path("/search/id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public List<ProductJson> findByIds(@NotEmpty final List<String> ids) {
        return query.findByIds(ids)
                .stream()
                .map(ProductJson::new)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/search/type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public List<ProductJson> findForType(@PathParam("type") @NotNull final Type type) {
        return query.findByType(type).stream()
                .map(ProductJson::new)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/pull")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public void pull(@NotEmpty final Map<String, Integer> idWIthCount) {
        commandHandler.pull(idWIthCount);
    }

    @POST
    @Path("/push/{id}/{count}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public ProductJson push(@NotEmpty @PathParam("id") final String id,
            @NotNull @Min(1) @PathParam("count") final Integer count) {
        return commandHandler.push(id, count)
                .map(ProductJson::new)
                .orElseThrow(() -> new NotFoundException("No Product found for id=" + id));
    }
}
