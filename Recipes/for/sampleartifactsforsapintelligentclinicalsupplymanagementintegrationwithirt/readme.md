# SAP Intelligent Clinical Supply Management Integration with IRT 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20SAP%20Intelligent%20Clinical%20Supply%20Management%20Integration%20with%20IRT) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20SAP%20Intelligent%20Clinical%20Supply%20Management%20Integration%20with%20IRT)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20SAP%20Intelligent%20Clinical%20Supply%20Management%20Integration%20with%20IRT) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Sample artifacts for SAP Intelligent Clinical Supply Management for integration with Interactive Response Technology (IRT) systems

<p style="margin: 0cm ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">This package contains Iflows and the required documentation for integration of SAP Intelligent Clinical Supply Management (ICSM) and Interactive Response Technology (IRT).</p>
<p style="margin: 0cm ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">&nbsp;</p>
<p style="margin: 0cm ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">This integration scenario is only applicable for SAP S/4HANA on-premise systems running on release 2022 and above. The integration supports ICSM mapping to GS1 3.6 version.</p>
<p style="margin: 0cm ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">&nbsp;</p>
<p style="margin: 0cm ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">The following scenarios are covered-</p>
<ol style="margin-bottom: 0cm ; margin-top: 0px">
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">Iflow gets inventory report from ICSM once Inventory report is executed and event is triggered in ICSM.</li>
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">IRT send shipment request to ICSM.</li>
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">Iflow gets sales order details after event notification is triggered &nbsp;when sales order is created in ICSM. Shipment confirmation is sent to IRT.</li>
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">Iflow gets outbound delivery details after event notification is triggered after Post Goods Issue is performed in ICSM. Dispatch Advice is sent to IRT.</li>
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">IRT sends receiving advice to ICSM.</li>
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">IRT sends dispensing advice to ICSM.</li>
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">IRT sends kits status change data to ICSM.</li>
 <li style="margin: 0cm 0cm 0cm 0px ; font-size: 12pt ; font-family: &quot;aptos&quot; , sans-serif">Iflow gets kits data once kit status is changed and event notification is triggered in ICSM.</li>
</ol>

[Download the integration package](SampleArtifactsforSAPIntelligentClinicalSupplyManagementIntegrationwithIRT.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/SampleArtifactsforSAPIntelligentClinicalSupplyManagementIntegrationwithIRT)\
[View documentation](SampleArtifactsforSAPIntelligentClinicalSupplyManagementIntegrationwithIRT.pdf)\
[View high level effort](effort.md)
## Integration flows
### Receive IRT Actuals Data from IRT during a clinical study 
This interface is used to receive Actuals data (on visit level) from an interactive response technology system during a clinical study. \
 ![input-image](Receive_IRT_Actuals_Data_from_IRT_during_a_clinical_study.png)
### Receive Shipment Request from IRT to Event Mesh 
Receive Shipment Request data from IRT and send it to Event Mesh Queue \
 ![input-image](Receive_Shipment_Request_from_IRT_to_Event_Mesh.png)
### Map KitStatusChange Data GS1 to ICSM 
KitStatusChange Data Mapping GS1 into ICSM Format \
 ![input-image](Map_KitStatusChange_Data_GS1_to_ICSM.png)
### Map Shipment Request Data GS1 to ICSM 
Shipment Request Data Mapping GS1 into ICSM Format \
 ![input-image](Map_Shipment_Request_Data_GS1_to_ICSM.png)
### Map Inventory Study Data from ICSM to GS1 
Mapping Inventory Study Data from ICSM to GS1 format \
 ![input-image](Map_Inventory_Study_Data_from_ICSM_to_GS1.png)
### Send Shipment Confirmation from Event Mesh to IRT 
Send Shipment Confirmation data from Event Mesh Queue Payload to IRT System in GS1 format \
 ![input-image](Send_Shipment_Confirmation_from_Event_Mesh_to_IRT.png)
### Receive KitStatusChange from IRT to Event Mesh 
Receive KitStatusChange Data from IRT and Send it to Event Mesh Queue \
 ![input-image](Receive_KitStatusChange_from_IRT_to_Event_Mesh.png)
### Auxiliary_Scripts_Collection 
Script collection for needed functionality for logging errors, setting header parameters etc. \
 ![input-image](Auxiliary_Scripts_Collection.png)
### Map Sales Order Data ICSM to GS1 
Sales Order Mapping to GS1 Mapping Format \
 ![input-image](Map_Sales_Order_Data_ICSM_to_GS1.png)
### Get Inventory from ICSM and Map to GS1 
Get Inventory data from ICSM based on event payload and map it to GS1 format \
 ![input-image](Get_Inventory_from_ICSM_and_Map_to_GS1.png)
### Send Shipment Notification from Event Mesh to IRT 
Sending the Shipment Notification from Event Mesh Queue to IRT System \
 ![input-image](Send_Shipment_Notification_from_Event_Mesh_to_IRT.png)
### Send MedicationKitStatusChange from Event Mesh to IRT 
Send the MedicationKitStatusChange Data from Event Mesh Queue to IRT \
 ![input-image](Send_MedicationKitStatusChange_from_Event_Mesh_to_IRT.png)
### Process Dispensing Advice from Event Mesh to ICSM 
Process the Dispensing Advice Data GS1 from Event Mesh Queue payload to ICSM Format \
 ![input-image](Process_Dispensing_Advice_from_Event_Mesh_to_ICSM.png)
