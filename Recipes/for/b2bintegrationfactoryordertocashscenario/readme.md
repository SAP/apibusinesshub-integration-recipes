# B2B Integration Factory - Order-to-Cash B2B Scenario 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20B2B%20Integration%20Factory%20-%20Order-to-Cash%20B2B%20Scenario) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20B2B%20Integration%20Factory%20-%20Order-to-Cash%20B2B%20Scenario)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20B2B%20Integration%20Factory%20-%20Order-to-Cash%20B2B%20Scenario) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Pre-built content for SAP Integration Suite's B2B capabilities Trading Partner Management & Integration Advisor, covering the Order-to-Cash B2B scenario with the choreography of message exchange between SAP S/4HANA On-Premise (SOAP/ IDOC) & EDI standards.

<p><strong>Business Challenge</strong></p>
<p>When using SAP Integration Suite's B2B capabilities, the creation of a Trading Partner Agreement (TPA) for an Order-to-Cash B2B scenario, where the business partner uses e.g., UN/EDIFACT D.10B and the internally supported format is SAP IDOC, requires functional experts with domain knowledge, integration experts to create and enable these TPAs, and above all, it requires time and efforts for implementation. Very often, neither the required knowledge nor the time is available.&nbsp;</p>
<p>&nbsp;</p>
<p><strong>Value Story&nbsp;</strong></p>
<p>The B2B Integration Factory creates this content of B2B scenarios as pre-packaged content in the form of trading partner agreements and the corresponding message implementation and mapping guidelines and makes them available accordingly. This B2B scenario now describes the electronic data exchange of the first set of relevant Order-to-Cash business data, such as &nbsp;</p>
<ul>
 <li><strong>Sales Order Request</strong>&nbsp;(Inbound):&nbsp;UN/EDIFACT D.(version) ORDERS &gt; SAP IDOC ORDERS.ORDERS05</li>
 <li><strong>Sales Order Response</strong>&nbsp;(Oubound): SAP IDOC ORDRSP.ORDERS05 &gt; UN/EDIFACT D.(version) ORDRSP</li>
 <li><strong>Delivery Notification</strong>&nbsp;(Outbound): SAP IDOC DESADV.DELVRY07 &gt; UN/EDIFACT D.(version) DESADV</li>
 <li><strong>Invoice&nbsp;</strong>(Outbound).&nbsp;SAP IDOC INVOIC.INVOIC02 &gt; UN/EDIFACT D.(version) INVOIC</li>
</ul>
<p>As you can see, in this B2B scenario, the enterprise message types are based on SAP IDOC, and the trading partner-related message types are based on UN/EDIFACT D.(version). For all corresponding message types, the required Message Implementation Guidelines (MIGs) and the Mapping Guidelines (MAGs) are already available. Significantly, the Mapping Guidelines already contain the needed mapping elements between the corresponding nodes as required at a functional level.&nbsp;</p>
<p>For&nbsp;<strong>SAP S/4HANA On-Premise (SOAP/ IDOC)</strong>, following&nbsp;<strong>EDI standards</strong>&nbsp;are provided:&nbsp;</p>
<ul>
 <li><strong>UN/EDIFACT&nbsp;</strong>versions&nbsp;<strong>D.93A, D.96A, D.01B, D.10B</strong></li>
 <li><strong>ASC X12&nbsp;</strong>versions&nbsp;<strong>4010, 5010, 7010</strong></li>
 <li><strong>TRADACOMS 4, 9</strong></li>
</ul>
<p>The included MIGs (Message Implementation Guidelines), MAGs (Message Application Guidelines), and TPAs (Trading Partner Agreements) are provided solely as examples to guide users. They can be customised according to specific customer requirements. For the TPAs, this package includes one example TPA per message type as a reference.</p>
<p>&nbsp;</p>
<p><strong>Business Benefits&nbsp;</strong></p>
<p>As a user of the SAP Integration Suite's B2B capabilities, you can download the zip files of the design-time artefacts (TPA, MIGs and MAGs) provided on GitHub for your Order-to-Cash B2B scenario and import them directly into the Trading Partner Management and Integration Advisor systems of your SAP Integration Suite tenant. This not only saves you a lot of time, but also provides you with high-quality artefacts securing a high level of consistency based on standard conventions and rules. Instead of creating everything from scratch, simply use the B2B scenario and its associated guidelines and adapt them to your trading partners' and your own specific requirements, configure the communication channels, activate the TPAs, test, and finally go live with your trading partners.</p>

[Download the integration package](B2BIntegrationFactoryOrdertoCashScenario.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/B2BIntegrationFactoryOrdertoCashScenario)\
[View documentation](B2BIntegrationFactoryOrdertoCashScenario.pdf)\
[View high level effort](effort.md)
## Integration flows