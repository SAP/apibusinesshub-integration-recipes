# CICD - Deploy Integration Artefact and Get Endpoint

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Axel Albrecht](https://github.com/axelalbrechtsap.png?size=50 ) | [Axel Albrecht](https://github.com/axelalbrechtsap ) |
----|----|

This CICD Jenkins job allows you to deploy an existing integration flow on Cloud Integration runtime. Optionally you can also get the endpoint of the integration flow.

 A typical flow would look something like this:

 * Create or upload an integration flow to your Cloud Integration tenant.
 * Deploy the integration flow using this pipeline job.
 * Either the integration flow starts processing a message immediately or you can send a message to the endpoint retrieved from the job.
 * Check the message processing log if the run was successful or not.
 * Do some other activities based on the result.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file/readme.md)

### Environment Variables List
Configure the following environment variables before executing the pipeline job

Name|Example|Description
----|----|----
IntegrationFlowID| ```"IntegrationFlow1"``` | The ID of the integration artefact that shall be deployed to the configured Cloud Integration tenant |
GetEndpoint | ```true``` <br/> ```false``` | Specify if you like to retrieve the endpoint of the deployed integration artefact. If you don't need the endpoint or the artefact does not provide an endpoint, set the value to false |
DeploymentCheckRetryCounter | ```20``` | Specify the maximum count of retries checking for a final deployment status as the deployment of the integration artefact might take a few seconds. Between each check we'll wait 3 seconds |
CPIHost| "${env.CPI_HOST}" <br/> Neo: ```"xxxxxx-tmn.hci.eu2.hana.ondemand.com"``` <br/>CF: ```"xxxxxx.it-cpi001.cfapps.eu10.hana.ondemand.com"```| The host name (without HTTPS) of your Cloud Integration tenant |
CPIOAuthHost | "${env.CPI_OAUTH_HOST}" <br/>```"xxxxxx.authentication.sap.hana.ondemand.com"``` | The host name (without HTTPS) of the OAuth token server of your Cloud Integration tenant |
CPIOAuthCredentials | "${env.CPI_OAUTH_CRED}" <br/>       ```"CPIOAuthCredentials"``` | The alias of the OAuth credentials for the Cloud Integration tenant which is deployed on your build server (like Jenkins) |

## Related Recipes
* [CICD - Upload Integration Artefact](../CICD-UploadIntegrationArtefact)
* [CICD - Get Latest Message Processing Log](../CICD-GetLatestMessageProcessingLog)
* [CICD - Undeploy Integration Artefact](../CICD-UndeployIntegrationArtefact)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/02/ci-cd-for-sap-integration-suite-here-you-go/)
* [Cloud Integration OData APIs](https://api.sap.com/package/CloudIntegrationAPI?section=Artifacts)
