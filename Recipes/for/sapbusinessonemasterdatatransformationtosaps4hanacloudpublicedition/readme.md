# SAP Business One Master Data Transformation to SAP S/4HANA Cloud Public Edition 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20SAP%20Business%20One%20Master%20Data%20Transformation%20to%20SAP%20S/4HANA%20Cloud%20Public%20Edition) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20SAP%20Business%20One%20Master%20Data%20Transformation%20to%20SAP%20S/4HANA%20Cloud%20Public%20Edition)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20SAP%20Business%20One%20Master%20Data%20Transformation%20to%20SAP%20S/4HANA%20Cloud%20Public%20Edition) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Transform the data in master data objects of SAP Business one into a form suitable for migration into SAP S/4HANA Cloud Public Edition object.

<p>This package supports you with the transformation of data in business objects of SAP Business One into a form suitable for migration into SAP S/4HANA Cloud Public Edition object.</p>
<p>Each integration flow in this package provides the field level mapping of standard attributes in business object of SAP Business One to attributes in object of SAP S/4HANA Cloud Public Edition.</p>
<p>The artifact in this package works without connecting the integration suite to source system and target system. The payload is passed manually to integration flows and the output is received as a file that is stored locally.</p>
<p>This package provides one IFlow:&nbsp;</p>
<p>1. B1 Customer Master Data Transformation</p>

[Download the integration package](SAPBusinessOneMasterDataTransformationtoSAPS4HANACloudPublicEdition.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/SAPBusinessOneMasterDataTransformationtoSAPS4HANACloudPublicEdition)\
[View documentation](SAPBusinessOneMasterDataTransformationtoSAPS4HANACloudPublicEdition.pdf)\
[View high level effort](effort.md)
## Integration flows
### B1 Customer Master Data Transformation 
Transform the data in Customer Master Data object of SAP Business One into the Customer migration template of SAP S/4HANA Cloud Public Edition, suitable for upload via Migration cockpit. \
 ![input-image](B1_Customer_Master_Data_Transformation.png)