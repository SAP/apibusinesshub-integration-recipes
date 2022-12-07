# Replicate vendor and vendor group from SAP S/4HANA to SAP Concur

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Replicate%20vendor%20and%20vendor%20group%20from%20SAP%20S/4HANA%20to%20SAP%20Concur%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Replicate%20vendor%20and%20vendor%20group%20from%20SAP%20S/4HANA%20to%20SAP%20Concur%20 ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Replicate%20vendor%20and%20vendor%20group%20from%20SAP%20S/4HANA%20to%20SAP%20Concur%20 ) \|

![SAP API Business Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP API Business Hub](https://api.sap.com/allcommunity) |
----|----|


This iflow replicates vendor and vendor group from S/4HANA to Concur. It has full load and delta load mode. By default, the job is scheduled to run every hour.

[Download the integration package](ReplicatevendorandvendorgroupfromSAPS_4HANAtoSAPConcur.zip)\
[View package on the SAP API Business Hub](https://api.sap.com/package/ReplicateVendorandVendorgroupfromS4HANAtoConcur)\
[View documentation](ConfigurationGuide_replicatevendorandvendorgroupfromsaps4hanatosapconcur.pdf)\
[View high level effort estimate](effort.md)

## Integration Flows/Value Mappings

### Replicate Vendor and Vendor Group from SAP S4HANA to SAP Concur
Requests business partner data from SAP S/4HANA based on a Timer, and creates or updates vendor and creates vendor group.\
[View on SAP API Business Hub](https://api.sap.com/integrationflow/RepliacteVendorandVendorgroupfromS4HANAtoConcur)
![Replicate Vendor and Vendor Group from SAP S4HANA to SAP Concur](Vendor-and-Vendor-Gourp-Replicate-to-Concur-Main.png)
![Connect to SAP Concur API](Vendor-and-Vendor-Gourp-Replicate-to-Concur-Concur.png)

### Request Token from SAP Concur
Request session token from SAP Concur API on a scheduled basis.\
[View on SAP API Business Hub](https://api.sap.com/integrationflow/Get_Concur_Token)
![Request Token from SAP Concur](Get_Concur_Token.png)
