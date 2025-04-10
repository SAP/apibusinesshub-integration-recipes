# B2B Integration Factory - Purchase-to-Pay B2B Scenario 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20B2B%20Integration%20Factory%20-%20Purchase-to-Pay%20B2B%20Scenario) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20B2B%20Integration%20Factory%20-%20Purchase-to-Pay%20B2B%20Scenario)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20B2B%20Integration%20Factory%20-%20Purchase-to-Pay%20B2B%20Scenario) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Pre-built content for SAP Integration Suite's B2B capabilities Trading Partner Management & Integration Advisor, covering the Purchase-to-Pay B2B scenario with the choreography of message exchange between SAP S/4HANA On-Premise IDOC & SOAP messages.

<p><strong>Business Challenge</strong></p>
<p>When using SAP Integration Suite's B2B capabilities, the creation of a Trading Partner Agreement (TPA) for a Purchase-to-Pay B2B scenario, where the business partner uses SOAP and the internally supported format is SAP IDoc, requires functional experts with domain knowledge, integration experts to create and enable these TPAs, and above all, it requires time and efforts for implementation. Very often, neither the required knowledge nor the time is available.&nbsp;</p>
<p>&nbsp;</p>
<p><strong>Value Story&nbsp;</strong></p>
<p>The B2B Integration Factory creates this content of B2B scenarios as pre-packaged content in the form of trading partner agreements and the corresponding message implementation and mapping guidelines and makes them available accordingly. This B2B scenario now describes the electronic data exchange of the first set of relevant Purchase-to-Pay business data, such as</p>
<ul>
 <li><strong>Purchase Order Request</strong> (Outbound): SAP IDOC ORDERS.ORDERS05 &gt; <span style="background-color: rgb( 255 , 255 , 255 )">SAP SOAP Ord</span>erRequest</li>
 <li><strong>Purchase Order Response</strong> (Inbound): <span style="background-color: rgb( 255 , 255 , 255 )">SAP SOAP OrderConfirmationReques</span>t &gt; SAP IDOC ORDRSP.ORDERS05</li>
 <li><strong>Delivery Notification</strong> (Inbound): SAP SOAP DeliveryRequest &gt; <span style="background-color: rgb( 255 , 255 , 255 )">SAP IDOC DESADV.DELVRY07</span></li>
 <li><strong>Invoice </strong>(Inbound). SAP SOAP InvoiceRequest &gt; <span style="background-color: rgb( 255 , 255 , 255 )">SAP IDOC INVOIC.INVOIC02</span></li>
</ul>
<p>As you can see, in this B2B scenario, the enterprise message types are based on SAP IDOC, and the trading partner-related message types are based on <span style="background-color: rgb( 255 , 255 , 255 )">SAP SOAP</span>. For all corresponding message types, the required Message Implementation Guidelines (MIGs) and the Mapping Guidelines (MAGs) are already available. Significantly, the Mapping Guidelines already contain the needed mapping elements between the corresponding nodes as required at a functional level.&nbsp;</p>
<p>The included MIGs (Message Implementation Guidelines), MAGs (Message Application Guidelines), and TPAs (Trading Partner Agreements) are provided solely as examples to guide users. They can be customised according to specific customer requirements. For the TPAs, this package includes one example TPA per message type as a reference.</p>
<p>&nbsp;</p>
<p><strong>Business Benefits&nbsp;</strong></p>
<p>As a user of the SAP Integration Suite's B2B capabilities, you can download the zip files of the design-time artefacts (TPA, MIGs and MAGs) provided on GitHub for your Purchase-to-Pay B2B scenario and import them directly into the Trading Partner Management and Integration Advisor systems of your SAP Integration Suite tenant. This not only saves you a lot of time, but also provides you with high-quality artefacts securing a high level of consistency based on standard conventions and rules. Instead of creating everything from scratch, simply use the B2B scenario and its associated guidelines and adapt them to your trading partners' and your own specific requirements, configure the communication channels, activate the TPAs, test, and finally go live with your trading partners.</p>

[Download the integration package](B2BIntegrationFactoryPurchasetoPayScenario.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/B2BIntegrationFactoryPurchasetoPayScenario)\
[View documentation](B2BIntegrationFactoryPurchasetoPayScenario.pdf)\
[View high level effort](effort.md)
## Integration flows