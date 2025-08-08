# Message Transformation Utilities 

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Message%20Transformation%20Utilities) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Message%20Transformation%20Utilities)\| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Message%20Transformation%20Utilities) \| 

 ![SAP Business Accelerator Hub](https://github.com/SAPAPIBusinessHub.png?size=50 ) | [SAP Business Accelerator Hub](https://api.sap.com/allcommunity) | 
 ----|----| 

This package includes artifacts to support message payload transformation between formats like XML, plain text, CSV, and JSON, replicating functions available in SAP Process Orchestration Adapter Modules.

<p>This package is designed to provide an alternative solution for adapter modules and features previously used in SAP Process Orchestration (PO) to the new Cloud Integration. It includes a comprehensive suite of artifacts that facilitate the transformation of message payloads across various formats, such as XML, plain text, CSV, and JSON. The package ensures that the functionalities that were once part of SAP Process Orchestration are retained and enhanced in the Cloud Integration.</p>
<p>The utilities provided are:</p>
<ol>
 <li>MessageTransformBean.</li>
 <li>XMLAnonymizer.</li>
 <li>IDOCFlatConvertor.</li>
 <li>AddSOAPHeaderBean.</li>
 <li>PayloadSwapBean.</li>
 <li>TextCodepageConversionBean.</li>
 <li>ConvertCRLFfromToLF.</li>
 <li>ReplaceStringBean.</li>
 <li>JSONDataTypeConvertor</li>
</ol>

[Download the integration package](com.sap.integration.cloud.utilities.MessageTransformations.zip)\
[View package on the SAP Business Accelerator Hub](https://api.sap.com/package/com.sap.integration.cloud.utilities.MessageTransformations)\
[View documentation](com.sap.integration.cloud.utilities.MessageTransformations.pdf)\
[View high level effort](effort.md)
## Integration flows
### IDOCFlatConvertor - Flat2XML - TC1 
Transforming Flat messages to IDOC-XML \
 ![input-image](IDOCFlatConvertor_-_TC.png)
### JSONDataTypeConvertor - TC1 - Dynamic Conversion 
Uses script ‘JSONDataTypeConvertor’ to convert JSON properties in a payload from String to Integer, Decimal and Boolean with dynamic determination. \
 ![input-image](JSONDataTypeConvertor_-_TC1_-_Dynamic_Conversion.png)
### MessageTransformBean - SimplePlain2XML - TC3 
Transforming plain text messages to XML where there is a fixed field length with headers not available. \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimplePlain2XML.TC3.png)
### PayloadSwapBean - TC3 
Read attachment from Form-Data, by Content-Type. \
 ![input-image](PayloadSwapBean_-_TC3.png)
### PayloadSwapBean - TC2 
Read attachment from Form-Data, by name. \
 ![input-image](PayloadSwapBean_-_TC2.png)
### ReplaceStringBean - TC2 
Replace text in payload from one value to another. \
 ![input-image](ReplaceStringBean_-_TC2.png)
### MessageTransformBean - StructurePlainToXML - TC1 
Transforming structure plain text messages to XML with a fixed field length, without headers \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructurePlainToXML.TC1.png)
### MessageTransformBean - SimplePlain2XML - TC2 
Transforming plain text messages to XML where there is a fixed field length without headers \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimplePlain2XML.TC2.png)
### MessageTransformBean - SimpleXMLToPlain - TC2 
Transforming XML to plain text messages with a separator and headers payload \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimpleXMLToPlain.TC2.png)
### XMLAnonymizer - Remove Prefix 
Remove the prefix of the given namespace from an incoming XML message \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.XMLAnonymizer.RemovePrefix.TC1.png)
### MessageTransformations Utilities 
Script Collection containing the converted Adapter Modules from SAP Process Orchestration. \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.Utilities.png)
### MessageTransformBean - StructurePlainToXML - TC5 
Transforming structure plain text messages to XML FFL , without headers Filter 2E \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructurePlainToXML.TC5.png)
### AddSOAPHeaderBean - TC1 
Add custom SOAP headers to SOAP Message. \
 ![input-image](com.sap.integration.cloud.guidelines.MessageTransformations.AddSOAPHeaderBean.TC1.png)