### Get Inventory for IRT Provider from ICSM to Event Mesh 
Get Inventory for IRT provider based on event payload and sent it to Event Mesh queue \
 ![input-image](Get_Inventory_for_IRT_Provider_from_ICSM_to_Event_Mesh.png)
### Map Dispensing Advice Data GS1 to ICSM 
Dispensing Advice Data Mapping GS1 into ICSM format \
 ![input-image](Map_Dispensing_Advice_Data_GS1_to_ICSM.png)
### Process KitStatusChange from Event Mesh to ICSM 
Process the KitStatusChange Data GS1 from Event Mesh Queue payload to ICSM Format \
 ![input-image](Process_KitStatusChange_from_Event_Mesh_to_ICSM.png)
### Map InventoryReport Data from ICSM to GS1 
Message Mapping from ICSM to GS1 format. \
 ![input-image](Map_InventoryReport_Data_from_ICSM_to_GS1.png)
### Process Receiving Advice from Event Mesh to ICSM 
Process the Receiving Advice Data GS1 from Event Mesh Queue payload to ICSM Format. \
 ![input-image](Process_Receiving_Advice_from_Event_Mesh_to_ICSM.png)
### Process Event Mesh Data for Inventory Report 
Get inventory event mesh payload and process it \
 ![input-image](Process_Event_Mesh_Data_for_Inventory_Report.png)
### Process Shipment Request from Event Mesh to ICSM 
Process the Shipment Request Data GS1 from Event Mesh Queue payload to ICSM Format. \
 ![input-image](Process_Shipment_Request_from_Event_Mesh_to_ICSM.png)
### Map Outbound Delivery Data from ICSM to GS1 
Mapping Outbound Delivery data from ICSM to GS1 format \
 ![input-image](Map_Outbound_Delivery_Data_from_ICSM_to_GS1.png)
### Value Mapping ICSM to GS1 Status Code 
  \
 ![input-image](Value_Mapping_ICSM_to_GS1_Status_Code.png)
### Send Inventory Data to IRT 
Send inventory to IRT \
 ![input-image](Send_Inventory_Data_to_IRT.png)
### Send Inventory for IRT Provider from Event Mesh to IRT 
Send Inventory data for IRT Provider from Event Mesh to IRT \
 ![input-image](Send_Inventory_for_IRT_Provider_from_Event_Mesh_to_IRT.png)
### Send Dispatch Advice from Event Mesh to IRT 
Send the Dispatch Advice Data from Event Mesh Queue to IRT \
 ![input-image](Send_Dispatch_Advice_from_Event_Mesh_to_IRT.png)
### Fetch study information data from Clinical study API 
This Clinical Study iflow enables an external system to read study data and add a study entity and submit the results. \
 ![input-image](Fetch_study_information_data_from_Clinical_study_API.png)
### Receive Receiving Advice from IRT to Event Mesh 
Receive Receiving Advice Data from IRT and Send it to Event Mesh Queue \
 ![input-image](Receive_Receiving_Advice_from_IRT_to_Event_Mesh.png)
### Sales Order Creation from ICSM to Event Mesh 
Creating the Sales Order from ICSM to Event Mesh Queue and process it. \
 ![input-image](Sales_Order_Creation_from_ICSM_to_Event_Mesh.png)
### Process MedicationKitStatusChange from ICSM to Event Mesh 
Process the MedicationKitStatusChange  from ICSM to Event Mesh Queue and Process it. \
 ![input-image](Process_MedicationKitStatusChange_from_ICSM_to_Event_Mesh.png)
### Send Inventory Study Data to IRT 
Send inventory Study to IRT \
 ![input-image](Send_Inventory_Study_Data_to_IRT.png)
### Process Outbound Delivery from ICSM to Event Mesh 
Create Outbound Delivery from ICSM to Event Mesh Queue and process it \
 ![input-image](Process_Outbound_Delivery_from_ICSM_to_Event_Mesh.png)
### Map Receiving Advice Data GS1 to ICSM 
Receiving Advice Data Mapping GS1 into ICSM format \
 ![input-image](Map_Receiving_Advice_Data_GS1_to_ICSM.png)
### Process Event Mesh Data for Inventory Report Study 
Get Inventory Study event mesh data payload and process it \
 ![input-image](Process_Event_Mesh_Data_for_Inventory_Report_Study.png)
### Get Inventory Study from ICSM to GS1 
Get Inventory Study data from ICSM based on event payload and map it to GS1 format \
 ![input-image](Get_Inventory_Study_from_ICSM_to_GS1.png)
### Receive Dispensing Advice from IRT to EVent Mesh 
Receive Dispensing Advice Data from IRT and Send it to Event Mesh Queue \
 ![input-image](Receive_Dispensing_Advice_from_IRT_to_EVent_Mesh.png)
### Receive IRT Master data from IRT during a clinical study 
This interface is used to receive master data (Subject Status, Treatment Arm, Dose, Phase) and actual enrollment data (on visit level) from an interactive response technology system during a clinical study. \
 ![input-image](Receive_IRT_Master_data_from_IRT_during_a_clinical_study.png)
### Map MedicationKitStatusChange Data ICSM to GS1 
MedicationKitStatusChange Data Mapping ICSM into GS1 Format \
 ![input-image](Map_MedicationKitStatusChange_Data_ICSM_to_GS1.png)