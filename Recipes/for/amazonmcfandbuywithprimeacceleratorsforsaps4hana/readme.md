# Amazon MCF and Buy with Prime Accelerators for SAP S/4HANA Private Cloud 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Amazon%20MCF%20and%20Buy%20with%20Prime%20Accelerators%20for%20SAP%20S/4HANA%20Private%20Cloud) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Amazon%20MCF%20and%20Buy%20with%20Prime%20Accelerators%20for%20SAP%20S/4HANA%20Private%20Cloud)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Amazon%20MCF%20and%20Buy%20with%20Prime%20Accelerators%20for%20SAP%20S/4HANA%20Private%20Cloud) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

This Integration package contains integration flows between Amazon's "Buy With Prime" system and SAP S/4HANA Cloud, Private Edition 2023 version

<p>This Integration package contains integration flows between Amazon's "Buy With Prime" system and SAP S/4HANA Cloud, Private Edition 2023</p>

[Download the integration package](AmazonMCFandBuywithPrimeAcceleratorsforSAPS4HANA.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/AmazonMCFandBuywithPrimeAcceleratorsforSAPS4HANA)\
[View documentation](AmazonMCFandBuywithPrimeAcceleratorsforSAPS4HANA.pdf)\
[View high level effort](effort.md)
## Integration flows
### Reusable Groovy Scripts for Amazon accelerators 
Script collection to support processing of IFlows between Amazon and S/4HANA \
 ![input-image](Reusable_Groovy_Scripts_for_Amazon_accelerators.png)
### Sync All Fulfillment Orders from SAP S4HANA to Amazon 
IFlow to replicate all orders from S/4HANA to Amazon for fulfillment \
 ![input-image](Sync_All_Fulfillment_Orders_from_SAP_S4HANA_to_Amazon.png)
### Sync Issue Refunds from S4HANA to Amazon 
IFlow syncs refunds from S/4HANA to Amazon \
 ![input-image](Sync_Issue_Refunds_from_S4HANA_to_Amazon.png)
### Receive Inventory Event from Amazon 
IFlow receives real-time inventory events from Amazon \
 ![input-image](Receive_Inventory_Event_from_Amazon.png)
### Create Fulfillment Order in Amazon 
IFlow to create Amazon order \
 ![input-image](Create_Fulfillment_Order_in_Amazon.png)
### Receive Package Delivery Fulfillment from Amazon 
IFlow receives package delivery in transit update from Amazon \
 ![input-image](Receive_Package_Delivery_Fulfillment_from_Amazon.png)
### Read Amazon Return Events from Message Queue 
IFlow to read return events from JMS Queue related to return and call sync return integration flow \
 ![input-image](Read_Amazon_Return_Events_from_Message_Queue.png)
### Sync Return Updates with Amazon from S4HANA 
IFlow to sync return details from S/4HANA to Amazon \
 ![input-image](Sync_Return_Updates_with_Amazon_from_S4HANA.png)
### Receive Refund Notification from Amazon 
IFlow to receive refund request notifications from Amazon \
 ![input-image](Receive_Refund_Notification_from_Amazon.png)
### Cancel Fulfillment Order in Amazon 
IFlow to cancel Amazon order \
 ![input-image](Cancel_Fulfillment_Order_in_Amazon.png)
### Read Amazon Inventory Summary from Message Queue 
IFlow to read inventory summaries from JMS Queue related to stock updates in S/4HANA \
 ![input-image](Read_Amazon_Inventory_Summary_from_Message_Queue.png)
### Receive Delivery Event from Amazon 
IFlow to receive delivery events from Amazon \
 ![input-image](Receive_Delivery_Event_from_Amazon.png)
### Value Mapping for Amazon accelerators 
Value Mapping for Amazon processes and S/4HANA \
 ![input-image](Value_Mapping_for_Amazon_accelerators.png)
### Receive Return Events from Amazon 
IFlow to receive return request notifications from Amazon \
 ![input-image](Receive_Return_Events_From_Amazon.png)
### Validate Return Order by Reference Sales Order 
IFlow to validate return order exists in S/4HANA for the corresponding sales order number \
 ![input-image](Validate_Return_Order_by_Reference_Sales_Order.png)
### Update Amazon Order for Fulfillment 
IFlow to update Amazon order to start fulfillment \
 ![input-image](Update_Amazon_Order_for_Fulfillment.png)
### Sync Return Updates from Amazon 
IFlow to sync return updates from Amazon to S/4HANA \
 ![input-image](Sync_Return_Updates_from_Amazon.png)
### Sync Selected Fulfillment Orders from SAP S4HANA to Amazon 
IFlow to replicate selected orders from S/4HANA to Amazon for fulfillment \
 ![input-image](Sync_Selected_Fulfillment_Orders_from_SAP_S4HANA_to_Amazon.png)
### Update Material Stock details in S4HANA 
IFlow to post inventory updates in S/4HANA  \
 ![input-image](Update_Material_Stock_details_in_S4HANA.png)
### Retreive Refunds from S4HANA 
IFlow to read refunds from S/4HANA on a periodic basis \
 ![input-image](Retreive_refunds_from_S4HANA.png)
### Receive Package Delivery Cancel from Amazon 
IFlow receives package delivery cancel updates from Amazon \
 ![input-image](Receive_Package_Delivery_Cancel_from_Amazon.png)
### Sync Inventory Updates from Amazon to SAP S4HANA 
IFlow to sync inventory updates from Amazon to SAP S/4HANA \
 ![input-image](Sync_Inventory_Updates_from_Amazon_to_SAP_S4HANA.png)
### Receive Package Delivery Milestone from Amazon 
IFlow receives package delivery milestone and delivered status updates  from Amazon to SAP S4HANA \
 ![input-image](Receive_Package_Delivery_Milestone_from_Amazon.png)