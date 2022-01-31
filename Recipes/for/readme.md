# Integration Flow Recipes
\| [Browse by Topic](../readme.md)  \| [Browse by Author](../author.md) \| Browsing by Artefact Type \| [Request a Recipe](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Request&template=recipe-request.md&title=How+to++) \| [Report a broken link](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=documentation&template=bug_report.md&title=Broken%20Link) \| [Contribute](https://github.com/SAP-samples/apibusinesshub-integration-recipes/blob/master/CONTRIBUTING.md) \|

## Artefact Type
* [Groovy Scripts](#groovy-scripts)
* [Integration Adapters](#integration-adapters)
* [Reusable integration flows](#reusable-integration-flows)
* [Reusable integraiton packages](#reusable-integration-packages)
* [Reusable Jenkinsfiles](#reusable-jenkinsfiles)
* [Sample integration flows](#sample-integration-flow)
* [XSLT Scripts](#xslt-scripts)

****

### Groovy Scripts
Recipe|Description|Topic
---|---|---|
[Accessing keystore artifacts using a Groovy script](AccessTenantKeystoreusingScript) |Any keypair available in tenant keystore can be accessed programmatically from a script with the help of the getKey and getCertificate api of the KeyStoreService class|[Security](../readme.md#security)|
[Accessing Partner Directory entries from within a Groovy script](Accessing-Partner-Directory-entries-from-within-a-script)|The String and Binary parameters from the Partner Directory can be accessed using a script with the help of getParameter API of the PartnerDirectoryService class. | [Partner Directory](../readme.md#partner-directory) |
[Accessing Value Mappings from Groovy script](AccessValueMappingsDynamicallyScript)|Use ```ITApiFactory.getApi()``` to get ```ValueMappingAPI``` class that can be used to retrieve the mappings.|[Mappings](../readme.md#mappings)|
[CMS Decryption with AES256-GCM algorithm using iaik libraries](Decryption_using_AES_GCM_iaik)|Decryption algorithm AES256-GCM using iaik which is the default security provider for CPI|[Security](../readme.md#security)|
[Encryption with AES256-GCM algorithm using iaik libraries](Encryption_using_AES_GCM_iaik)|Encryption algorithm AES256-GCM using iaik which is the default security provider for CPI|[Security](../readme.md#security)|
[Generate AWS4-HMAC-SHA256 Signature](GenerateAWS4_HMAC_SHA256)| A reusable recipe to generate an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.|[Amazon Web Service](../readme.md#amazon-web-service)\|[Security](../readme.md#security)|

****

### Integration Adapters
Recipe|Description|Topic
---|---|---|
[Build custom Azure Blob Storage integration adapter](azure-integration-adapter/readme.md)| zure Blob Storage is a massively scalable and secure object storage for cloud-native workloads, archives, data lakes, high-performance computing and machine learning. The camel-Azure Blob Storage component stores and retrieves blobs from [Azure Storage Blob Service](https://azure.microsoft.com/services/storage/blobs/) using Azure APIs v12. This integration adapter enables an integration flow to connect to Azure Blob Storage collection.|[Integration Adapters](../readme.md#integration-adapters) |
[Build custom MongoDB integration adapter](mongodb-integration-adapter/readme.md)|MongoDB is a very popular NoSQL solution and the camel-mongodb component integrates Camel with MongoDB allowing you to interact with MongoDB collections both as a producer (performing operations on the collection) and as a consumer (consuming documents from a MongoDB collection). This integration adapter enables an integration flow to connect to MongoDb collection.| [Integration Adapters](../readme.md#integration-adapters) |
[Build custom Rabbit MQ integration adapter](rabbitmq-integration-adapter/readme.md)|The rabbitmq: component allows you produce and consume messages from RabbitMQ instances. Using the RabbitMQ AMQP client, this component offers a pure RabbitMQ approach over the generic AMQP component. This integration adapter enables an integration flow to persist or read messages in a RabbitMQ queue. | [Integration Adapters](../readme.md#integration-adapters)|
[Build custom Redis integration adapter](redis-integration-adapter/readme.md)|Redis is advanced key-value store where keys can contain strings, hashes, lists, sets and sorted sets. In addition it provides pub/sub functionality for inter-app communications. This integration adapter allows an integration flow to access Redis.| [Integration Adapters](../readme.md#integration-adapters)|


***

### Reusable Integration Flows
Recipe|Description|Topic
---|---|---|
[Generate AWS4-HMAC-SHA256 Signature](GenerateAWS4_HMAC_SHA256)| A reusable recipe to generate an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.|[Amazon Web Service](../readme.md#amazon-web-service)\|[Security](../readme.md#security)|

***

### Reusable Integration packages

Recipe|Description|Topic
---|---|---|
[Amazon Seller Marketplace Integration with Third Party](amazonsellermarketplaceintegrationwiththirdparty)|File based integration for sending Sales Transaction data from Amazon Seller Marketplace to Third Party (SAP Vistex template)  | [Amazon](../readme.md#amazon-web-services) \| [Third-Party Integration](../readme.md#third-party-integration)
[Qualtrics Transaction-Based Survey Integration with SAP Marketing Cloud](qualtricsextendedintegrationwithsapmarketingcloud)| Load data (customers and transactions with transcation based interaction in Marketing cloud) from SAP Marketing Cloud system to SAP Qualtrics. | [Qualtrics](../readme.md#qualtrics) \| [SAP Customer Experience](../readme.md#sap-customer-experience)
[ SAP Ariba Integration with Third-Party for Analytical Reporting](saparibaanalyticalreportingintegrationwiththirdparty)| Consumption of Ariba APIs (Job Submission API and Job Results API) for Analytical Reporting ( Standard/Custom Templates) with CSV Output for integrating with Third Party  | [SAP Ariba or SAP Business Network](../readme.md#sap-ariba) \| [Third-Party Integration](../readme.md#third-party-integration)
[SAP Ariba Integration with Third-Party for Vendor And Questionnaires](saparibaintegrationwiththirdpartyforvendorandquestionnaires)| Ariba APIs for Vendor and Questionnaires with CSV Output for integrating with Third Party| [SAP Ariba or SAP Business Network](../readme.md#sap-ariba) \| [Third-Party Integration](../readme.md#third-party-integration)
[SAP Business Network Integration with Non-SAP ERP](sapbusinessnetworkintegrationwithnonsaperp)| Baseline template to support the Purchase Order, Invoice and other transactional documents with the SAP Business Network| [SAP Ariba or SAP Business Network](../readme.md#sap-ariba) \| [Third-Party Integration](../readme.md#third-party-integration)
[SAP Document Compliance with Third Party - eDocuments](saps4hanaintegrationwiththirdpartyedocuments)| Exchange electronic invoices with the tax authorities for Chile, Colombia and Mexico, available for SAP S/4HANA, and SAP ERP (available as of SAP ERP 6.0 EHP5)|[SAP S/4HANA](../readme.md#sap-s4hana) \| [SAP ERP](../readme.md#sap-erp) \| [SAP Document Compliance](../readme.md#sap-document-compliance)
[SAP SuccessFactors Employee Central Payroll Integration with SAP S_4HANA or SAP ERP](sapsuccessfactorsemployeecentralpayrollintegrationwithsaps4hanaorsaperp)| Replication of Payroll posting data from SAP SuccessFactors Employee Central to SAP ERP or SAP S/4HANA. This data includes Cost Center/GL accounts/Expenses.| [SAP SuccessFactors Employee Central](../readme.md#sap-successfactors-employee-central) \| [SAP S/4HANA](../readme.md#sap-s4hana) \| [SAP ERP](../readme.md#sap-erp)
[SAP SuccessFactors Employee Central with Third-Party Payroll Vendor](sapsuccessfactorsemployeecentralwiththirdpartypayrollvendor)| Integration of business processes in SAP SuccessFactors Employee Central system with Third Party Payroll Vendor; support for delta based integration with seperate files required for HIRE/REHIRE, Daily Changes and TERMINATION|[SAP SuccessFactors Employee Central](../readme.md#sap-successfactors-employee-central) \| [Third-Party Integration](../readme.md#third-party-integration)

***
### Reusable JenkinsFiles

Recipe|Description|Author
---|---|---
[Deploy Integration Artefact and Get Endpoint](CICD-DeployIntegrationArtefactGetEndpoint)|Deploy an existing integration flow on Cloud Integration runtime. Optionally you can also get the endpoint of the integration flow.| [CICD](../readme.md#cicd) |
[Deploy Run Once Integration Artefact and Check MPL](CICD-DeployRunOnceIntegrationArtefactAndCheckMpl)|Deploy an integration flow, check its deployment status and the message processing log status of the message execution that gets automatically triggered after the deployment due to a scheduler configuration or the consumption of files from a (S)FTP server or messages from a JMS queue.| [CICD](../readme.md#cicd) |
[Deploy Run Once Integration Artefact and Check MPL and Store If Success](CICD-DeployRunOnceIntegrationArtefactAndCheckMplAndStoreIfSuccess)|Deploy an integration flow, check its deployment status and the MPL (message processing log) status. In case of a successful message processing the job then downloads the integration flow artefact from the Cloud Integration tenant and commits it to the source code repository.| [CICD](../readme.md#cicd) |
[Get Latest Message Processing Log](CICD-GetLatestMessageProcessingLog)|Get the status of the last message execution of the configured integration artefact. In case the message execution failed, the job also provides the error information.| [CICD](../readme.md#cicd) |
[Get Specific Message Processing Log](CICD-GetSpecificMessageProcessingLog)|Get the message procesing log status of either a specific message exchange ID or of the last run of a specific integration artefact. In case the message execution failed, the job also provides the error information.| [CICD](../readme.md#cicd) |
[Store All API Providers](CICD-StoreAllAPIProviders)|Download all API Providers from the API Portal and store it in your source code repository like Git.|[CICD](../readme.md#cicd)|
[Store Integration Artefact](CICD-StoreIntegrationArtefact)|Download a specific integration artefact from the Cloud Integration tenant and store it in your source code repository like Git.| [CICD](../readme.md#cicd) |
[Store Integration Artefact on New Version](CICD-StoreIntegrationArtefactOnNewVersion)|Check the Cloud Integration tenant for a new version of your integration artefact and if a new version exists, it downloads and stores it in a source code repository like Git.| [CICD](../readme.md#cicd) |
[Store Single API Provider](CICD-StoreSingleAPIProvider)|Download a specific API Provider from the API Portal and store it in your source code repository like Git.|[CICD](../readme.md#cicd)|
[Store Single API Proxy](CICD-StoreSingleAPIProxy)|Download a specific API Proxy from the API Portal and store it in your source code repository like Git.|[CICD](../readme.md#cicd)|
[Store Single Key Value Map](CICD-StoreSingleKeyValueMap)|Download a specific Key Value Map from the API Portal and store it in your source code repository like Git.|[CICD](../readme.md#cicd)|
[Undeploy Integration Artefact](CICD-UndeployIntegrationArtefact)|Undeploy a specific integration artefact from the Cloud Integration tenant.| [CICD](../readme.md#cicd) |
[Update Integration Configuration Parameter](CICD-UpdateIntegrationConfigurationParameter)|Configure the value of an externalized parameter of a specific integration artefact in the Cloud Integration tenant.| [CICD](../readme.md#cicd) |
[Update Integration Resources on Git Commit](CICD-UpdateIntegrationResourcesOnGitCommit)|Develop and manage your integration resources like scripts and XSLT mappings using external IDEs and SCM tools like Git.|[CICD](../readme.md#cicd)|
[Upload Integration Artefact](CICD-UploadIntegrationArtefact)|Checkout the latest version of the configured integration flow artefact from your source code repository and either update or create the artefact on the Cloud Integration tenant.| [CICD](../readme.md#cicd) |
[Upload Single API Provider](CICD-UploadSingleAPIProvider)|Checkout the configured API Provider from the source code repository and either update or create the artefact on the API Portal tenant.|[CICD](../readme.md#cicd)|
[Upload Single API Proxy](CICD-UploadSingleAPIProxy)|Checkout the configured API Proxy from the source code repository and either update or create the artefact on the API Portal tenant.|[CICD](../readme.md#cicd)|
[Upload Single Key Value Map](CICD-UploadSingleKeyValueMap)|Checkout the configured Key Value Map from the source code repository and either update or create the artefact on the API Portal tenant.| [CICD](../readme.md#cicd)|


***
### Sample integration flows
These recipes only have sample integration flows, other types usually **also** have samples included.

Recipe|Description|Topic
---|---|---|
[Command Message](EIP-MessageConstruction-CommandMessage ) | This recipe lets you try out Command Message pattern for SOAP, OData and a BAPI| [Integration Pattern](../readme.md#integration-pattern)
[Connect to Amazon DynamoDB](ConnectToAWSDynmoDB)|SAP CPI needs to make a rest call to DynamoDB endpoint|[Amazon Web Service](../readme.md#amazon-web-service) \|[Database](../readme.md#database)|
[Document Message](EIP-MessageConstruction-DocumentMessage/readme.md)|This recipe lets you send a Email using the Document Message pattern |[Integration Patterns](../readme.md#integration-patterns)|
[Event Message](EIP-MessageConstruction-EventMessage/readme.md)|This recipe lets you send a product update using the Event Message pattern|[Integration Patterns](../readme.md#integration-patterns)|
[Request Reply](EIP-MessageConstruction-Request-Reply/readme.md)|This recipe retrieves a list of products using the Request-Reply pattern|[Integration Patterns](../readme.md#integration-patterns)|
[Return Address](EIP-MessageConstruction-ReturnAddress/readme.md) | This recipe lets you try out Return Address pattern | [Integration Pattern](../readme.md#integration-pattern)

***

### XSLT Scripts
Recipe|Description|Topic
---|---|---|
[Convert JSON to XML using XSLT Mappings](ConvertJsonToXMLusingXSLT30)|This recipe converts and incoming file in JSON format into XML format |[Mappings](../readme.md#mappings)|
[Use Map data structures in XSLT Mapping](ConstructMapDataStructsUsingXSLT30)|Utilize [Map](https://www.w3.org/TR/xslt-30/#map) data structures in XSLT Mappings flow step.|[Mappings](../readme.md#mappings)|
[Invoke Java functions from XSLT Mapping](InvokeJavaFunctionsFromXSLT30)|Writing reflexive extension functions in Java to be invoked from XSLT Mappings | [Mappings](../readme.md#mappings) |

***
