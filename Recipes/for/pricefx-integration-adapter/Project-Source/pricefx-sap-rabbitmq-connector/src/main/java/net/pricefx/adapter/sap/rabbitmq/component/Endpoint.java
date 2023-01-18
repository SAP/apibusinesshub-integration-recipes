package net.pricefx.adapter.sap.rabbitmq.component;

import org.apache.camel.component.rabbitmq.RabbitMQComponent;
import org.apache.camel.component.rabbitmq.RabbitMQEndpoint;
import org.apache.camel.spi.UriEndpoint;


@UriEndpoint(scheme = "pfx-mq-general", syntax = "", title = "")
public class Endpoint extends RabbitMQEndpoint {

    public Endpoint(final String endpointUri, RabbitMQComponent component) {
        super(endpointUri, component);

    }

}
