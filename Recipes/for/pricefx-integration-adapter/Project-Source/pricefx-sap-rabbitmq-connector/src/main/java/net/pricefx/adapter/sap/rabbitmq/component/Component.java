package net.pricefx.adapter.sap.rabbitmq.component;

import net.pricefx.adapter.sap.rabbitmq.util.CredentialsOperation;
import org.apache.camel.CamelContext;
import org.apache.camel.component.rabbitmq.RabbitMQComponent;
import org.apache.camel.component.rabbitmq.RabbitMQEndpoint;
import org.apache.camel.spi.UriParam;

import java.util.Map;

import static net.pricefx.adapter.sap.rabbitmq.util.ConnectionUtil.resolveCNAME;

public class Component extends RabbitMQComponent {


    @UriParam
    private String queue;

    @UriParam
    private String credentials;


    public Component() {
        super();
        init();
    }

    public Component(CamelContext context) {
        super(context);
        init();
    }

    private void init(){
        setDeclare(false);
        setSslProtocol("TLSv1.2");
    }

    @Override
    protected RabbitMQEndpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception {

        setProperties(this, parameters);

        RabbitMQEndpoint endpoint = createEndpoint(uri, parameters);

        endpoint.setQueue(queue);

        return endpoint;
    }

    private Endpoint createEndpoint(String uri, Map<String, Object> params) throws Exception {
        Endpoint endpoint = new Endpoint(uri, this);
        String resolvedHost = resolveCNAME(this.getHostname());

        endpoint.setHostname(resolvedHost);
        endpoint.setPortNumber(this.getPortNumber());

        CredentialsOperation operation = new CredentialsOperation(credentials);
        endpoint.setUsername(operation.getUsername());
        endpoint.setPassword(new String(operation.getPassword()));

        endpoint.setVhost(this.getVhost());
        endpoint.setSslProtocol(this.getSslProtocol());
        endpoint.setDeclare(this.isDeclare());

        setProperties(endpoint, params);

        return endpoint;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}
