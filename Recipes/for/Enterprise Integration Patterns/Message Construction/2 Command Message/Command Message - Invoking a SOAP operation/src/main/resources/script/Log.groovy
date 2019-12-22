import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    def messageLog = messageLogFactory.getMessageLog(message)
    messageLog.addAttachmentAsString('Temperature in Celsius', message.getBody(String), 'application/xml')
    
    return message
}