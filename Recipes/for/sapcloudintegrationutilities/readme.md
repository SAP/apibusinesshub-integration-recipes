# SAP Integration Suite Cloud Integration Utilities 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20SAP%20Integration%20Suite%20Cloud%20Integration%20Utilities) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20SAP%20Integration%20Suite%20Cloud%20Integration%20Utilities)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20SAP%20Integration%20Suite%20Cloud%20Integration%20Utilities) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

This package provides content for handling specific operational topics related to the SAP Integration Suite Cloud Integration.

<p>This packages covers integration flows regarding:</p>
<ul>
 <li>Notification for certificates for which the expiration date has exceeded a predefined threshold.</li>
 <li>Notification for scenarios where no file has been picked up with an SFTP/FTP Sender Adapter.</li>
 <li>Upload Value Mapping Entries based on JSON or XML Input</li> 
</ul>

[Download the integration package](SAPIntegrationSuiteCloudIntegrationUtilities.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/SAPCloudIntegrationUtilities)\
[View documentation - Notify SFTP File Pick up failure](NotifySFTPFilePickupfailure.pdf)\
[View documentation - Alert Notification for expiring certificates on Cloud Integration](AlertNotificationforexpiringcertificatesontheSAPCloudIntegration.pdf)\
[View documentation - Upload Value Mapping Entries based on JSON or XML Input](UploadValueMappingEntriesbasedonJSONorXMLInput.pdf)\
[View high level effort](effort.md)
## Integration flows
### Notify SFTP File Pick up failure 
It provides a notification via email if no file has been picked up from the SFTP server. \
 ![input-image](Notify_SFTP_File_Pick_up_failure.png)
### Mail Notification for expiring keystore entries 
This integration flow notifies via a mail distribution list when keystore entries (e.g. certificate) reach a predefined expiration limit (in days). \
 ![input-image](Mail_Notification_for_expiring_keystore_entries.png)
 ### Upload Value Mapping Entries based on JSON or XML Input 
This integration flow enabled the upload of either a JSON/XML payload to a specific Value Mapping in Cloud Integration.\
 ![input-image](Upload_payload_based_entries_to_a_specific_Value_Mapping.png)\
 ![input-image](Upload_payload_based_entries_to_a_specific_Value_Mapping1.png)
### Scheduled Interface with Adhoc Run Functionality - Step 1 
This is a sample integration flow for scheduled interface \
 ![input-image](Scheduled_Interface_with_Adhoc_Run_Functionality_-_Step_1_1.png)
 ![input-image](Scheduled_Interface_with_Adhoc_Run_Functionality_-_Step_1_2.png)
### Scheduled Interface with Adhoc Run Functionality - Step 2 
This is tthe integration flow which adhoc run the scheduled interface  \
 ![input-image](Scheduled_Interface_with_Adhoc_Run_Functionality_-_Step_2.png)
### Export Value Mapping as CSV 
This Integration Flow  creates the Value Mapping entries as a CSV file via an API for further processing or documentation. \
 ![input-image](Export_Value_Mapping_as_CSV_copy_1.png)
 ![input-image](Export_Value_Mapping_as_CSV_copy_2.png)
### Process SOAP Attachments via Base64 XML 
This Iflows provides a way to process SOAP Attachments by creating an Base64 XML \
 ![input-image](Process_SOAP_Attachments_via_Base64_XML.png)