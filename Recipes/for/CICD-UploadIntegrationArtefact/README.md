# CICD - Upload Integration Artefact

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Mayur Mohan Belur](https://github.com/mayurmohan.png?size=50 ) | [Mayur Mohan Belur](https://github.com/mayurmohan ) |
----|----|

This CICD Jenkins job allows you to checkout the latest version of the configured integration flow artefact from your source code repository and either update or create the artefact on the Cloud Integration tenant. Optionally you can also deploy the integration flow artefact and retrieve the deployment status.

A typical flow would look something like this:

 * You create a new integration flow on your Cloud Integration development tenant.
 * You download the integration flow into your source code repository to do security checks or to merge it with other resources that have been worked upon outside of Cloud Integration.
 * Using this job you can upload the integration flow to your Cloud Integration test tenant.
 * You perform some tests on your test tenant.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file.md)

### Environment Variables List
Configure the following environment variables before executing the Jenkins Job

Name|Example|Description
----|----|----
IntegrationFlowID| ```"IntegrationFlow1"``` | The ID of the integration artefact that shall be uploaded to the configured Cloud Integration tenant |
IntegrationPackage  | ```"CICD"``` | The ID of the integration package. In case the integration flow artefact does not exist in the Cloud Integration design time yet, for the first upload an integration package has to be specified, so that the integration flow can be added there. If it already exists in the design time, this parameter can be ignored. |
DeployFlow  | ```true``` <br/> ```false``` |Based on this flag, after the upload the integration flow artefact will be deployed to the Cloud Integration tenant runtime |
DeploymentCheckRetryCounter | ```20``` |Specify the maximum count of retries checking for a final deployment status as the deployment of the integration artefact might take a few seconds. Between each check we'll wait for 3 seconds |
CPIHost| "${env.CPI_HOST}" <br/> Neo: ```"xxxxxx-tmn.hci.eu2.hana.ondemand.com"``` <br/>CF: ```"xxxxxx.it-cpi001.cfapps.eu10.hana.ondemand.com"```| The host name (without HTTPS) of your Cloud Integration tenant |
CPIOAuthHost | "${env.CPI_OAUTH_HOST}" <br/>```"xxxxxx.authentication.sap.hana.ondemand.com"``` | The host name (without HTTPS) of the OAuth token server of your Cloud Integration tenant |
CPIOAuthCredentials | "${env.CPI_OAUTH_CRED}" <br/>       ```"CPIOAuthCredentials"``` | The alias of the OAuth credentials for the Cloud Integration tenant which is deployed on your build server (like Jenkins) |
GITRepositoryURL | "${env.GIT_REPOSITORY_URL}" <br/>```"github.com/CICD/integrations.git"```| The full URL of the source code repository without HTTPS |
GITCredentials | "${env.GIT_CRED}" <br/> ```"GIT_Credentials"``` |The alias of the source code repository credentials which is deployed on your build server (like Jenkins)|
GITBranch | "${env.GIT_BRANCH_NAME}" <br/> ```"refs/heads/master"``` |Specify the source code repository branch that you want to work with |
GITFolder | ```"IntegrationContent/IntegrationArtefacts"``` |Specify the folder structure in your source code repository from where you like to read the integration artefact |

## Related Recipes
* [CICD - Deploy Run Once Integration Artefact and Check MPL and Store If Success](../CICD-DeployRunOnceIntegrationArtefactAndCheckMplAndStoreIfSuccess)
* [CICD - Deploy Integration Artefact and Get Endpoint](../CICD-DeployIntegrationArtefactGetEndpoint)
* [CICD - Store Integration Artefact](../CICD-StoreIntegrationArtefact)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/02/ci-cd-for-sap-integration-suite-here-you-go/)
* [Cloud Integration OData APIs](https://api.sap.com/package/CloudIntegrationAPI?section=Artifacts)
