package net.pricefx.adapter.sap.util;

import com.sap.it.api.msglog.adapter.AdapterMessageLog;
import com.sap.it.api.msglog.adapter.AdapterMessageLogFactory;
import com.sap.it.api.msglog.adapter.AdapterTraceMessage;
import com.sap.it.api.msglog.adapter.AdapterTraceMessageType;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;

import java.util.UUID;

public class LogUtil {

    private LogUtil() {
    }

    public static void writeTrace(Endpoint endpoint, Exchange exchange, byte[] traceData) {
        AdapterMessageLogFactory msgLogFactory = (AdapterMessageLogFactory) endpoint.getCamelContext().getRegistry()
                .lookupByName(com.sap.it.api.msglog.adapter.AdapterMessageLogFactory.class.getName());

        AdapterMessageLog mplLog = msgLogFactory.getMessageLog(exchange, "Receiving message", "ctype::Adapter/cname::Pricefx-SAP-Integration-Adapter/vendor::Pricefx/version::1.0.0",
                UUID.randomUUID().toString());

        if (!mplLog.isTraceActive()) {
            return;
        }
        // if you have a fault inbound message then specify AdapterTraceMessageType.RECEIVER_INBOUND_FAULT,
        // if you have a fault outbound message then specify AdapterTraceMessageType.SENDER_OUTBOUND_FAULT
        // for synchronous adapters you may also need AdapterTraceMessageType.SENDER_OUTBOUND and AdapterTraceMessageType.RECEIVER_INBOUND

        AdapterTraceMessage traceMessage = mplLog.createTraceMessage(AdapterTraceMessageType.RECEIVER_OUTBOUND, traceData, false);//Setting isTruncated as false assuming traceData is less than 25MB.
        // Encoding is optional, but should be set if available.
        traceMessage.setEncoding("UTF-8");
        // Headers are optional and do not forget to obfuscate security relevant header values.
        mplLog.writeTrace(traceMessage);

    }

}
