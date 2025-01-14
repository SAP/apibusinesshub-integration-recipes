# Reguvis Fachmedien Integration with SAP GTS

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Bundesanzeiger%20Integration%20with%20SAP%20S4HANA%20GTS%20OnPremise%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Bundesanzeiger%20Integration%20with%20SAP%20S4HANA%20GTS%20OnPremise%20 ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Bundesanzeiger%20Integration%20with%20SAP%20S4HANA%20GTS%20OnPremise%20 ) \|

![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) |
----|----|

Reguvis Fachmedien (formerly Bundesanzeiger) Integration with SAP GTS (Global Trade Services) 11.0 provides an integration to automatically upload data content of Sanctioned Party Lists of Reguvis Fachmedien to SAP GTS 11.0 in order to screen Business Partners and Transactional Documents.

Note: Please be aware that this package will only be valid until end of 2025 when SAP Global Trade Services 11.0 goes out of maintenance.

For later SAP GTS versions, such as SAP Global Trade Services, edition for SAP HANA 2023, the package will no longer run as it does not support the needed modification in the SAP GTS system.

This package enables consumption of the scenarios below:

1. Query and send SanctionsLists from Revugis Fachmedien to SAP GTS
2. Send SanctionsLists to SAP GTS
3. Trigger SanctionsLists processing in SAP GTS

[Download the reuseable integration package](ReguvisFachmedienIntegrationwithSAPGTS.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/BundesanzeigerIntegrationwithSAPS4HANAGTSOnPremise/overview)\
[View documentation](IntegrationBundesanzeigerandGTSviaCPI.pdf)\
[View high level effort estimate](effort.md)

## Integration Flows

### Query and send SanctionsLists from Revugis Fachmedien to SAP GTS
Query SanctionsLists from Revugis Fachmedien and mirror to SAP \
[View on SAP Business Accelerator Hub](https://api.sap.com/integrationflow/Query_and_send_SanctionsLists_from_Bundesanzeiger_to_SAP_S4_HANA_GTS_copy)
![Query and send SanctionsLists from Revugis Fachmedien to SAP GTS](Query-and-send-SanctionsLists-from-Bundesanzeiger-to-SAP-S4HANA-GTS.png)

### Send SanctionsLists to SAP GTS
Send SanctionsLists from Revugis Fachmedien and mirror to SAP GTS\
[View on SAP Business Accelerator Hub](https://api.sap.com/integrationflow/Send_SanctionsLists_to_SAP_S4_HANA_GTS_copy)
![Send SanctionsLists to SAP GTS](Send-SanctionsLists-to-SAP-S4-HANA-GTS.png)

### Trigger SanctionsLists processing in SAP GTS
iFlow to Trigger SanctionsLists processing on SAP GTS side (needs to run after "Query and send SanctionsLists from Bundesanzeiger to SAP GTS" iFlow)\
[View on SAP Business Accelerator Hub](https://api.sap.com/integrationflow/Trigger_SanctionsLists_processing_in_SAP_S4_HANA_GTS_copy)
![Trigger SanctionsLists processing in SAP GTS](Trigger-SanctionsLists-processing-in-SAP-S4HANA-GTS.png)
