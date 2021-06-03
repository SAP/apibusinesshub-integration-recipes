# Integration Flow recipes
\| [Browse by Topic](readme.md) \| Browsing by Author \| [Browse by Artefact Type](for/readme.md) \| [Request a Recipe](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Request&template=recipe-request.md&title=How+to++) \| [Report a broken link](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=documentation&template=bug_report.md&title=Broken%20Link) \|[ Contribute ](https://github.com/SAP-samples/apibusinesshub-integration-recipes/blob/master/CONTRIBUTING.md)\|

## Authors
* [Amrita Laxmi](#amrita-Laxmi)
* [Abhinav Verma](#abhinav-verma)
* [Axel Albrecht](#axel-albrecht)
* [Balchandra Wadekar](#bhalchandra-wadekar)
* [Kamlesh Zanje](#kamlesh-zanje)
* [Mayur Mohan Belur](#mayur-mohan-belur)
* [Sharad Dixit](#sharad-dixit)
* [Shweta Walaskar](#shweta-walaskar)
* [Sunny Kapoor](#sunny-kapoor)

***

### [Amrita Laxmi](https://github.com/amritalaxmi )
![Amrita Laxmi](https://github.com/amritalaxmi.png?size=50) | Integration scenario developer for SAP Cloud Integration, SAP Labs India |
----|----|

Recipe|Description|Topic
---|---|---
[Accessing Partner Directory entries from within a Groovy script](for/Accessing-Partner-Directory-entries-from-within-a-script)|The String and Binary parameters from the Partner Directory can be accessed using a script with the help of getParameter API of the PartnerDirectoryService class. |[Partner Directory](readme.md#partner-directory) |

***

### [Abhinav Verma](https://github.com/abhinavverma0501 )
![Abhinav Verma](https://github.com/abhinavverma0501.png?size=50) | Integration scenario developer for SAP Cloud Integration, SAP Labs India |
----|----|

Recipe|Description|Topic
---|---|---
[Simulate Response from Datastore Select Operation and Write Variable](for/SimulateResponseFromWriteVariableAndDataStores)|Test a still under development integration flow with dummy data without the need for deployment. This recipe simulate reading of Write Variable in Content Modifier and the Datastore Select operation.|[How to Guides](readme.md#how-to-guide)|

***

### [Axel Albrecht](https://github.com/axelalbrechtsap)
![Axel Albrecht](https://github.com/axelalbrechtsap.png?size=50 )|Product Manager for SAP Integration Suite ([LinkedIn](https://www.linkedin.com/in/axel-albrecht-a6a16216a/))|
----|----|

Recipe|Description|Topic
---|---|---
[Deploy Integration Artefact and Get Endpoint](for/CICD-DeployIntegrationArtefactGetEndpoint)|Deploy an existing integration flow on Cloud Integration runtime. Optionally you can also get the endpoint of the integration flow.| [CICD](readme.md#cicd)|
[Deploy Run Once Integration Artefact and Check MPL](for/CICD-DeployRunOnceIntegrationArtefactAndCheckMpl)|Deploy an integration flow, check its deployment status and the message processing log status of the message execution that gets automatically triggered after the deployment due to a scheduler configuration or the consumption of files from a (S)FTP server or messages from a JMS queue.| [CICD](readme.md#cicd) |
[Get Latest Message Processing Log](for/CICD-GetLatestMessageProcessingLog)|Get the status of the last message execution of the configured integration artefact. In case the message execution failed, the job also provides the error information.| [CICD](readme.md#cicd) |
[Get Specific Message Processing Log](for/CICD-GetSpecificMessageProcessingLog)|Get the message procesing log status of either a specific message exchange ID or of the last run of a specific integration artefact. In case the message execution failed, the job also provides the error information.| [CICD](readme.md#cicd)|
[Store Integration Artefact](for/CICD-StoreIntegrationArtefact)|Download a specific integration artefact from the Cloud Integration tenant and store it in your source code repository like Git.| [CICD](readme.md#cicd) |
[Store Integration Artefact on New Version](for/CICD-StoreIntegrationArtefactOnNewVersion)|Check the Cloud Integration tenant for a new version of your integration artefact and if a new version exists, it downloads and stores it in a source code repository like Git.| [CICD](readme.md#cicd)|
[Undeploy Integration Artefact](for/CICD-UndeployIntegrationArtefact)|Undeploy a specific integration artefact from the Cloud Integration tenant.| [CICD](readme.md#cicd) |
[Update Integration Configuration Parameter](for/CICD-UpdateIntegrationConfigurationParameter)|Configure the value of an externalized parameter of a specific integration artefact in the Cloud Integration tenant.| [CICD](readme.md#cicd) |

***

### [Bhalchandra Wadekar](https://github.com/BhalchandraSW )
![Bhalchandra Wadekar](https://github.com/BhalchandraSW.png?size=50) | Senior Consultant, Syniti ([LinkedIn](https://www.linkedin.com/in/bhalchandrasw/))  |
----|----|

Recipe|Description|Topic
---|---|---
[Command Message](for/EIP-MessageConstruction-CommandMessage/readme.md) | This recipe lets you try out Command Message pattern for SOAP, OData and a BAPI. | [Integration Patterns](readme.md#integration-patterns)
[Correlation Identifier](for/EIP-MessageConstruction-CorrelationIdentifier/readme.md)| When sending the request, the sender adds a unique key to request. The receiver processes the request and puts the unique key as the Correlation Identifier in the reply message. Thus when a reply is received independent of the request, the sender knows the request corresponding the received reply based on the Correlation Identifier. | [Integration Patterns](readme.md#integration-patterns)
[Document Message](for/EIP-MessageConstruction-DocumentMessage/readme.md)|This recipe lets you send a Email using the Document Message pattern.|[Integration Patterns](readme.md#integration-patterns)|
[Event Message](for/EIP-MessageConstruction-EventMessage/readme.md)|This recipe lets you send a product update using the Event Message pattern.|[Integration Patterns](readme.md#integration-patterns)|
[Request Reply](for/EIP-MessageConstruction-Request-Reply/readme.md)|This recipe retrieves a list of products using the Request-Reply pattern.|[Integration Patterns](readme.md#integration-patterns)|
[Return Address](for/EIP-MessageConstruction-ReturnAddress/readme.md) | This recipe lets you try out Return Address pattern.| [Integration Patterns](readme.md#integration-patterns)

***

### [Kamlesh Zanje](https://github.com/kamleshzanje)
![Kamlesh Zanje](https://github.com/kamleshzanje.png?size=50 )| Product Owner for SAP Cloud Integration|
----|----|

Recipe|Description|Topic
---|---|---
[Convert JSON to XML using XSLT Mappings](for/ConvertJsonToXMLusingXSLT30)|This recipe converts and incoming file from JSON format to XML format. |[Mappings](readme.md#mappings)||
[Use Map data structures in XSLT Mapping](for/ConstructMapDataStructsUsingXSLT30)|Utilize [Map](https://www.w3.org/TR/xslt-30/#map) data structures in XSLT Mappings flow step.|[Mappings](readme.md#mappings)||
[Invoke Java functions from XSLT Mapping](for/InvokeJavaFunctionsFromXSLT30)|Writing reflexive extension functions in Java to be invoked from XSLT Mappings. | [Mappings](readme.md#mappings)|) |


***

### [Mayur Mohan Belur](https://github.com/mayurmohan)
![Mayur Mohan Belur](https://github.com/mayurmohan.png?size=50 )| Product Manager for SAP Integration Suite, SAP Cloud Integration expert, JAVA developer, SAP Labs India|
----|----|

Recipe|Description|Topic
---|---|---
[Build custom Azure Blob Storage integration adapter](for/azure-integration-adapter/readme.md)| Azure Blob Storage is a massively scalable and secure object storage for cloud-native workloads, archives, data lakes, high-performance computing and machine learning. The camel-Azure Blob Storage component stores and retrieves blobs from [Azure Storage Blob Service](https://azure.microsoft.com/services/storage/blobs/) using Azure APIs v12. This integration adapter enables an integration flow to connect to Azure Blob Storage collection.|[Integration Adapters](readme.md#integration-adapters) |
[Build custom MongoDB integration adapter](for/mongodb-integration-adapter/readme.md)|MongoDB is a very popular NoSQL solution and the camel-mongodb component integrates Camel with MongoDB allowing you to interact with MongoDB collections both as a producer (performing operations on the collection) and as a consumer (consuming documents from a MongoDB collection). This integration adapter enables an integration flow to connect to MongoDb collection.| [Integration Adapters](readme.md#integration-adapters) |
[Build custom Rabbit MQ integration adapter](for/redis-integration-adapter/readme.md)|The rabbitmq: component allows you produce and consume messages from RabbitMQ instances. Using the RabbitMQ AMQP client, this component offers a pure RabbitMQ approach over the generic AMQP component. This integration adapter enables an integration flow to persist or read messages in a RabbitMQ queue. | [Integration Adapters](readme.md#integration-adapters)|
[Build custom Redis integration adapter](for/redis-integration-adapter/readme.md)|Redis is advanced key-value store where keys can contain strings, hashes, lists, sets and sorted sets. In addition it provides pub/sub functionality for inter-app communications. This integration adapter allows an integration flow to access Redis.| [Integration Adapters](readme.md#integration-adapters)|
[Deploy Run Once Integration Artefact and Check MPL and Store If Success](for/CICD-DeployRunOnceIntegrationArtefactAndCheckMplAndStoreIfSuccess)|Deploy an integration flow, check its deployment status and the MPL (message processing log) status. In case of a successful message processing the job then downloads the integration flow artefact from the Cloud Integration tenant and commits it to the source code repository.| [CICD](readme.md#cicd)|
[Upload Integration Artefact](for/CICD-UploadIntegrationArtefact)|Checkout the latest version of the configured integration flow artefact from your source code repository and either update or create the artefact on the Cloud Integration tenant.|[CICD](readme.md#cicd)|

***


### [Sharad Dixit](https://github.com/sharadiiita)
![Sharad Dixit](https://github.com/sharadiiita.png?size=50 ) | Senior Software Developer in SAP Sales Cloud with in-depth experience of designing and developing software for business solution ([LinkedIn](https://www.linkedin.com/in/dixitsharad/))|
----|----|

Recipe|Description|Topic
---|---|---
[Accessing Value Mappings from Groovy script](for/AccessValueMappingsDynamicallyScript)|Use ```ITApiFactory.getApi()``` to get ```ValueMappingAPI``` class that can be used to retrieve the mappings.|[Mappings](readme.md#mappings)|

***

### [Shweta Walaskar](https://github.com/swalaskar)
![Shweta Walaskar](https://github.com/swalaskar.png?size=50 )|Development Architect in Innovative Business Solutions with 15 years of experience in integrating SAP SuccessFactors, SAP Marketing Cloud, SAP S/4HANA Cloud and also Hybrid integrations|
----|----|

Recipe|Description|Topic
---|---|---
[Accessing keystore artifacts using a Groovy script](for/AccessTenantKeystoreusingScript) |Any keypair available in tenant keystore can be accessed programmatically from a script with the help of the getKey and getCertificate api of the KeyStoreService class.|[Security](readme.md#security)|
[CMS Decryption with AES256-GCM algorithm using iaik libraries](for/Decryption_using_AES_GCM_iaik)|Decryption algorithm AES256-GCM using iaik which is the default security provider for CPI.|[Security](readme.md#security)|
[Encryption with AES256-GCM algorithm using iaik libraries](for/Encryption_using_AES_GCM_iaik)|Encryption algorithm AES256-GCM using iaik which is the default security provider for CPI.|[Security](readme.md#security)|

***

### [Sunny Kapoor](https://github.com/simplykapoor)
![Sunny Kapoor](https://github.com/simplykapoor.png?size=50 )|Product Manager for SAP Integration Suite ([LinkedIn](https://www.linkedin.com/in/sunny-kapoor-6a013129/))|
----|----|

Recipe|Description|Topic
---|---|---
[Connect to Amazon DynamoDB](for/ConnectToAWSDynmoDB)|SAP CPI needs to make a rest call to DynamoDB endpoint.|[Amazon Web Service](readme.md#amazon-web-services) \|[Database](readme.md#database)|
[Generate AWS4-HMAC-SHA256 Signature](for/GenerateAWS4_HMAC_SHA256)| A reusable recipe to generate an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.|[Amazon Web Service](readme.md#amazon-web-services)\|[Security](readme.md#security)|
[Store All API Providers](for/CICD-StoreAllAPIProviders)|Download all API Providers from the API Portal and store it in your source code repository like Git.|[CICD](readme.md#cicd)|
[Store Single API Provider](for/CICD-StoreSingleAPIProvider)|Download a specific API Provider from the API Portal and store it in your source code repository like Git.|[CICD](readme.md#cicd)|
[Store Single API Proxy](for/CICD-StoreSingleAPIProxy)|Download a specific API Proxy from the API Portal and store it in your source code repository like Git.|[CICD](readme.md#cicd)|
[Store Single Key Value Map](for/CICD-StoreSingleKeyValueMap)|Download a specific Key Value Map from the API Portal and store it in your source code repository like Git.|[CICD](readme.md#cicd)|
[Update Integration Resources on Git Commit](for/CICD-UpdateIntegrationResourcesOnGitCommit)|Develop and manage your integration resources like scripts and XSLT mappings using external IDEs and SCM tools like Git.|[CICD](readme.md#cicd)|
[Upload Single API Provider](for/CICD-UploadSingleAPIProvider)|Checkout the configured API Provider from the source code repository and either update or create the artefact on the API Portal tenant.|[CICD](readme.md#cicd)|
[Upload Single API Proxy](for/CICD-UploadSingleAPIProxy)|Checkout the configured API Proxy from the source code repository and either update or create the artefact on the API Portal tenant.|[CICD](readme.md#cicd)|
[Upload Single Key Value Map](for/CICD-UploadSingleKeyValueMap)|Checkout the configured Key Value Map from the source code repository and either update or create the artefact on the API Portal tenant.|[CICD](readme.md#cicd)|

***