### JSONDataTypeConvertor - TC3 - Decimal to Integer 
Uses script ‘JSONDataTypeConvertor’ to convert JSON properties in a payload from String to Integer or Decimal. \
 ![input-image](JSONDataTypeConvertor_-_TC3_-_Decimal_to_Integer.png)
### ReplaceStringBean - TC1 
Replace text in payload from one value to another. \
 ![input-image](ReplaceStringBean_-_TC1.png)
### TextCodepageConversionBean - TC1 
Convert Text payload from one encoding to another. \
 ![input-image](TextCodepageConversionBean_-_TC1.png)
### XMLAnonymizer - Rename Namespaces 
Rename the prefix of certain namespaces from an incoming XML message \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.XMLAnonymizer.RenameNamespaces.TC2.png)
### MessageTransformBean - SimpleXMLToPlain - TC3 
Transforming XML to plain text messages with a fixed field length, without headers \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimpleXMLToPlain.TC3.png)
### MessageTransformBean - StructurePlainToXML - TC3 
Transforming structure plain text messages to XML FFL with a separator, without headers \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructurePlainToXML.TC3.png)
### IDOCFlatConvertor 
Transform IDOC files, from XML to Flat and from Flat to XML. \
 ![input-image](IDOCFlatConvertor.png)
### MessageTransformBean - SimpleXMLToPlain - TC1 
Transforming XML to plain text messages with a separator and headers \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimpleXMLToPlain.TC1.png)
### MessageTransformBean - StructureXMLToPlain - TC1 
Transforming structure XML to plain text messages with headers payload \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructureXMLToPlain.TC1.png)
### MessageTransformBean - StructurePlainToXML - TC4 
Transforming structure plain text messages to XML FFL with a separator, with headers \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructurePlainToXML.TC4.png)
### PayloadSwapBean - TC1 
Read attachment from Email, by name. \
 ![input-image](PayloadSwapBean_-_TC1.png)
### IDOCFlatConvertor - XML2Flat - TC2 
Transforming IDOC-XML messages to Flat \
 ![input-image](IDOCFlatConvertor_-_XML2Flat_-_TC2.png)
### JSONDataTypeConvertor - TC2 - SpecificTypes 
Uses script ‘JSONDataTypeConvertor’ to convert JSON properties in a payload from String to Integer, Decimal and Boolean. \
 ![input-image](JSONDataTypeConvertor_-_TC2_-_SpecificTypes.png)
### MessageTransformBean - SimplePlain2XML - TC5 
Transforming plain text messages to XML where there is a separator and with headers. \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimplePlain2XML.TC5.png)
### MessageTransformBean - SimplePlain2XML - TC4 
Transforming plain text messages to XML where there is a separator and no headers. \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimplePlain2XML.TC4.png)
### MessageTransformBean - StructurePlainToXML - TC6 
Transforming structure plain text messages to XML FFL , without headers Filter 1E \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructurePlainToXML.TC6.png)
### MessageTransformBean - StructurePlainToXML - TC2 
Transforming structure plain text messages to XML with a separator, without headers \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructurePlainToXML.TC2.png)
### MessageTransformBean - StructurePlainToXML - TC7 
Transforming structure plain text messages to XML FFL, Single Object \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructurePlainToXML.TC7.png)
### ConvertCRLFfromToLF - TC1 
Convert Text payload from one encoding to another. \
 ![input-image](ConvertCRLFfromToLF_-_TC1.png)
### MessageTransformBean - SimplePlain2XML - TC1 
Transforming plain text messages to XML where there is a fixed field length with headers. \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.SimplePlain2XML.TC1.png)
### MessageTransformBean - StructureXMLToPlain - TC2 
Transforming structure XML to plain text messages with fixed field length and headers properties. \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructureXMLToPlain.TC2.png)
### MessageTransformBean - StructureXMLToPlain - TC3 
Transforming structure XML to plain text messages with Separator Filter \
 ![input-image](com.sap.integration.cloud.utilities.MessageTransformations.MessageTransformBean.StructureXMLToPlain.TC3.png)