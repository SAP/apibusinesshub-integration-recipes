import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message)
    messageLog.addAttachmentAsString('Response', message.getBody(String), null)
    
    return message
}