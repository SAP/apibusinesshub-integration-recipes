# Integration with Stored Value Solutions (SVS) for eGiftCard 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Integration%20with%20Stored%20Value%20Solutions%20(SVS)%20for%20eGiftCard) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Integration%20with%20Stored%20Value%20Solutions%20(SVS)%20for%20eGiftCard)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Integration%20with%20Stored%20Value%20Solutions%20(SVS)%20for%20eGiftCard) \| 

 ![SAP Accelerator Business Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Accelerator Business Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

This package provides iFlows for creating eGiftcard from stored value solutions (SVS).

<p>This package enables other systems to integrate with Stored Value Solutions (SVS) for custom gift card solutions.</p>
<p>This Package has the following IFlows:&nbsp;</p>
<ol>
 <li>Request the active certificate key from SVS that will be used for encrypting and decrypting message exchanged with SVS</li>
 <li>Establish session by calling the SVS's authenticate service and then call service (except trust/authenticate) at SVS</li>
 <li>Refresh the certificate key once currently used certificate key expires.&nbsp;</li>
</ol>

[Download the integration package](IntegrationwithStoredValueSolutionsforeGiftCard.zip)\
[View package on the SAP Accelerator Business Hub](https://api.sap.com/package/IntegrationwithStoredValueSolutionsSVSforeGiftCard)\
[Configuration Guideline for First ActiveKey Integration Flow](ConfigurationGuidelineforFirstActiveKeyIflow.pdf)\
[Configuration Guideline for Refresh Active Key Integration Flow](ConfigurationGuidelineforRefreshActiveKeyIflow.pdf)\
[Configuration Guideline for Requesting eGiftCard Integration Flow](ConfigurationGuidelineforRequestingeGiftCardIflow.pdf)\
[View high level effort](effort.md)
## Integration flows
### Request eGiftCard from SVS 
This Iflow requests the eGiftCard information for a specific customer from SVS. \
 ![input-image](Request_eGiftCard_from_SVS_1.png)
 ![input-image](Request_eGiftCard_from_SVS_2.png)
 ![input-image](Request_eGiftCard_from_SVS_3.png)
### Script Collection for SVS Integration 
Scripts being used in the Iflow especially for encrypting and decrypting messages. \
 ![input-image](Script_Collection_for_SVS_Integration.png)
### Request First Active Key from SVS 
This Iflow initially retrieves the Active Key from SVS and saves it in the Credential Store. \
 ![input-image](Request_First_Active_Key_from_SVS.png)
### Refresh Active Key for SVS 
Process Direct Flow called by the Iflow "Request eGiftCard from SVS" to refresh the active key from SVS. \
 ![input-image](Refresh_Active_Key_for_SVS.png)