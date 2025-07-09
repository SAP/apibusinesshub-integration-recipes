# B2B Interface Migration Accelerator - UN/EDIFACT 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20B2B%20Interface%20Migration%20Accelerator%20-%20UN/EDIFACT) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20B2B%20Interface%20Migration%20Accelerator%20-%20UN/EDIFACT)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20B2B%20Interface%20Migration%20Accelerator%20-%20UN/EDIFACT) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

This package helps in migrating 1:1 B2B interfaces using EDIFACT message types from SAP PI/PO to SAP Cloud Integration. This includes pre-configured templates that facilitates quick migration process.

<p>To help to migrate the B2B interfaces using UN/EDIFACT messages types from SAP PI/PO to SAP Cloud Integration, this packages provides:</p>
<ul>
 <li>A template integration flow for the inbound interfaces</li>
 <li>A generic integration flow for a structure modification for the inbound scenario</li>
 <li>A sample message mapping for the inbound scenario (ORDERS05)</li>
 <li>A template integration flow for the outbound interfaces</li>
 <li>A generic integration flow for a structure modification for the outbound scenario</li>
 <li>A sample message mapping for the outbound scenario (INVOIC02)</li>
</ul>

[Download the integration package](BIMAB2BInterfaceMigrationAcceleratorUNEDIFACT.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/BIMAB2BInterfaceMigrationAcceleratorUNEDIFACT)\
[View documentation](BIMAB2BInterfaceMigrationAcceleratorUNEDIFACT.pdf)\
[View high level effort](effort.md)
## Integration flows
### MM_ORDERS_96A_to_ORDERS05_Sample 
This is a sample inbound message mapping imported from SAP PI/PO system \
 ![input-image](MM_ORDERS_96A_to_ORDERS05_Sample.png)
### BIMA - EDIFACT Essential Scripts 
This script collection includes Groovy Scripts which facilitates B2B EDIFACT Interfaces Migration from PI/PO to Cloud Integration \
 ![input-image](BIMA_-_EDIFACT_Essential_Scripts.png)
### BIMA - EDIFACT Flow - Outbound_TEMPLATE 
This iFlow template can be used to migrate B2B UN/EDIFACT message type outbound interfaces from SAP PI/PO to Cloud Integration \
 ![input-image](BIMA_-_EDIFACT_Flow_Outbound_TEMPLATE_1.png)
 ![input-image](BIMA_-_EDIFACT_Flow_Outbound_TEMPLATE_2.png)
### BIMA - EDIFACT Flow - Inbound_TEMPLATE 
This iFlow template can be used to migrate B2B UN/EDIFACT message type inbound interfaces from SAP PI/PO to Cloud Integration \
 ![input-image](BIMA_-_EDIFACT_Flow_Inbound_TEMPLATE_1.png)
 ![input-image](BIMA_-_EDIFACT_Flow_Inbound_TEMPLATE_2.png)
### BIMA - EDIFACT Structure Modifier Flow - Inbound 
This generic iFlow contains the logic to modify Cloud Integration Specific UN/EDIFACT XML to PI/PO specific XML file and forward it to Mapper flow \
 ![input-image](BIMA_-_Structure_Modifier_Flow_-_Inbound.png)
### BIMA - EDIFACT Structure Modifier Flow - Outbound 
This generic iFlow contains the logic to modify PI/PO specific UN/EDIFACT XML to Cloud Integration specific XML file and forward it to Converter flow \
 ![input-image](BIMA_-_Structure_Modifier_Flow_-_Outbound.png)
### MM_INVOIC_INVOIC02_to_INVOIC96A_Sample 
This is a sample outbound message mapping imported from SAP PI/PO system \
 ![input-image](MM_INVOIC_INVOIC02_to_INVOIC96A_Sample.png)