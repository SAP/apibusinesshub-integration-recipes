# CICD - Get Specific Message Processing Log

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Axel Albrecht](https://github.com/axelalbrechtsap.png?size=50 ) | [Axel Albrecht](https://github.com/axelalbrechtsap ) |
----|----|

This CICD Jenkins job enables you to get the message procesing log status of either a specific message exchange ID or of the last run of a specific integration artefact. You can decide if an intermediate state like "Processing" is ok or if you want the final state. In case the message execution failed, the job also provides the error information.

 A typical flow would look something like this:

 * You build/upload and deploy an integration flow on your Cloud Integration tenant.
 * A message gets processed.
 * Using this job, you can retrieve the MPL status of the run.
 * Do some activities based on the result.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file/readme.md)

### Environment Variables List
Configure the following environment variables before executing the pipeline job

Name|Example|Description
----|----|----
SearchCriteria| ```"IntegrationFlowID"``` | Specify either "IntegrationFlowID", if you like to get the latest MPL status of the configured integration flow artefact (status "Discarded" excluded) or select "MPLID", if you like to get the MPL status of the configured MPL ID |
IntegrationFlowID| ```"IntegrationFlow1"``` | The ID of the integration artefact for which you want to get the message processing log status. This parameter will be ignored if the "SearchCriteria" parameter is set to "MPLID" |
MPLID| ```"AGB2y3D65NFlH-8tWDxqAZKoZD58"``` | The ID of a specific Message Processing Log. This parameter will be ignored if the "SearchCriteria" parameter is set to "IntegrationFlowID" |
AcceptIntermediateStatus | ```true``` <br/> ```false``` | If you accept "Processing" as a valid status, set this to "true". If you want only final status results, set it to "false" |
FailJobOnFailedMPL | ```true``` <br/> ```false``` | Specify if the job should fail in case the status of the retrieved message processing log is Failed or Retry. If you are doing negative testing and you're expecting the integration artefact run to fail, set this to "false" |
MPLCheckRetryCounter | ```10``` | Specify the maximum count of retries for checking the final MPL status as the process might run for a while. Between each check we'll wait for 3 seconds. This field will be ignored if you have configured the job to accept intermediate status |
CPIHost| "${env.CPI_HOST}" <br/> Neo: ```"xxxxxx-tmn.hci.eu2.hana.ondemand.com"``` <br/>CF: ```"xxxxxx.it-cpi001.cfapps.eu10.hana.ondemand.com"```| The host name (without HTTPS) of your Cloud Integration tenant |
CPIOAuthHost | "${env.CPI_OAUTH_HOST}" <br/>```"xxxxxx.authentication.sap.hana.ondemand.com"``` | The host name (without HTTPS) of the OAuth token server of your Cloud Integration tenant |
CPIOAuthCredentials | "${env.CPI_OAUTH_CRED}" <br/>       ```"CPIOAuthCredentials"``` | The alias of the OAuth credentials for the Cloud Integration tenant which is deployed on your build server (like Jenkins) |

## Related Recipes
* [CICD - Upload Integration Artefact](../CICD-UploadIntegrationArtefact)
* [CICD - Deploy Integration Artefact and Get Endpoint](../CICD-DeployIntegrationArtefactGetEndpoint)
* [CICD - Store Integration Artefact](../CICD-StoreIntegrationArtefact)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/02/ci-cd-for-sap-integration-suite-here-you-go/)
* [Cloud Integration OData APIs](https://api.sap.com/package/CloudIntegrationAPI?section=Artifacts)
