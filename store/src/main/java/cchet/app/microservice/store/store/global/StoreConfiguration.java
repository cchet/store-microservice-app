package cchet.app.microservice.store.store.global;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "store")
public interface StoreConfiguration {
    
    String welcomeTitleLogged();

    String welcomeTitleNotLogged();
}
