# SAP SuccessFactors Integration with Third Party - EmpJob Delta Change

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20SAP%20SuccessFactors%20Integration%20with%20Third%20Party%20-%20EmpJob%20Delta%20Change) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20SAP%20SuccessFactors%20Integration%20with%20Third%20Party%20-%20EmpJob%20Delta%20Change)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20SAP%20SuccessFactors%20Integration%20with%20Third%20Party%20-%20EmpJob%20Delta%20Change) \|

 ![SAP API Business Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP API Business Hub](https://api.sap.com/allcommunity) |
 ----|----|

<p>With this integration package, you can transport the EmpJob delta changes (create, update&nbsp; and delete) via ODATA API from SAP SuccessFactors to Third Party.</p>
<p>This package enables you to do the following:</p>
<ul>
 <li>SF EC Job Information Initial Load(Save Data to HANA DB as history data, if needed, send to Third Party for initial load)</li>
 <li>SF EC Job Information Delta Load(Send data to Third Party for delta changes create, update and delete via ODATA API )</li>
 <li>SF EC Special Deletion EmpJob Compare /SF EC Special Deletion EmpJob Send(Send data to Third Party for the special deletion situation :If all records of the employee were deleted, no record is returned in the response )</li>
</ul>

[Download the integration package](SAPSuccessFactorsIntegrationwithThirdPartyEmpJobDeltaChange.zip)\
[View package on the SAP API Business Hub](https://api.sap.com/package/SAPSuccessFactorsIntegrationwithThirdPartyEmpJobDeltaChange)\
[View documentation - Job Information Delta Load](Documentation_SF EC Job Information Delta Load.pdf)\
[View Documentation - Job Information Initial Load](Documentation_SF EC Job Information Initial Load.pdf)\
[View Documentation - Job Information Special Deletion](Documentation_SF EC Job Information Special Deletion.pdf)\
[View high level effort](effort.md)

 ## Integration flows
### SF EC Job Information Special Deletion Send
Step2:Send data to Third Party for the special deletion situation :If all records of the employee were deleted, no record is returned in the response \
 ![input-image](SF_EC_Job_Information_Special_Deletion_Send.png)
### SF EC Job Information Initial Load
Save Data to HANA DB as history data, if needed, send to Third Party for initial load \
 ![input-image](SF_EC_Job_Information_Initial_Load.png)
### SF EC Job Information Special Deletion Compare
Step1:Send data to Third Party for the special deletion situation :If all records of the employee were deleted, no record is returned in the response \
 ![input-image](SF_EC_Job_Information_Special_Deletion_Compare.png)
### SF EC Job Information Delta Load
Send data to Third Party for delta changes create, update and delete via ODATA API  \
 ![input-image](SF_EC_Job_Information_Delta_Load.png)
