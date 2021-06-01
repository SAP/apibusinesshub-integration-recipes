# CICD - Undeploy Integration Artefact

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Axel Albrecht](https://github.com/axelalbrechtsap.png?size=50 ) | [Axel Albrecht](https://github.com/axelalbrechtsap ) |
----|----|

This CICD Jenkins job simply enables you to undeploy an integration artefact from the Cloud Integration tenant.

 A typical work flow would look something like this:

 * You build an integration flow on your Cloud Integration tenant.
 * You deploy the integration flow and perform some tests.
 * Based on the job results you can take certain actions.
 * Using this job, the integration artefact will be undeployed from the tenant.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file.md)

### Environment Variables List
Configure the following environment variables before executing the pipeline job

Name|Example|Description
----|----|----
IntegrationFlowID| ```"IntegrationFlow1"``` | The ID of the integration artefact that needs to be undeployed from the Cloud Integration tenant |
CPIHost| "${env.CPI_HOST}" <br/> Neo: ```"xxxxxx-tmn.hci.eu2.hana.ondemand.com"``` <br/>CF: ```"xxxxxx.it-cpi001.cfapps.eu10.hana.ondemand.com"```| The host name (without HTTPS) of your Cloud Integration tenant |
CPIOAuthHost | "${env.CPI_OAUTH_HOST}" <br/>```"xxxxxx.authentication.sap.hana.ondemand.com"``` | The host name (without HTTPS) of the OAuth token server of your Cloud Integration tenant |
CPIOAuthCredentials | "${env.CPI_OAUTH_CRED}" <br/>       ```"CPIOAuthCredentials"``` | The alias of the OAuth credentials for the Cloud Integration tenant which is deployed on your build server (like Jenkins) |

## Related Recipes
* [CICD - Deploy Run Once Integration Artefact and Check MPL and Store If Success](../CICD-DeployRunOnceIntegrationArtefactAndCheckMplAndStoreIfSuccess)
* [CICD - Deploy Integration Artefact and Get Endpoint](../CICD-DeployIntegrationArtefactGetEndpoint)
* [CICD - Deploy Run Once Integration Artefact and Check MPL ](../CICD-DeployRunOnceIntegrationArtefactAndCheckMpl)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/01/ci-cd-for-sap-integration-suite-here-you-go/)
* [Cloud Integration OData APIs](https://api.sap.com/package/CloudIntegrationAPI?section=Artifacts)
