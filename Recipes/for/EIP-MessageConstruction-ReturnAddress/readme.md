# EIPinCPI: Return Address

[Recipes by Topic](../../../../readme.md) | [Recipes by Author](../../../../author.md) | [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20EIPinCPI%3A%20Return%20Address) | [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20EIPinCPI%3A%20Return%20Address) | [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20EIPinCPI%3A%20Return%20Address)

![Bhalchandra Wadekar](https://github.com/BhalchandraSW.png?size=50) | [Bhalchandra Wadekar](../../../author.md#bhalchandra-wadekar)
----|----

This recipe lets you try out Return Address pattern using AMQP Adapter.

[Download the integration flow for Enqueueing Customer](Return%20Address%20-%20Enqueueing%20Customer.zip)\
[Download the integration flow for Processing Customer](Return%20Address%20-%20Processing%20Customer.zip)\
[Download the integration flow for Processing Response](Return%20Address%20-%20Processing%20Response.zip)

## Recipe

Step|Code|Why?
----|----|----
Enqueue Customer | |
Process Customer | |
Process Response | |

### Related Recipes
* [Request Reply](../EIP-MessageConstruction-Request-Reply/readme.md): This recipe retrieves a list of products using the Request-Reply pattern
* [Correlation Identifier](../EIP-MessageConstruction-CorrelationIdentifier/readme.md)| When sending the request, the sender adds a unique key to request. The receiver processes the request and puts the unique key as the Correlation Identifier in the reply message. Thus when a reply is received independent of the request, the sender knows the request corresponding the received reply based on the Correlation Identifier.
## References
* [EIPinCPI: Return Address](https://blogs.sap.com/2020/01/19/eipincpi-return-address)

## Sample integration flows

### Enqueueing Customer

#### Integration Flow
![Enqueueing Customer](Return%20Address%20-%20Enqueueing%20Customer.png)

### Processing Customer

#### Integration Flow
![Processing Customer](Return%20Address%20-%20Processing%20Customer.png)

### Processing Response

#### Integration Flow
![Processing Response](Return%20Address%20-%20Processing%20Response.png)

### Sample Input
```
<Customer>
    <Id>1</Id>
    <Name>SAP</Name>
</Customer>
```

### Sample Output
```
<Response>
    <Result>SUCCESS</Result>
</Response>
```
