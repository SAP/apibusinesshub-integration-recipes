import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.Map
import java.util.Iterator
import javax.activation.DataHandler
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.BodyPart;
import org.apache.commons.codec.binary.Base64;
import org.apache.camel.impl.DefaultAttachment;
import groovy.xml.XmlUtil;
import java.nio.charset.StandardCharsets;

def Message processData(Message message) {
    //Body 
   def messageLog = messageLogFactory.getMessageLog(message);
   def body = message.getBody(java.lang.String) as String;
   def bodyxml = new XmlSlurper()
   
   bodyxml.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false) 
   bodyxml.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
   
   Map<String, DataHandler> attachments = message.getAttachments()
   HashMap<String,String> Base64Hashmap=new HashMap<String,String>(); 
   Iterator<String> keys;
   def finalcontent;

   int count
   
   if (attachments.isEmpty()) {
      // Handling of missing attachment goes here
        messageLog.setStringProperty("Logging#CheckAttachment", "No attachment")
   } else {
       
        messageLog.setStringProperty("Logging#CheckAttachment", "attachment")
        messageLog.setStringProperty("Logging#No.of Invoices:", String.valueOf(attachments.size()));
         
      Iterator<DataHandler> iterator = attachments.values().iterator()
      
      int cxml = 0;
      while (iterator.hasNext()) 
      {
        DataHandler datahandler = iterator.next()
        
        String contentType = datahandler.getContentType()
        String parentcid = contentType.substring(contentType.indexOf("start=") + "start=".length(), contentType.indexOf("type="))
        parentcid = parentcid.substring(1, parentcid.length()-3);
        
        messageLog.setStringProperty("Logging#Nparentcid:" + 1, String.valueOf(parentcid));
        
        MimeMultipart multipart = (MimeMultipart) datahandler.getContent();
        count = multipart.getCount();
        messageLog.setStringProperty("Logging#No.of Attachment:", String.valueOf(count));
        
        BodyPart bodyPart = multipart.getBodyPart(parentcid); 
        
        // Get Inputstream for each attachment
        ByteArrayInputStream bai = (ByteArrayInputStream)bodyPart.getInputStream();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];

        for (;;) 
        {
            int nread = bai.read(buf, 0, buf.length);
            if (nread <= 0) {
                break; }

        baos.write(buf, 0, nread);
        }
        
        // Convert data to Base64 String and prepare HashMap
        byte[] bytepayload = baos.toByteArray();
        baos.flush();
        baos.close();
        bai.close();
        
        def xmlcontent = bodyxml.parseText(new String(bytepayload, "utf-8"));
        
        String ReplaceComment;
        String ReplaceAttachment;
        
        if( xmlcontent.Request.InvoiceDetailRequest.InvoiceDetailRequestHeader.Comments.toString().toString().indexOf("]") > -1)
        {
        ReplaceComment = '<Comment>' + xmlcontent.Request.InvoiceDetailRequest.InvoiceDetailRequestHeader.Comments.toString().substring(0,xmlcontent.Request.InvoiceDetailRequest.InvoiceDetailRequestHeader.Comments.toString().indexOf("]")) + '</Comment>'
        
        ReplaceAttachment = '<Attachments>';
        }
        
        xmlcontent.'**'.findAll { it.name() == 'Attachment' }.each {
           node ->
           
           String cid = String.valueOf(node);
           cid = "<" + cid.substring(contentType.indexOf("cid:") + "cid:".length() + 1) + ">";
           
          bodyPart = multipart.getBodyPart(cid); 
        
        // Get Inputstream for each attachment
        bai = (ByteArrayInputStream)bodyPart.getInputStream();
	    baos = new ByteArrayOutputStream();
        buf = new byte[8192];

        for (;;) 
        {
            int nread = bai.read(buf, 0, buf.length);
            if (nread <= 0) {
                break; }

        baos.write(buf, 0, nread);
        }
        
        // Convert data to Base64 String and prepare HashMap
        bytepayload = baos.toByteArray();
        baos.flush();
        baos.close();
        bai.close();
        
           String encodedBytes = Base64.encodeBase64String(bytepayload);
           //messageLog.addAttachmentAsString(cid, String.valueOf(encodedBytes), "text/plain") 
           
           node.replaceBody(String.valueOf(encodedBytes));
           
           def parent = node.parent()
           
           if(parent.@name != 'invoicePDF')
           {
           ReplaceAttachment = ReplaceAttachment + '<Attachment>' + node + '</Attachment>'
           }
        }
        
        if( ReplaceComment != null )
        {
        def xmlcontent1 = bodyxml.parseText(ReplaceComment);
        xmlcontent.Request.InvoiceDetailRequest.InvoiceDetailRequestHeader.Comments.replaceBody(xmlcontent1)
        }
        
        if( ReplaceAttachment != null )
        {
        ReplaceAttachment = ReplaceAttachment + '</Attachments>';
        def xmlcontent2 = bodyxml.parseText(ReplaceAttachment);
        xmlcontent.Request.InvoiceDetailRequest.InvoiceDetailRequestHeader.Comments.appendNode(xmlcontent2)
        }
        
        //xmlcontent.Request.InvoiceDetailRequest.InvoiceDetailRequestHeader.Extrinsic[3].replaceBody("")
        
        if (finalcontent == null)
        {
         finalcontent = bodyxml.parseText("<MultiCxmlMessage></MultiCxmlMessage>")
         //finalcontent = xmlcontent;
        }
        finalcontent.appendNode(xmlcontent);
        cxml++;
      }
      
// Seralize replaced data and update message body
       body = XmlUtil.serialize(finalcontent)
       //messageLog.addAttachmentAsString("finalcontent:", String.valueOf(body), "text/plain") 
       message.setBody(body)
   }
   return message

}