# Integration Flow Recipes
\| [Browse by Topic](../readme.md)  \| [Browse by Author](../author.md) \| Browsing by Artefact Type \| [Request a Recipe](https://github.com/SAP-samples/cloud-integration-flow/issues/new ) \| [Report an issue or broken link](https://github.com/SAP-samples/cloud-integration-flow/issues/new) \|

## Artefact Type
* [Groovy Scripts](#groovy-scripts)
* [Reusable Integration flows](#reusable-integration-flows)
* [XSLT Scripts](#xslt-scripts)

****

### Groovy Scripts
Recipe|Description|Topic
---|---|---|
[Accessing keystore artifacts using a Groovy script](for/AccessTenantKeystoreusingScript) |Any keypair available in tenant keystore can be accessed programmatically from a script with the help of the getKey and getCertificate api of the KeyStoreService class|[Security](../readme.md#security)|
[Accessing Value Mappings from Groovy script](for/AccessValueMappingsDynamicallyScript)|Use ```ITApiFactory.getApi()``` to get ```ValueMappingAPI``` class that can be used to retrieve the mappings.|[Mappings](../readme.md#mappings)|
[CMS Decryption with AES256-GCM algorithm using iaik libraries](for/Decryption_using_AES_GCM_iaik)|Decryption algorithm AES256-GCM using iaik which is the default security provider for CPI|[Security](../readme.md#security)|
[Encryption with AES256-GCM algorithm using iaik libraries](for/Encryption_using_AES_GCM_iaik)|Encryption algorithm AES256-GCM using iaik which is the default security provider for CPI|[Security](../readme.md#security)|
[Generate AWS4-HMAC-SHA256 Signature](for/GenerateAWS4_HMAC_SHA256)| A reusable recipe to generate an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.|[Amazon Web Service](../readme.md#amazon-web-service)\|[Security](../readme.md#security)|

***

### Reusable Integration flows
Recipe|Description|Topic
---|---|---|
[Generate AWS4-HMAC-SHA256 Signature](for/GenerateAWS4_HMAC_SHA256)| A reusable recipe to generate an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.|[Amazon Web Service](../readme.md#amazon-web-service)\|[Security](../readme.md#security)|

***

### Sample integration flow
These recipes only have sample integration flows, other types usually **also** have samples included.

Recipe|Description|Topic
---|---|---|
[Connect to Amazon DynamoDB](for/ConnectToAWSDynmoDB)|SAP CPI needs to make a rest call to DynamoDB endpoint|[Amazon Web Service](../readme.md#amazon-web-service) \|[Database](../readme.md#database)|

***

### XSLT Scripts
Recipe|Description|Topic
---|---|---|
[Convert JSON to XML using XSLT Mappings](for/ConvertJsonToXMLusingXSLT30)|This recipe converts and incoming file in JSON format into XML format |[Mappings](../readme.md#mappings)|
[Use Map data structures in XSLT Mapping](for/ConstructMapDataStructsUsingXSLT30)|Utilize [Map](https://www.w3.org/TR/xslt-30/#map) data structures in XSLT Mappings flow step.|[Mappings](../readme.md#mappings)|
[Invoke Java functions from XSLT Mapping](for/InvokeJavaFunctionsFromXSLT30)|Writing reflexive extension functions in Java to be invoked from XSLT Mappings | [Mappings](../readme.md#mappings) |

***
