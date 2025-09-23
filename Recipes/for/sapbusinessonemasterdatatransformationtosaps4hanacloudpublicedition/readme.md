# SAP Business One Accounting Data Transformation to SAP S/4HANA Cloud Public Edition

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Enabling%20Exactly%20Once%20in%20Order%20via%20Cloud%20Integration) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Enabling%20Exactly%20Once%20in%20Order%20via%20Cloud%20Integration)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Enabling%20Exactly%20Once%20in%20Order%20via%20Cloud%20Integration) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

<p>This package supports you with the transformation of data in business objects of SAP Business One (B1) into a form suitable for migration into SAP S/4HANA Cloud Public Edition object.

Each integration flow in this package provides the field level mapping of standard attributes in business object of SAP Business One to attributes in object of SAP S/4HANA Cloud Public Edition.

The artifact in this package works without connecting the integration suite to source system and target system. The payload is passed manually to integration flows and the output is received as a file that is stored locally.</p>
<p>This package provides three IFlows:</p>
<p>1. B1 Customer Master Data Transformation&nbsp;</p>
<p>2. B1 Item Master Data Transformation&nbsp;</p>
<p>3. B1 Supplier Master Data Transformation&nbsp;</p>
<p>&nbsp;</p>

[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/SAPBusinessOneAccountingDataTransformationtoSAPS4HANACloudPublicEdition)\
[View documentation - B1 Customer Master Data Transformation](B1CustomerMasterDataTransformation.pdf)\
[View documentation - B1 Item Master Data Transformation](B1ItemMasterDataTransformation.pdf)\
[View documentation - B1 Supplier Master Data Transformation](B1SupplierMasterDataTransformation.pdf)


[View high level effort](effort.md)

## Integration flows
### B1 Customer Master Data Transformation
Transform the data in Customer Master Data object of SAP Business One into the Customer migration template of SAP S/4HANA Cloud Public Edition, suitable for upload via Migration cockpit. \
 ![input-image](B1_Customer_Master_Data_Transformation.png)
### B1 Item Master Data Transformation
Transform the data in Item Master Data object of SAP Business One into the Product migration template of SAP S/4HANA Cloud Public Edition, suitable for upload via Migration cockpit. \
 ![input-image](B1_Item_Master_Data_Transformation.png)
### B1 Supplier Master Data Transformation
Transform the data in Supplier Master Data object of SAP Business one into the Supplier migration template of SAP S/4HANA Cloud Public Edition, suitable for upload via Migration cockpit. \
 ![input-image](B1_Supplier_Master_Data_Transformation.png)
