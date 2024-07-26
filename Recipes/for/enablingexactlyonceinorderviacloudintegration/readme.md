# Enabling Exactly Once in Order via Cloud Integration 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Enabling%20Exactly%20Once%20in%20Order%20via%20Cloud%20Integration) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Enabling%20Exactly%20Once%20in%20Order%20via%20Cloud%20Integration)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Enabling%20Exactly%20Once%20in%20Order%20via%20Cloud%20Integration) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Enabling Exactly Once in Order via Cloud Integration

<p>This Package enables Exactly Once in Order (EOIO) processing with SAP Integration Suite, Advanced Event Mesh (AEM) utilizing partitioned Queues using the "Advanced Event Mesh Adapter for SAP Integration Suite"&nbsp;based on the blog:</p>
<p><a href="https://community.sap.com/t5/technology-blogs-by-sap/enabling-in-order-processing-with-sap-integration-suite-advanced-event-mesh/ba-p/13703498#M172490" rel="nofollow">https://community.sap.com/t5/technology-blogs-by-sap/enabling-in-order-processing-with-sap-integration-suite-advanced-event-mesh/ba-p/13703498#M172490</a></p>
<p>This package provides two IFlows:</p>
<p>1. Produce Event from AEM Exactly Once In Order&nbsp;</p>
<p>2. Consume Event from AEM Exactly Once In Order&nbsp;</p>
<p>&nbsp;</p>
<p>Download "Advanced Event Mesh Adapter" Version 1.3.0 as a prerequisite for this Package</p>
<p><a href="https://api.sap.com/package/AdvancedEventMeshAdapterforSAPIntegrationSuite/overview" rel="nofollow">https://api.sap.com/package/AdvancedEventMeshAdapterforSAPIntegrationSuite/overview</a></p>

[Download the integration package](EnablingExactlyOnceinOrderviaCloudIntegration.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/EnablingExactlyOnceinOrderviaCloudIntegration)\
[View high level effort](effort.md)
## Integration flows
### Consume Event from AEM Exactly Once In Order 
Consume Events from Advanced Event Mesh
(AEM) via AEM Adapter with quality of service Exactly Once In Order (EOIO) \
 ![input-image](Consume_Event_from_AEM_Exactly_Once_In_Order.png)
### Produce Event to AEM Exactly Once In Order 
Produce Events to Advanced Event Mesh
(AEM) via AEM Adapter with quality of service Exactly Once In Order (EOIO) \
 ![input-image](Produce_Event_to_AEM_Exactly_Once_In_Order.png)