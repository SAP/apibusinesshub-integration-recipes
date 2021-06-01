# CICD - Store Integration Artefact

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Axel Albrecht](https://github.com/axelalbrechtsap.png?size=50 ) | [Axel Albrecht](https://github.com/axelalbrechtsap ) |
----|----|

This CICD Jenkins job simply enables you to download an integration artefact from the Cloud Integration tenant and store it in your source code repository like Git.

 A typical work flow would look something like this:

 * You build an integration flow on your Cloud Integration tenant.
 * Using this job you download the integration artefact and store it in your source code repository.
 * You do activities like security scans on the resources or can compare versions.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file.md)

### Environment Variables List
Configure the following environment variables before executing the pipeline job

Name|Example|Description
----|----|----
IntegrationFlowID| ```"IntegrationFlow1"``` | The ID of the integration artefact that needs to be stored in the source code repository  |
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
* [CICD - Get Latest Message Processing Log](../CICD-GetLatestMessageProcessingLog)
* [CICD - Get Specific Message Processing Log](../CICD-GetSpecificMessageProcessingLog)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CI/CD Blog](https://blogs.sap.com/2021/06/01/ci-cd-for-sap-integration-suite-here-you-go/)
* [Cloud Integration OData APIs](https://api.sap.com/package/CloudIntegrationAPI?section=Artifacts)
