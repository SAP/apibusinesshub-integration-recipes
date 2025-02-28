# Replication of Master Data between SAP S/4HANA Public Cloud Edition and S/4HANA 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Replication%20of%20Master%20Data%20between%20SAP%20S/4HANA%20Public%20Cloud%20Edition%20and%20S/4HANA) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Replication%20of%20Master%20Data%20between%20SAP%20S/4HANA%20Public%20Cloud%20Edition%20and%20S/4HANA)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Replication%20of%20Master%20Data%20between%20SAP%20S/4HANA%20Public%20Cloud%20Edition%20and%20S/4HANA) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Replication of master data between SAP S/4HANA Public Cloud Edition and SAP S/4HANA On-Premise involves synchronizing essential business data across both environments.

<p>Replication of master data between SAP S/4HANA Public Cloud Edition and SAP S/4HANA on-premise involves synchronizing essential business data across both environments.&nbsp;</p>
<p>The package contains the following Integration Flows:</p>
<ul>
 <li>Replicate Business Partner from S4HANA Private Cloud to SAP S4HANA Public Cloud Edition</li>
 <li>Replicate Business Partner Confirmation from SAP S4HANA Public Cloud Edition to S4HANA Private Cloud (Optional)</li>
 <li>Replicate Business Partner Attachment from S/4HANA Private Cloud to S/4HANA Public Cloud Edition</li>
 <li>Replicate Cost Centre from SAP S4HANA Public Cloud Edition to S4HANA Private Cloud</li>
 <li>Replicate Profit Center from SAP S4HANA Public Cloud Edition to S4HANA Private Cloud</li>
 <li>Replicate Work Breakdown Structure (WBS) Code from S4HANA Public Cloud Edition to S4HANA Private Cloud</li>
</ul>

[Download the integration package](ReplicationofMasterDatabetweenSAPS4HANAPublicCloudEditionandS4HANA.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/ReplicationofMasterDatabetweenSAPS4HANAPublicCloudEditionandS4HANA)\
[View documentation](ReplicationofMasterDatabetweenSAPS4HANAPublicCloudEditionandS4HANA.pdf)\
[View high level effort](effort.md)
## Integration flows
### Value Mapping for Logical System Name 
Map SenderBusinessSystemID from S/4HANA Private Cloud Edition to Logical System Name to S/4HANA Public Cloud Edition \
 ![input-image](Map_SenderBusinessSystemID_from_ERP_or_S4HANA_OP_to_Logical_System_from_S4HANA_Public_Cloud_Communication_System_DEV.png)
### Replicate Business Partner Confirmation from SAP S4HANA Public Cloud Edition to S4HANA Private Cloud 
Business Partner Confirmation replication from SAP S/4HANA Public Cloud Edition to S/4HANA Private Cloud \
 ![input-image](Replicate_Business_Partner_Confirmation_from_SAP_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud.png)
### Replicate Profit Center from SAP S4HANA Public Cloud Edition to S4HANA Private Cloud 
Profit Center Replication from S/4HANA Public Cloud to S/4HANA On-premise. \
 ![input-image](Replicate_Profit_Center_from_SAP_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud.png)
### Replicate Cost Centre from SAP S4HANA Public Cloud Edition to S4HANA Private Cloud 
Cost Center Replication from S/4HANA Public Cloud Edition to S/4HANA Private Cloud Edition \
 ![input-image](Replicate_Cost_Centre_from_SAP_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud.png)
### Replicate Business Partner from S4HANA Private Cloud to SAP S4HANA Public Cloud Edition 
Business Partner Replication from S4HANA Private Cloud to SAP S4HANA Public Cloud Edition \
 ![input-image](Replicate_Business_Partner_from_S4HANA_Private_Cloud_to_SAP_S4HANA_Public_Cloud_Edition.png)
### Replicate Business Partner Attachment from S4HANA Private Cloud to S4HANA Public Cloud Edition 
Replicate Business Partner Attachment from S/4HANA Private Cloud to S/4HANA Public Cloud Edition \
 ![input-image](Replicate_Business_Partner_Attachment_from_S4HANA_Private_Cloud_to_S4HANA_Public_Cloud_Edition_1.png)
 ![input-image](Replicate_Business_Partner_Attachment_from_S4HANA_Private_Cloud_to_S4HANA_Public_Cloud_Edition_2.png)
 ![input-image](Replicate_Business_Partner_Attachment_from_S4HANA_Private_Cloud_to_S4HANA_Public_Cloud_Edition_3.png)
 ![input-image](Replicate_Business_Partner_Attachment_from_S4HANA_Private_Cloud_to_S4HANA_Public_Cloud_Edition_4.png)
### Replicate WBS Code from S4HANA Public Cloud Edition to S4HANA Private Cloud 
Replicate Work Breakdown Structure (WBS) Code from S4HANA Public Cloud Edition to S4HANA Private Cloud \
 ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_1.png)
 ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_2.png)
  ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_3.png)
 ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_4.png)
  ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_5.png)
 ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_6.png)
  ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_7.png)
 ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_8.png)
  ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_9.png)
 ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_10.png)
  ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_11.png)
 ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_12.png)
  ![input-image](Replicate_WBS_Code_from_S4HANA_Public_Cloud_Edition_to_S4HANA_Private_Cloud_13.png)
