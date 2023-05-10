# Replicate vendor and vendor group from SAP S/4HANA to SAP Concur 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Replicate%20vendor%20and%20vendor%20group%20from%20SAP%20S/4HANA%20to%20SAP%20Concur) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Replicate%20vendor%20and%20vendor%20group%20from%20SAP%20S/4HANA%20to%20SAP%20Concur)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Replicate%20vendor%20and%20vendor%20group%20from%20SAP%20S/4HANA%20to%20SAP%20Concur) \| 

 ![SAP Accelerator Business Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Accelerator Business Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Requests business partner information from SAP S/4HANA and handles the replication to SAP Concur.

<p>This package is used to replicate vendor and vendor group information from SAP S/4HANA to SAP Concur system by requesting valid supplier information from SAP S/4HANA and then updating the vendor information and vendor group information in SAP Concur</p>
<p>It contains 3 artifacts to replicate the vendor and vendor groups from SAP S/4HANA to SAP Concur.</p>
<p>-&nbsp;Iflow to handle the authentication to SAP Concur APIs</p>
<p>- Iflow to replicate vendor and vendor group info from SAP S/4HANA to SAP Concur</p>
<p>- value mapping for supplier country and currency value mapping&nbsp;</p>

[Download the integration package](ReplicatevendorandvendorgroupfromSAPS_4HANAtoSAPConcur.zip)\
[View package on the SAP Accelerator Business Hub](https://api.sap.com/package/ReplicatevendorandvendorgroupfromSAPS4HANAtoSAPConcur)\
[View documentation](ConfigurationGuide_replicatevendorandvendorgroupfromsaps4hanatosapconcur.pdf)\
[View high level effort](effort.md)
## Integration flows/Value mapping
### Value Mapping for Replicating of Vendor and Vendor Group from SAP S4HANA to SAP Concur 
Value Mapping for supplier country and currency value. \
 ![input-image](Value_Mapping_for_replication_of_Vendor_and_Vendor_Group_from_SAP_S4HANA_to_SAP_Concur.jpg)
### Request Token from SAP Concur 
Request session token from SAP Concur API on a scheduled basis. \
 ![input-image](Get_Concur_Token.png)
### Replicate Vendor and Vendor Group from SAP S4HANA to SAP Concur 
Requests business partner data from SAP S/4HANA based on a Timer, and creates or updates vendor and creates vendor group \
 ![input-image](Vendor-and-Vendor-Gourp-Replicate-to-Concur-Main.png)\
 ![input-image](Vendor-and-Vendor-Gourp-Replicate-to-Concur-Concur.png)