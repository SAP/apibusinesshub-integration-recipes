# Integration with SAP SuccessFactors to Automatically Convert Assignment IDs 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Integration%20with%20SAP%20SuccessFactors%20to%20Automatically%20Convert%20Assignment%20IDs) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Integration%20with%20SAP%20SuccessFactors%20to%20Automatically%20Convert%20Assignment%20IDs)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Integration%20with%20SAP%20SuccessFactors%20to%20Automatically%20Convert%20Assignment%20IDs) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

It converts/updates SAP SuccessFactors Assignment ID of Users based a CSV input file.

<p>This package picks up a CSV&nbsp;<span>f</span>ile from a<span>n</span>&nbsp;SFTP server and maps it into the request to update/convert the Assignment ID in SAP SuccessFactors using the&nbsp;<span>OData</span> API. The response of the update is provided on the SFTP server.&nbsp;</p>

[Download the integration package](IntegrationwithSAPSuccessFactorsforautomaticallyconvertAssignmentID.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/IntegrationwithSAPSuccessFactorsforautomaticallyconvertAssignmentID)\
[View documentation](IntegrationwithSAPSuccessFactorsforautomaticallyconvertAssignmentID.pdf)\
[View high level effort](effort.md)
## Integration flows
### Convert Assignment ID in SAP SuccessFactors via SFTP 
This integration flow takes Assignment ID data from an input CSV file of an SFTP server and updates Assignment ID of users in SAP SuccessFactors via OData API. \
 ![input-image](Convert_Assignment_ID_in_SAP_Successfactors_via_SFTP.png)