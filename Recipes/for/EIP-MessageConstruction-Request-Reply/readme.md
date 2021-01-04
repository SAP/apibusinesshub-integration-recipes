# EIPinCPI: Request-Reply

[Recipes by Topic](../../../../readme.md) | [Recipes by Author](../../../../author.md) | [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20EIPinCPI%3A%20Request-Reply) | [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20EIPinCPI%3A%20Request-Reply) | [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20EIPinCPI%3A%20Request-Reply)

![Bhalchandra Wadekar](https://github.com/BhalchandraSW.png?size=50) | [Bhalchandra Wadekar](../../../../author.md#bhalchandra-wadekar)
----|----

This recipe lets you try out Request-Reply pattern using an example of:
* Getting Products

[Download the integration flow Sample for getting products operation](Request-Reply%20-%20Getting%20Products.zip)

## Recipe

Step|Code|Why?
----|----|----
Start Immediately | |
Get Products | |
Log | ```messageLog.addAttachmentAsString('Products', message.getBody(String), 'application/xml')``` |

### Related Recipes
* [Return Address](../EIP-MessageConstruction-ReturnAddress/readme.md): This recipe lets you try out Return Address pattern.
* [Correlation Identifier](../EIP-MessageConstruction-CorrelationIdentifier\readme.md)| When sending the request, the sender adds a unique key to request. The receiver processes the request and puts the unique key as the Correlation Identifier in the reply message. Thus when a reply is received independent of the request, the sender knows the request corresponding the received reply based on the Correlation Identifier.
## References
* [EIPinCPI: Request-Reply](https://blogs.sap.com/2019/01/12/eipincpi-request-reply)

## Sample integration flows

### Getting Products

#### Integration Flow
![Getting Products](Request-Reply%20-%20Getting%20Products.png)

#### Sample Output
```
<Products>
    <Product>
      <CategoryID>1</CategoryID>
      <Discontinued>false</Discontinued>
      <SupplierID>1</SupplierID>
      <UnitPrice>18.0000</UnitPrice>
      <ProductName>Chai</ProductName>
      <QuantityPerUnit>10 boxes x 20 bags</QuantityPerUnit>
      <UnitsOnOrder>0</UnitsOnOrder>
      <ProductID>1</ProductID>
      <ReorderLevel>10</ReorderLevel>
      <UnitsInStock>39</UnitsInStock>
    </Product>
    <Product>
      <CategoryID>1</CategoryID>
      <Discontinued>false</Discontinued>
      <SupplierID>1</SupplierID>
      <UnitPrice>19.0000</UnitPrice>
      <ProductName>Chang</ProductName>
      <QuantityPerUnit>24 - 12 oz bottles</QuantityPerUnit>
      <UnitsOnOrder>40</UnitsOnOrder>
      <ProductID>2</ProductID>
      <ReorderLevel>25</ReorderLevel>
      <UnitsInStock>17</UnitsInStock>
    </Product>
    <Product>
      <CategoryID>2</CategoryID>
      <Discontinued>false</Discontinued>
      <SupplierID>1</SupplierID>
      <UnitPrice>10.0000</UnitPrice>
      <ProductName>Aniseed Syrup</ProductName>
      <QuantityPerUnit>12 - 550 ml bottles</QuantityPerUnit>
      <UnitsOnOrder>70</UnitsOnOrder>
      <ProductID>3</ProductID>
      <ReorderLevel>25</ReorderLevel>
      <UnitsInStock>13</UnitsInStock>
    </Product>
    <Product>
      <CategoryID>2</CategoryID>
      <Discontinued>false</Discontinued>
      <SupplierID>2</SupplierID>
      <UnitPrice>22.0000</UnitPrice>
      <ProductName>Chef Anton's Cajun Seasoning</ProductName>
      <QuantityPerUnit>48 - 6 oz jars</QuantityPerUnit>
      <UnitsOnOrder>0</UnitsOnOrder>
      <ProductID>4</ProductID>
      <ReorderLevel>0</ReorderLevel>
      <UnitsInStock>53</UnitsInStock>
    </Product>
    <Product>
      <CategoryID>2</CategoryID>
      <Discontinued>true</Discontinued>
      <SupplierID>2</SupplierID>
      <UnitPrice>21.3500</UnitPrice>
      <ProductName>Chef Anton's Gumbo Mix</ProductName>
      <QuantityPerUnit>36 boxes</QuantityPerUnit>
      <UnitsOnOrder>0</UnitsOnOrder>
      <ProductID>5</ProductID>
      <ReorderLevel>0</ReorderLevel>
      <UnitsInStock>0</UnitsInStock>
    </Product>
  </Products>
  ```
