# [DEPRECATED] SAP Signavio Integration with SAP Cloud ALM 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20[DEPRECATED]%20SAP%20Signavio%20Integration%20with%20SAP%20Cloud%20ALM) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20[DEPRECATED]%20SAP%20Signavio%20Integration%20with%20SAP%20Cloud%20ALM)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20[DEPRECATED]%20SAP%20Signavio%20Integration%20with%20SAP%20Cloud%20ALM) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Import business processes between SAP Signavio and SAP Cloud ALM.

<p>Please note, that this package is deprecated. Please use the standard integration between SAP Signavio and SAP Cloud ALM instead:</p>
<ul>
 <li><a href="https://help.sap.com/docs/signavio-process-manager/workspace-admin-guide/integrating-sap-process-manager-with-sap-cloud-alm" rel="nofollow">Getting Started Guide (SAP Help)</a></li>
</ul>
<ul>
 <li><a href="https://help.sap.com/docs/signavio-process-manager/user-guide/exporting-bpmn-diagrams-revisions-in-sap-signavio-to-sap-cloud-alm" rel="nofollow">SAP Help Documentation</a></li>
</ul>
<p>The Integration Suite community solution for the integration of SAP Signavio and SAP Cloud ALM provides Integration Flows to transfer Process Diagrams from SAP Signavio into SAP Cloud ALM as Solution Processes into the custom process repository.</p>
<p>The Process Diagrams (BPMN) are transferred as Scalable Vector Graphics (SVGs) into the Process Authoring microservice of SAP Cloud ALM upon approval via the SAP Signavio Process Governance workflow.<br>
  Continuous process updates of the process can be sent from SAP Signavio to SAP Cloud ALM without losing the reference to the assigned artefacts (e.g., requirements, test cases).<br>
  Besides the Process Information a backlink to SAP Signavio from SAP Cloud ALM is provided.<br>
  The community solution can easily be consumed directly via the provided community template.<br>
  The Integration Suite Community Solution is a custom solution and not a standard SAP product.</p>
<p>Consulting Services to support the implementation are available.</p>
<p>To learn more contact alm.consulting@sap.com</p>

[Download the integration package](SAPSignavioIntegrationwithSAPCloudALM.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/SAPSignavioIntegrationwithSAPCloudALM)\
[View documentation](SAPSignavioIntegrationwithSAPCloudALM.pdf)\
[View high level effort](effort.md)
## Integration flows
### Send Error Notification E-Mail 
When the main IFlow fails/ throws an exception, this IFlow sends an e-mail to a configured address. \
 ![input-image](IF_Send_Error_Notification_Email.png)
### Import processes from SAP Signavio to SAP Cloud ALM 
This IFlow imports processes from SAP Signavio to SAP Cloud ALM. \
 ![input-image](IF_Signavio_CALM_Process.png)