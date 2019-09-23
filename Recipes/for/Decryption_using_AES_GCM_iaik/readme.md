# CMS Decryption with AES256-GCM algorithm using iaik libraries

![Shweta Walaskar](https://github.com/swalaskar.png?size=50 )|[Shweta Walaskar](https://github.com/swalaskar)|
----|----|

Standard Decryptor component in CPI doesn't provide an option for Encryption algorithm AES256-GCM. Hence, there was a need to develop this custom decryptor
The examples available in internet use BouncyCastle as security provider and this needs BouncyCastle registration as security provider on CPI tenant, which is not recommended.
Hence we chose to do this using iaik which is the default security provider for CPI

[Download the integration flow Sample](CMS_AES256GCM_Decryption_iaik.zip)\

## Recipe

Step|Code|Why?
----|----|----
Extract enveloped data out of InputStream|```EnvelopedDataStream enveloped_data = new EnvelopedDataStream(is);```|
Retrieve recipient info(RSA public/key certificate) from enveloped data|```RecipientInfo[] recipients = enveloped_data.getRecipientInfos();```|
Extract secret key used for encrypting the data|```SecretKey secretKey = recipients[0].decryptKey(privateSignKey);```|
Get encryption algorithm details|```AlgorithmID contentEA = eci.getContentEncryptionAlgorithm();```|
Get GCM parameters|```params = new GCMParameterSpec(128, (byte[]) oct.getValue());```|
Write encrypted content stream in output stream|```Util.copyStream(data_is, baos, null);```|

### Related Recipes
* [Encryption with AES256-GCM algorithm using iaik libraries](../Encryption_using_AES_GCM_iaik/readme.md)
* [Accessing keystore artifacts using a Groovy script](../AccessTenantKeystoreusingScript/readme.md)

## References
* Blogs
* Specs
* [Galois/Counter Mode](https://en.wikipedia.org/wiki/Galois/Counter_Mode)

## Sample integration flow
![iflowimage](CMS_AES256_GCM_Decryption_iaik.JPG)

This sample integration flow is used to encrypt data with AES256-GCM algorithm using iaik libraries

### Sample Script
This is the script used in the sample
```
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

import com.sap.it.api.ITApiFactory;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import no.difi.asic.*;
import no.difi.asic.extras.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

//Java keystore
import com.sap.it.api.keystore.KeystoreService;
import com.sap.it.api.keystore.exception.KeystoreException;
import java.security.*;
import java.security.cert.*;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;

import iaik.cms.EncryptedContentInfoStream;
import iaik.cms.EnvelopedDataStream;
import iaik.cms.RecipientInfo;
import iaik.asn1.OCTET_STRING;
import iaik.asn1.SEQUENCE;
import iaik.utils.Util;

import com.google.common.io.ByteStreams;

import iaik.asn1.structures.AlgorithmID;

def Message processData(Message message) {

    //Body
    def body = message.getBody(byte[].class);

       def clientSignKeyAlias = "sap_cloudintegrationcertificate";

        def service = ITApiFactory.getApi(KeystoreService.class, null);   
        if( service == null) {
            throw new IllegalStateException("Keystore Store Service is not available.");
        }

        //Get Private Key from the system.jks
        PrivateKey privateSignKey = (PrivateKey)service.getKey(clientSignKeyAlias);
    	if( privateSignKey == null) {
           	throw new IllegalStateException("privateSignKey is not available.");
        }

        //Get Public certificate from the system.jks
        X509Certificate encryptCert = (X509Certificate)service.getCertificate(clientSignKeyAlias);

        if(encryptCert == null) {
            throw new IllegalStateException("signCert is not available.");
        }
        encryptCert.checkValidity();



 //try {

        InputStream is = new ByteArrayInputStream(body);
        EnvelopedDataStream enveloped_data = new EnvelopedDataStream(is);
        GCMParameterSpec params = null;

        RecipientInfo[] recipients = enveloped_data.getRecipientInfos();
        SecretKey secretKey = recipients[0].decryptKey(privateSignKey);
        EncryptedContentInfoStream eci = (EncryptedContentInfoStream) enveloped_data.getEncryptedContentInfo();
        AlgorithmID contentEA = eci.getContentEncryptionAlgorithm();
        SEQUENCE seq = (SEQUENCE) contentEA.getParameter();
    	OCTET_STRING oct = (OCTET_STRING) seq.getComponentAt(0);
        //params = new GCMParameterSpec(secretKey.getEncoded().length, (byte[]) oct.getValue());
        params = new GCMParameterSpec(128, (byte[]) oct.getValue());
        eci.setupCipher(secretKey, params);
        InputStream data_is = eci.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Util.copyStream(data_is, baos, null);
        byte[] decrypted = baos.toByteArray();
    //} catch (Exception e) {
    //    message.setProperty("errorCode", "005");
    //    message.setProperty("errorMessage", "Decryption error - " + e.getMessage());
    //}


   message.setBody(decrypted);

   return message;
}
```
### Sample input
[Sample input encrypted file](aes256_gcm_encrypted_payload.p7m)
### Sample output
[Decrypted output](aes256_gcm_decrypted_payload.xml)
