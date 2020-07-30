import com.sap.gateway.ip.core.customdev.util.Message
import javax.ws.rs.core.MediaType

def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message)
    messageLog.addAttachmentAsString('Material', message.getBody(String), MediaType.APPLICATION_XML)
    
    return message
}