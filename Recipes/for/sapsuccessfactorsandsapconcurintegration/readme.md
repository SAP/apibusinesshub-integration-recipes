# Employee Integration between SAP SuccessFactors and SAP Concur Integration 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Employee%20Integration%20between%20SAP%20SuccessFactors%20and%20SAP%20Concur%20Integration) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Employee%20Integration%20between%20SAP%20SuccessFactors%20and%20SAP%20Concur%20Integration)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Employee%20Integration%20between%20SAP%20SuccessFactors%20and%20SAP%20Concur%20Integration) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

This package contains the employee integration between SAP SuccessFactors and SAP Concur via SFTP servers.

<p>This package provides a way to integrate employees from SAP SuccessFactors to SAP Concur via SFTP.<br>
  The newer version of the integration is independent of the file structure, whereas the old version is only covering the employee file structure for 305 and 350.</p>
<p>The Flow also covers the encryption of the payload.</p>
<p>Note: Starting from integration flow version 1.0.1, the Router and Content Modifier used for setting the row header for employee file structure for 305 and 350 are no longer included in the flow. Moving forward, as per SAP Best Practices, this should be handled within SAP SuccessFactors.</p>

[Download the integration package](SAPSuccessFactorsandSAPConcurIntegration.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/SAPSuccessFactorsandSAPConcurIntegration)\
[View documentation](ConfigurationGuide_EmployeeIntegrationfromSAPSuccessFactorstoSAPConcur.pdf)\
[View high level effort](effort.md)
## Integration flows
### Employee Integration from SAP SuccessFactors to SAP Concur 
This IFlow takes employee master data CSV files from the SAP SuccessFactors SFTP, transforms, PGP encrypts and sends them to the SAP Concur SFTP Server. \
 ![input-image](Employee_Integration_from_SAP_SuccessFactors_to_SAP_Concur.png)