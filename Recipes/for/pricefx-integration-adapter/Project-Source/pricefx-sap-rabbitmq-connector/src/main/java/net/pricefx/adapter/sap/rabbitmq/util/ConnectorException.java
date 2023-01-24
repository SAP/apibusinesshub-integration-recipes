package net.pricefx.adapter.sap.rabbitmq.util;

public class ConnectorException extends RuntimeException {

    public ConnectorException(String message) {
        super(message);
    }

    public ConnectorException(String message, Exception e) {
        super(message, e);
    }


}
