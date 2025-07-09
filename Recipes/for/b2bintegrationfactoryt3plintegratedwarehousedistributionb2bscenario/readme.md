# B2B Integration Factory - Transportation Management & 3PL - B2B Scenario 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20B2B%20Integration%20Factory%20-%20Transportation%20Management%20&%203PL%20-%20B2B%20Scenario) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20B2B%20Integration%20Factory%20-%20Transportation%20Management%20&%203PL%20-%20B2B%20Scenario)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20B2B%20Integration%20Factory%20-%20Transportation%20Management%20&%203PL%20-%20B2B%20Scenario) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

Pre-built content for SAP Integration Suite's B2B capabilities Trading Partner Management & Integration Advisor, covering the TM & 3PL B2B scenario with the choreography of message exchange between SAP S/4HANA On-Premise IDOC & GS1XML standards.

<p><strong>Business Challenge</strong></p>
<p>When using SAP Integration Suite's B2B capabilities, the creation of a Trading Partner Agreement (TPA) for an Order-to-Cash B2B scenario, where the business partner uses GS1XML and the internally supported format is SAP IDOC, requires functional experts with domain knowledge, integration experts to create and enable these TPAs, and above all, it requires time and efforts for implementation. Very often, neither the required knowledge nor the time is available.&nbsp;</p>
<p>&nbsp;</p>
<p><strong>Value Story&nbsp;</strong></p>
<p>The B2B Integration Factory creates this content of B2B scenarios as pre-packaged content in the form of trading partner agreements and the corresponding message implementation and mapping guidelines and makes them available accordingly. This B2B scenario now describes the electronic data exchange of the first set of relevant 3PL business data, such as &nbsp;</p>
<ol>
 <li><strong>Material Master</strong>&nbsp;(Outbound): SAP IDoc MATMAS.MATMAS05&nbsp; &nbsp; &gt; GS1 XML itemDataNotificationMessage</li>
 <li><strong>Inbound Delivery</strong>&nbsp;(Outbound): SAP IDoc DESADV.DELVRY07&nbsp; &nbsp; &nbsp;&gt; GS1 XML warehousingInboundInstructionMessage</li>
 <li><strong>Receiving Finished</strong>&nbsp;(Inbound): GS1 XML warehousingInboundNotificationMessage &gt; SAP IDoc MBGMCR.MBGMCR03</li>
 <li><strong>Outbound Delivery</strong>&nbsp;(Outbound): SAP IDoc DESADV.DELVRY07&nbsp; &gt; GS1 XML warehousingOutboundInstructionMessage&nbsp;</li>
 <li><strong>Product Picked</strong>&nbsp;(Inbound): GS1 XML warehousingOutboundNotificationMessage&nbsp; &nbsp; &nbsp;&gt; SAP IDoc WHSCON.DELVRY07</li>
 <li><strong>Customer Return</strong>&nbsp;(Outbound): SAP IDoc DESADV.DELVRY07&nbsp; &nbsp; &nbsp;&gt; GS1 XML warehousingInboundInstructionMessage</li>
 <li><strong>Customer Return Receiving Finished&nbsp;</strong>(Inbound): GS1 XML warehousingInboundNotificationMessage &gt; SAP IDoc WHSCON.DELVRY07</li>
 <li><strong>Outbound Delivery Status Update&nbsp;</strong>(Inbound): GS1 XML warehousingOutboundNotificationMessage&nbsp; &nbsp;&gt; SAP IDoc WHSCON.DELVRY07</li>
 <li><strong>Stock Comparison&nbsp;</strong>(Inbound): GS1 XML logisticsInventoryReportMessage&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &gt; SAP IDoc ZMMSTCOMP.ZMMSTOCK</li>
 <li><strong>Goods Movement&nbsp;</strong>(Inbound): GS1 XML logisticsInventoryReportMessage &gt; SAP IDoc MBGMCR.MBGMCR03</li>
 <li><strong>Inbound Delivery Create&nbsp;</strong>(Inbound): GS1 XML warehousingInboundNotificationMessage &gt; SAP IDoc DESADV.DELVRY07.ZDELVRY07</li>
 <li><strong>Outbound Delivery Create&nbsp;</strong>(Outbound): SAP IDoc DESADV.DELVRY07 &gt; GS1 XML warehousingOutboundInstruction</li>
</ol>
<p>&nbsp;</p>
<p>It covers next to the 3PL -IWD B2B Scenarios also the following Transportation Order requests:</p>
<ol>
 <li>Delivery Notification (Inbound): UN/EDIFACT DESADV D.10B &gt; SAP SOAP TransportationOrderGenericRequest 2302</li>
 <li>Delivery Notification (Outbound): SAP SOAP TransportationOrderGenericRequest 2302&nbsp;&gt;&nbsp;UN/EDIFACT DESADV D.10B</li>
</ol>
<p>As you can see, in this B2B scenario, the enterprise message types are based on SAP IDOC, and the trading partner-related message types are based on GS1XML. For all corresponding message types, the required Message Implementation Guidelines (MIGs) and the Mapping Guidelines (MAGs) are already available. Significantly, the Mapping Guidelines already contain the needed mapping elements between the corresponding nodes as required at a functional level.&nbsp;</p>
<p>The included MIGs (Message Implementation Guidelines), MAGs (Message Application Guidelines), and TPAs (Trading Partner Agreements) are provided solely as examples to guide users. They can be customised according to specific customer requirements. For the TPAs, this package includes one example TPA per message type as a reference.</p>
<p>&nbsp;</p>
<p><strong>Business Benefits&nbsp;</strong></p>
<p>As a user of the SAP Integration Suite's B2B capabilities, you can download the zip files of the design-time artefacts (TPA, MIGs and MAGs) provided on GitHub for your 3PL B2B scenario and import them directly into the Trading Partner Management and Integration Advisor systems of your SAP Integration Suite tenant. This not only saves you a lot of time, but also provides you with high-quality artefacts securing a high level of consistency based on standard conventions and rules. Instead of creating everything from scratch, simply use the B2B scenario and its associated guidelines and adapt them to your trading partners' and your own specific requirements, configure the communication channels, activate the TPAs, test, and finally go live with your trading partners.</p>

[Download the integration package](B2BIntegrationFactoryTM3PLIntegratedWarehouseDistributionB2BScenario.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/B2BIntegrationFactoryTM3PLIntegratedWarehouseDistributionB2BScenario)\
[View documentation](B2BIntegrationFactoryTM3PLIntegratedWarehouseDistributionB2BScenario.pdf)\
[View high level effort](effort.md)
