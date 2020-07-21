# Integration Flow recipes
\| Browsing by Topic \| [Browse by Author](author.md) \| [Browse by Artefact Type](for/readme.md) \| [Request a Recipe](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Request&template=recipe-request.md&title=How+to++) \| [Report a broken link](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=documentation&template=bug_report.md&title=Broken%20Link) \| [Contribute](https://github.com/SAP-samples/cloud-integration-flow/wiki/Things-to-do-on-this-repo#contribute)\|

## Topics
* [Amazon Web Services](#amazon-web-services)
* [Database Connectivity](#amazon-developer-connectivity)
* [How to Guides](#how-to-guide)
* [Integration Adapters](#integration-adapters)
* [Integration Pattern](#integration-pattern)
* [Mappings](#mappings)
* [Partner Directory](#partner-directory)
* [Security](#security)

***

### Amazon Web Services
Recipe|Description|Author
---|---|---
[Connect to Amazon DynamoDB](for/ConnectToAWSDynmoDB)|SAP CPI needs to make a rest call to DynamoDB endpoint|[Sunny Kapoor](author.md#sunny-kapoor)|
[Generate AWS4-HMAC-SHA256 Signature](for/GenerateAWS4_HMAC_SHA256)| A reusable recipe to generate an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.|[Sunny Kapoor](author.md#sunny-kapoor)|

***

### Database Connectivity
Recipe|Description|Author
---|---|---
[Connect to Amazon DynamoDB](for/ConnectToAWSDynmoDB)|SAP CPI needs to make a rest call to DynamoDB endpoint|[Sunny Kapoor](author.md#sunny-kapoor)|

***

### How to Guides
Recipe|Description|Author
---|---|---
[Simulate Response from Datastore Select Operation and Write Variable](for/SimulateResponseFromWriteVariableAndDataStores)|Test a still under development integration flow with dummy data without the need for deployment. This recipe simulate reading of Write Variable in Content Modifer and the Datastore Select operation|[Abhinav Verma](author.md#abhinav-verma)|

***

### Integration Adapters
Recipe|Description|Author
---|---|---
[Build custom Redis integration adapter](for/redis-integration-adapter/readme.md)|Redis is advanced key-value store where keys can contain strings, hashes, lists, sets and sorted sets. In addition it provides pub/sub functionality for inter-app communications. This integration adapter allows an integration flow to access Redis.| [Mayur Mohan Belur](author.md#mayur-mohan-belur)|
[Build custom Rabbit MQ integration adapter](for/redis-integration-adapter/readme.md)|The rabbitmq: component allows you produce and consume messages from RabbitMQ instances. Using the RabbitMQ AMQP client, this component offers a pure RabbitMQ approach over the generic AMQP component. This integration adapter enables an integration flow to persist or read messages in a RabbitMQ queue. | [Mayur Mohan Belur](author.md#mayur-mohan-belur) |

***

***

### Integration Patterns
Recipe|Description|Author
---|---|---

***

### Mappings
Recipe|Description|Author
---|---|---
[Accessing Value Mappings from Groovy script](for/AccessValueMappingsDynamicallyScript)|Use ```ITApiFactory.getApi()``` to get ```ValueMappingAPI``` class that can be used to retrieve the mappings.|[Sharad Dixit](author.md#sharad-dixit)|
[Convert JSON to XML using XSLT Mappings](for/ConvertJsonToXMLusingXSLT30)|This recipe converts and incoming file in JSON format into XML format |[Kamlesh Zanje](author.md#kamlesh-zanje)|
[Invoke Java functions from XSLT Mapping](for/InvokeJavaFunctionsFromXSLT30)|Writing reflexive extension functions in Java to be invoked from XSLT |[Kamlesh Zanje](author.md#kamlesh-zanje)|
[Use Map data structures in XSLT Mapping](for/ConstructMapDataStructsUsingXSLT30)|Utilize [Map](https://www.w3.org/TR/xslt-30/#map) data structures in XSLT Mappings flow step.|[Kamlesh Zanje](author.md#kamlesh-zanje) |

***

### Partner Directory
Recipe|Description|Author
---|---|---
[Accessing Partner Directory entries from within a Groovy script](for/Accessing-Partner-Directory-entries-from-within-a-script)|The String and Binary parameters from the Partner Directory can be accessed using a script with the help of getParameter API of the PartnerDirectoryService class. | [Amrita Laxmi](author.md#amrita-laxmi) |

***

### Security
Recipe|Description|Author
---|---|---
[Accessing keystore artifacts using a Groovy script](for/AccessTenantKeystoreusingScript) |Any keypair available in tenant keystore can be accessed programmatically from a script with the help of the getKey and getCertificate api of the KeyStoreService class|[Shweta Walaskar](author.md#shweta-walaskar)|
[CMS Decryption with AES256-GCM algorithm using iaik libraries](for/Decryption_using_AES_GCM_iaik)|Decryption algorithm AES256-GCM using iaik which is the default security provider for CPI|[Shweta Walaskar](author.md#shweta-walaskar)|
[Encryption with AES256-GCM algorithm using iaik libraries](for/Encryption_using_AES_GCM_iaik)|Encryption algorithm AES256-GCM using iaik which is the default security provider for CPI|[Shweta Walaskar](author.md#shweta-walaskar)|
[Generate AWS4-HMAC-SHA256 Signature](for/GenerateAWS4_HMAC_SHA256)| A reusable recipe to generate an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.|[Sunny Kapoor](author.md#sunny-kapoor)|
