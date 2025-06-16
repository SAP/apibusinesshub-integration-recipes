package net.pricefx.adapter.sap.component;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultConsumer;


public class Consumer extends DefaultConsumer {

    public Consumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        Exchange ex = getEndpoint().createExchange(ExchangePattern.InOut);
        ex.getMessage().setBody("Testing Only");

    }


}
