import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory; 
import com.sap.it.api.securestore.SecureStoreService; 
import com.sap.it.api.securestore.UserCredential; 
import java.util.Random;
import groovy.xml.XmlUtil;

def Message processData(Message message) 
{
    def bodyxml = new XmlSlurper();
    bodyxml.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false) 
    bodyxml.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    
    def property = message.getProperty('Credentials');
    def body = message.getBody(java.lang.String) as String;
    def service = ITApiFactory.getApi(SecureStoreService.class, null); 
    def credential = service.getUserCredential(property);
    
    Random random = new Random();
    
    if (credential == null)
    { 
        throw new IllegalStateException("No credential found for alias " + property);  
    } 
    else
    {
        String user = credential.getUsername(); 
        String password = new String(credential.getPassword())
        
        def payloadID = random.nextInt() / -1;
        def xmlcontent = bodyxml.parseText(new String(body));     
        
        //Replace Credentials & PayloadID
        xmlcontent.@payloadID = String.valueOf(payloadID);
        xmlcontent.Header.From.Credential[0].Identity.replaceBody(user);
        xmlcontent.Header.Sender.Credential.Identity.replaceBody(user);
        xmlcontent.Header.Sender.Credential.SharedSecret.replaceBody(password);
        
        message.setProperty("username", user);
        message.setProperty("password", password);
        message.setProperty("payloadID", payloadID);
        
        body = XmlUtil.serialize(xmlcontent)
        message.setBody(body)
    }
    return message;
}