package net.pricefx.adapter.sap.component;

import org.apache.camel.support.DefaultComponent;

import java.util.Map;

public class Component extends DefaultComponent {
    protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new Endpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }

}
