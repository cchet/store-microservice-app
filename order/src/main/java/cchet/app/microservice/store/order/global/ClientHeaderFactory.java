package cchet.app.microservice.store.order.global;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

public class ClientHeaderFactory implements ClientHeadersFactory {

    @ConfigProperty(name = "quarkus.application.name")
    String applicationName;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> clientOutgoingHeaders) {
        clientOutgoingHeaders.put("X-REQUESTER", List.of(applicationName));
        return clientOutgoingHeaders;
    }

}
