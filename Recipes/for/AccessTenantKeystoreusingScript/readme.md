# Accessing keystore artifacts using a Groovy script

![Shweta Walaskar](https://github.com/swalaskar.png?size=50 )|[Shweta Walaskar](https://github.com/swalaskar)|
----|----|

Any keypair available in tenant keystore can be accessed programmatically from a script with the help of the getKey and getCertificate api of the KeyStoreService class


## Recipe
Step|Code|Why?
----|----|----
Import classes | ```com.sap.it.api.securestore.KeyStoreService;```|
Get a handle to __KeyStoreService__ | ```def service = ITApiFactory.getApi(KeystoreService.class, null);```|
Retrieve private key from keypair in tenant keystore| ```PrivateKey privateSignKey = (PrivateKey)service.getKey(clientSignKeyAlias); ```|clientSignKeyAlias is the alias of keypair available in tenant keystore
Retrieve public certificate from keypair in tenant keystore| ```X509Certificate encryptCert = (X509Certificate)service.getCertificate(clientSignKeyAlias); ```
Check validity of public certificate | ```encryptCert.checkValidity();```

## Sample integration flow
To download a sample integration flow that used this script, refer to [Decryption with AES256-GCM algorithm using iaik libraries](../Decryption_using_AES_GCM_iaik/readme.md)

### Sample Script
Below code snippet is used to access these details from tenant keystore.
```
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
}
```
