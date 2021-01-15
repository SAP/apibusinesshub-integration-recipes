# Fixing bad request when calling a Cloud Integration endpoint from another integration flow

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Fixing-bad-request-when-calling-a-Cloud-Integration-endpoint-from-another-integration-flow ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Fixing-bad-request-when-calling-a-Cloud-Integration-endpoint-from-another-integration-flow ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Fixing-bad-request-when-calling-a-Cloud-Integration-endpoint-from-another-integration-flow ) \|


![Meghna Shishodiya](https://github.com/author-profile.png?size=50 ) | [Meghna Shishodiya](https://github.com/author-profile ) |
----|----|

This recipe shows how to solve a *bad request* error

## Recipe

**Motivation:**

Bad request when calling a Cloud Integration endpoint to integration flow.

**Guidelines:**

HTTP headers of size more than 8kb result in the above error. This is specifically relevant when you try to send the payload as a header value between 2 iflows. The servlet engine on the target HCI machine rejects the request if any one of the header is more than 8kb. Remove such headers before any such inter-iflow communication is expected.

**Recommendation:**

If you need to send the xml to second flow, merge it to the body and send it as one object.
