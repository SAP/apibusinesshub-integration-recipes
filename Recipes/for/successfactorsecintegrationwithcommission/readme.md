# SAP SuccessFactors Employee Central Integration with SAP Commissions 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20SAP%20SuccessFactors%20Employee%20Central%20Integration%20with%20SAP%20Commissions) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20SAP%20SuccessFactors%20Employee%20Central%20Integration%20with%20SAP%20Commissions)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20SAP%20SuccessFactors%20Employee%20Central%20Integration%20with%20SAP%20Commissions) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

This integration package provides integration flows to replicate employee data from SAP SuccessFactors Employee Central to SAP Commissions.

<p>This package provides an Integration between SAP SuccessFactors Employee Central with SAP Commissions to replicate employee data. The customer can choose between Event-Based and Timer-Initiated Integration Flow based on their business requirement.</p>
<p>&nbsp;</p>
<p>NOTE: While the standard content (<a href="https://api.sap.com/package/SAPSuccessFactorsEmployeeCentralintegrationwithSAPCommissions/integrationflow" rel="nofollow">https://api.sap.com/package/SAPSuccessFactorsEmployeeCentralintegrationwithSAPCommissions/integrationflow</a>) contains file-based Integration flows, this integration package provides Integration based on APIs and/or Event-Driven Integration.</p>

[Download the integration package](SuccessFactorsECIntegrationwithCommission.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/SuccessFactorsECIntegrationwithCommission)\
[View documentation](SuccessFactors_EmployeeCentral_Commission_Integration.pdf)\
[View high level effort](effort.md)
## Integration artifacts
### Script Collection Employee Data Replication 
Stores all the script files utilized in the Integration Flows. \
 ![input-image](Script_Collection_Employee_Data_Replication.png)
### Message Mapping for employee data between SAP SuccessFactors Employee Central and SAP Commissions 
Message Mapping to map the Employee Data to Participant Object \
 ![input-image](Participant_Message_Mapping.png)
### Message Mapping for title data between SAP SuccessFactors Employee Central and SAP Commissions 
Message Mapping to map Job Titles to Title Object \
 ![input-image](Title_Message_Mapping.png)
 ### Message Mapping for position data between SAP SuccessFactors Employee Central and SAP Commissions 
Message Mapping to map the Employee Job Position Data to Position Object \
 ![input-image](Position_Message_Mapping.png)
### Reprocess Error Event Messages 
Integration Flow to re-process the errored messages from Event-Initiated Replication \
 ![input-image](Reprocess_Error_Event_Messages.png)
### Replicate employee position from SAP SuccessFactors Employee Central to SAP Commissions 
Timer-Initiated Integration Flow to replicate employee job position data from SAP SuccessFactors Employee Central to SAP Commissions \
 ![input-image](Replicate_Employee_Position_from_SAP_SuccessFactors_Employee_Central_to_SAP_Commissions.png)
### Replicate Job Title from SuccessFactors Employee Central to Commission 
Timer-Initiated Integration Flow to replicate Job Titles from SuccessFactors Employee Central to Commission \
 ![input-image](Replicate_Job_Title_from_SuccessFactors_Employee_Central_to_Commissions1.png)\
 ![input-image](Replicate_Job_Title_from_SuccessFactors_Employee_Central_to_Commissions2.png)
 ### Push employee job position from SAP SuccessFactors Employee Central to SAP Commissions 
Event-Initiated Integration between SAP SuccessFactors Employee Central and SAP Commissions. The iflow replicates employee job position data. \ ![input-image](Employee_Job_Position_from_SAP_SuccessFactors_Employee_Central_to_SAP_Commissions1.png)\
![input-image](Employee_Job_Position_from_SAP_SuccessFactors_Employee_Central_to_SAP_Commissions2.png)
### Push employee data from SAP SuccessFactors Employee Central to SAP Commissions 
Event-Initiated Integration between SAP SuccessFactors Employee Central and SAP Commissions. The Iflow replicates employee data. \
 ![input-image](Employee_Data_from_SuccessFactors_Employee_Central_to_Commission1.png)\
 ![input-image](Employee_Data_from_SuccessFactors_Employee_Central_to_Commission2.png)
 ### Replicate employee data from SAP SuccessFactors Employee Central to SAP Commissions 
Timer-Initiated Integration Flow to replicate employee data from SAP SuccessFactors Employee Central to SAP Commissions. \
 ![input-image](Replicate_Employee_Data_from_SAP_SuccessFactors_Employee_Central_to_SAP_Commissions1.png)\
 ![input-image](Replicate_Employee_Data_from_SAP_SuccessFactors_Employee_Central_to_SAP_Commissions2.png) 
 ### Value Mapping Integration Configuration Controller 
Value Mapping to define Source and Target Data Settings that control the Integration flow behavior \
 ![input-image](Value_Mapping_Integration_Configuration_Controller1.png)\
 ![input-image](Value_Mapping_Integration_Configuration_Controller2.png)\
 ![input-image](Value_Mapping_Integration_Configuration_Controller3.png)