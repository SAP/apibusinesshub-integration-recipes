package net.pricefx.adapter.sap.component;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.UriEndpointComponent;

import java.util.Map;

public class Component extends UriEndpointComponent {
    public Component() {
        super(Endpoint.class);
    }

    public Component(CamelContext context) {
        super(context, Endpoint.class);
    }

    protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new Endpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }

}
