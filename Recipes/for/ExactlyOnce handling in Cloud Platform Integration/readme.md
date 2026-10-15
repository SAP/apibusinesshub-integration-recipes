# ExactlyOnce handling in Cloud Platform Integration

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20ExactlyOnce-handling-in-Cloud-Platform-Integration ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20ExactlyOnce-handling-in-Cloud-Platform-Integration ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20ExactlyOnce-handling-in-Cloud-Platform-Integration ) \|

![Meghna Shishodiya](https://github.com/author-profile.png?size=50 ) | [Meghna Shishodiya](https://github.com/author-profile ) |
----|----|

In this recipe we will show, how enable ExactlyOnce message processing through modelling.

[Download the integration flow Sample](ExactlyOnce.zip)

## Recipe

**Motivation:**

Cloud Integration now provides building blocks for retry and duplicate handling. SAP's [Quality of Service guidance](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/quality-of-service) explains that the end-to-end guarantee depends on the protocols and participating systems. Exactly Once is not a blanket guarantee for every integration flow.

For asynchronous delivery, consider [JMS queues](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/use-case-for-jms): an ingress flow persists a message using the JMS receiver adapter, and a delivery flow consumes it using the JMS sender adapter. Transfer failures can then trigger broker retries.

To suppress repeated successful processing, consider the [Idempotent Process Call](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/define-idempotent-process-call). It records completion only after the called local process succeeds. A timeout after the receiver has committed can still cause duplicate side effects; use receiver-side idempotency with a stable business message ID for an end-to-end Exactly Once design.

The data-store pattern below is a historical modeling alternative. It requires explicit retry, cleanup, monitoring, and receiver-side duplicate handling; persistence alone does not guarantee Exactly Once. Check current retention settings on your tenant instead of relying on historical defaults.

**Design** your flows as follows:

**First flow:**

The main processing of the message happens here.
Once all the processing is over, add process calls, one per receiver. You need one datastore per receiver.

In order to put messages for different Receivers into different datastores, create a sub-process for transmitting the message to each receiver – the exception sub-process of the sub-process will push the message to the corresponding datastore. Ensure that you use different names for datastore of each receiver –each sub-process must write to a different datastore.
Please note that this explanation does not cover the specifics of exception handling.
For more details, please refer to Discover --> Cloud Integration – Exemplars --> Documents --> Exception Handling.


 ![Config Image](Config.jpg)

  ![ConfigSubProcess Image](ConfigSubProcess.jpg)

  ![DataStore Config Image](DataStoreConfig.jpg)

**Second Flow:**

Have a second scheduled flow that runs every hour or 5 mins, depending on your business requirement. This process will trigger **other sub-processes** – one per receiver of the previous flow.

![modeling3](modeling3.png)

Each sub-process shall check if there is an entry for the corresponding receiver in the datastore for that receiver:

If none, the flow would exit.

If the system finds a datastore entry, the sub-flow will try to resend the message. On successful transmission of the message, the sub-flow shall delete the message from its datastore. Else, it will stay in the datastore for the next retry.

 ![modeling4](modeling4.png)

Retain the persisted message until successful delivery is confirmed. Deleting it before delivery and reinserting it on failure introduces a loss window if processing stops between those operations. Review transaction boundaries and test recovery before adopting this pattern.


### Related Recipes
* [Enabling Exactly Once in Order via Cloud Integration](../enablingexactlyonceinorderviacloudintegration)
* [Data Store Operations](../Data%20Store%20Operations)

### Verification

In a non-production environment, resend the same business message ID after success, force a receiver failure and retry, and simulate an ambiguous timeout after receiver processing. Check receiver records as well as message processing logs. Verify recovery separately for each receiver.
