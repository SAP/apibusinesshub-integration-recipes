# CICD - Deploy Run Once Integration Artefact and Check MPL and Store If Success

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Mayur Mohan Belur](https://github.com/mayurmohan.png?size=50 ) | [Mayur Mohan Belur](https://github.com/mayurmohan ) |
----|----|

This CICD Jenkins job allows you to deploy an integration flow, check its deployment status and the MPL (message processing log) status of the message that gets automatically performed after the deployment due to a scheduler configuration or the consumption of files from an (S)FTP server or messages from a JMS queue.

In case of a successful message processing the job then downloads the integration flow artefact from the Cloud Integration tenant and commits it to the source code repository.
In case of a failure during deployment or during message processing the job will provide you the error details.

A typical work flow would look something like this:

 * You have uploaded an integration flow to your Cloud Integration test tenant.
 * Using this job the integration artefact will be deployed and a message will automatically be launched.
 * The job will provide you the log status.
 * If the message processing was successful the job will download the integration flow and store it into your source code repository.
 * The stored integration flow can be deployed to a productive Cloud Integration tenant.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file.md)

### Environment Variables List
Configure the following environment variables before executing the Jenkins Job

Name|Example|Description
----|----|----
IntegrationFlowID| ```"IntegrationFlow1"``` |The ID of the integration artefact that shall be deployed to the configured Cloud Integration tenant |
FailJobOnFailedMPL | ```true``` <br/> ```false``` |Specify if the job should fail in case the status of the retrieved message processing log is Failed or Retry. If you are doing negative testing and you're expecting the integration artefact run to fail, set this to "false" |
DeploymentCheckRetryCounter | ```20``` |Specify the maximum count of retries checking for a final deployment status as the deployment of the integration artefact might take a few seconds. Between each check we'll wait for 3 seconds|
MPLCheckRetryCounter | ```10``` <br/>  |Specify the maximum count of retries for checking the final MPL status as the process might run for a while. Between each check we'll wait for 3 seconds. |
CPIHost| "${env.CPI_HOST}" <br/> Neo: ```"xxxxxx-tmn.hci.eu2.hana.ondemand.com"``` <br/>CF: ```"xxxxxx.it-cpi001.cfapps.eu10.hana.ondemand.com"```| The host name (without HTTPS) of your Cloud Integration tenant |
CPIOAuthHost | "${env.CPI_OAUTH_HOST}" <br/>```"xxxxxx.authentication.sap.hana.ondemand.com"``` | The host name (without HTTPS) of the OAuth token server of your Cloud Integration tenant |
CPIOAuthCredentials | "${env.CPI_OAUTH_CRED}" <br/>       ```"CPIOAuthCredentials"``` | The alias of the OAuth credentials for the Cloud Integration tenant which is deployed on your build server (like Jenkins) |
GITRepositoryURL | "${env.GIT_REPOSITORY_URL}" <br/>```"github.com/CICD/integrations.git"```| The full URL of the source code repository without HTTPS |
GITCredentials | "${env.GIT_CRED}" <br/> ```"GIT_Credentials"``` |The alias of the source code repository credentials which is deployed on your build server (like Jenkins)|
GITBranch | "${env.GIT_BRANCH_NAME}" <br/> ```"refs/heads/master"``` |Specify the source code repository branch that you want to work with |
GITFolder | ```"IntegrationContent/IntegrationArtefacts"``` |Specify the folder structure in your source code repository where you like to store the integration artefact |
GITComment | ```"Integration Artefacts update from CICD pipeline"``` |Specify the text to be used during check-in to your source code repository |

## Related Recipes
* [CICD - Upload Integration Artefact](../CICD-UploadIntegrationArtefact)
* [CICD - Undeploy Integration Artefact](../CICD-UndeployIntegrationArtefact)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/01/ci-cd-for-sap-integration-suite-here-you-go/)
* [Cloud Integration OData APIs](https://api.sap.com/package/CloudIntegrationAPI?section=Artifacts)
