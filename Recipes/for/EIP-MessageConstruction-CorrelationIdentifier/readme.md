# EIPinCPI: Correlation Identifier

[Recipes by Topic](../../../../readme.md) | [Recipes by Author](../../../../author.md) | [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20EIPinCPI%3A%20Correlation%20Identifier) | [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20EIPinCPI%3A%20Correlation%20Identifier) | [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20EIPinCPI%3A%20Correlation%20Identifier)

![Bhalchandra Wadekar](https://github.com/BhalchandraSW.png?size=50) | [Bhalchandra Wadekar](../../../../author.md#bhalchandra-wadekar)
----|----

This recipe lets you try out Correlation Identifier pattern using AMQP Adapter.\

When sending the request, the sender adds a unique key to request. The receiver processes the request and puts the unique key as the Correlation Identifier in the reply message. Thus when a reply is received independent of the request, the sender knows the request corresponding the received reply based on the Correlation Identifier.\

This pattern is a variant of the [Request Reply](../EIP-MessageConstruction-Request-Reply/readme.md) pattern.

[Download the integration flow for Enqueueing Customer](Correlation%20Identifier%20-%20Enqueueing%20Customer.zip)\
[Download the integration flow for Processing Customer](Correlation%20Identifier%20-%20Processing%20Customer.zip)\
[Download the integration flow for Processing Response](Correlation%20Identifier%20-%20Processing%20Response.zip)

## Recipe

Step|Code|Why?
----|----|----
Enqueue Customer | |
Process Customer | Content Modifier step ‘Prepare Response Message’ has the following addition to the ‘Message Header’ tab. \| **Action**: *Create* \| **Name**: *JMSCorrelationID*	\| **Type**:*String* \|	Data Type	\| **Value** : */Customer/Id*	\| **Default**:  \| | add the Correlation Identifier by setting header ‘JMSCorrelationID’ as Customer’s Id
Process Response |[see code](#Sample-Code-for-Process-Response )|

### Related Recipes
* [Request Reply](../EIP-MessageConstruction-Request-Reply/readme.md): This recipe retrieves a list of products using the Request-Reply pattern
* [Return Address](../EIP-MessageConstruction-ReturnAddress/readme.md): This recipe lets you try out Return Address pattern

## References
* [EIPinCPI: Correlation Identifier](https://blogs.sap.com/2020/01/26/eipincpi-correlation-identifier)
* [Correlation Identifier Pattern in Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/patterns/messaging/CorrelationIdentifier.html)

## Sample integration flows
### Sample Code for Process Response
```import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    def messageLog = messageLogFactory.getMessageLog(message)
    messageLog.addAttachmentAsString('Response for Customer: ' + message.getHeader('JMSCorrelationID', String), message.getBody(String), null)

    return message
}```

### Sample Output
![Output](Correlation%20Identifier%20-%20Output.png)
