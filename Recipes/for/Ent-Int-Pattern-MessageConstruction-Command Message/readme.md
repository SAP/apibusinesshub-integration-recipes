# EIPinCPI: Command Message

[Recipes by Topic](../../../../readme.md) | [Recipes by Author](../../../../author.md) | [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20EIPinCPI%3A%20Command%20Message) | [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20EIPinCPI%3A%20Command%20Message) | [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20EIPinCPI%3A%20Command%20Message)

![Bhalchandra Wadekar](https://github.com/BhalchandraSW.png?size=50) | [Bhalchandra Wadekar](https://github.com/BhalchandraSW)
----|----

This recipe lets you try out Command Message pattern using three examples:
* Invoking a SOAP operation
* Invoking an OData function import
* Invoking a BAPI

[Download the integration flow Sample for invoking a SOAP operation](Command%20Message%20-%20Invoking%20a%20SOAP%20operation.zip)\
[Download the integration flow Sample for invoking an OData function import](Command%20Message%20-%20Invoking%20an%20OData%20function%20import.zip)\
[Download the integration flow Sample for invoking a BAPI](Command%20Message%20-%20Invoking%20a%20BAPI.zip)

## Recipe

Step|Code|Why?
----|----|----
Start immediately | |
Prepare Command Message | |
Command | |
Log | ```messageLog.addAttachmentAsString('Response', message.getBody(String), 'application/xml')``` |

## References
* [EIPinCPI: Command Message](https://blogs.sap.com/2019/12/22/eipincpi-command-message)

## Sample integration flows

### Invoking a SOAP operation

#### Integration Flow
![Invoking a SOAP operation](Command%20Message%20-%20Invoking%20a%20SOAP%20operation.png)

#### Sample Input
```
<FahrenheitToCelsius xmlns="https://www.w3schools.com/xml/">
  <Fahrenheit>37</Fahrenheit>
</FahrenheitToCelsius>
```

#### Sample Output
```
<FahrenheitToCelsiusResponse xmlns="https://www.w3schools.com/xml/" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <FahrenheitToCelsiusResult>2.77777777777778</FahrenheitToCelsiusResult>
</FahrenheitToCelsiusResponse>
```

### Invoking an OData function import

#### Integration Flow
![Invoking an OData function import](Command%20Message%20-%20Invoking%20an%20OData%20function%20import.png)

#### Sample Input
![Sample Input for invoking an OData function import](Sample%20Input%20for%20invoking%20an%20OData%20function%20import.png)

#### Sample Output
```
<ProductDrafts>
  <ProductDraft>
    <Description>PC multimedia speakers - 10 Watt (Total) - 2-way</Description>
    <CreatedAt>2019-12-22T10:02:07.446</CreatedAt>
    <WeightUnit>KGM</WeightUnit>
    <ProductId>EPM-033190</ProductId>
    <DimensionUnit>CMT</DimensionUnit>
    <Name>Blaster Extreme</Name>
    <LastModified>2019-12-22T10:02:07.446</LastModified>
    <CurrencyCode>USD</CurrencyCode>
    <DimensionWidth>13.000</DimensionWidth>
    <WeightMeasure>1.400</WeightMeasure>
    <MainCategoryName>Computer Components</MainCategoryName>
    <CreatedBy>P1941728552</CreatedBy>
    <MainCategoryId>Computer Components</MainCategoryId>
    <SubCategoryId>Speakers</SubCategoryId>
    <ImageUrl>/sap/public/bc/NWDEMO_MODEL/IMAGES/HT-1091.jpg</ImageUrl>
    <DimensionHeight>17.500</DimensionHeight>
    <IsNewProduct>true</IsNewProduct>
    <SubCategoryName>Speakers</SubCategoryName>
    <SupplierId>100000000</SupplierId>
    <Price>26.00</Price>
    <SupplierName>SAP</SupplierName>
    <Id>EPM-033190</Id>
    <IsDirty>false</IsDirty>
    <ExpiresAt>2019-12-22T10:32:07.446</ExpiresAt>
    <DimensionDepth>11.000</DimensionDepth>
    <QuantityUnit>EA</QuantityUnit>
  </ProductDraft>
</ProductDrafts>
```

### Invoking a BAPI

#### Integration Flow
![Invoking a BAPI](Command%20Message%20-%20Invoking%20a%20BAPI.png)

#### Sample Input
```
<rfc:BAPI_CURRENCY_GETDECIMALS xmlns:rfc="urn:sap-com:document:sap:rfc:functions">
  <CURRENCY>GBP</CURRENCY>
</rfc:BAPI_CURRENCY_GETDECIMALS>
```

#### Sample Output
```
<rfc:BAPI_CURRENCY_GETDECIMALS.Response xmlns:rfc="urn:sap-com:document:sap:rfc:functions">
  <CURRENCY_DECIMALS>
    <CURRENCY>GBP</CURRENCY>
    <CURDECIMALS>2</CURDECIMALS>
    <CURRENCY_ISO>GBP</CURRENCY_ISO>
  </CURRENCY_DECIMALS>
  <RETURN>
    <TYPE/>
    <CODE/>
    <MESSAGE/>
    <LOG_NO/>
    <LOG_MSG_NO>000000</LOG_MSG_NO>
    <MESSAGE_V1/>
    <MESSAGE_V2/>
    <MESSAGE_V3/>
    <MESSAGE_V4/>
  </RETURN>
</rfc:BAPI_CURRENCY_GETDECIMALS.Response>
```
