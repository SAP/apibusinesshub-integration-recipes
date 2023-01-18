package net.pricefx.adapter.sap.rabbitmq.util;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import java.util.Properties;

public class ConnectionUtil {

    private ConnectionUtil(){}

    public static String resolveCNAME(String hostName)  {
        try {
            String[] hostNameParts = hostName.split(":");
            Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            InitialDirContext idc = new InitialDirContext(env);
            javax.naming.directory.Attributes attrs = idc.getAttributes(hostNameParts[0], new String[]{"CNAME"});
            Attribute attr = attrs.get("CNAME");
            if(attr == null) {
                return hostName;
            }

            String cnamedVal = attr.get().toString();
            if(cnamedVal.endsWith(".")) {
                cnamedVal = cnamedVal.substring(0,cnamedVal.length()-1);
            }
            if(hostNameParts.length > 1)
                return String.format("%s:%s",cnamedVal,hostNameParts[1]);
            else
                return cnamedVal;
        } catch (NamingException e) {
            return hostName;
        }
        
    }

}
