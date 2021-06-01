# CICD - Update Integration Resources on Git Commit

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Sunny Kapoor](https://github.com/simplykapoor.png?size=50 ) | [Sunny Kapoor](https://github.com/simplykapoor) |
----|----|

This CICD Jenkins job allows you to develop and manage your integration resources like scripts and XSLT mappings using external IDEs (with better development support) and SCM tools like Git.

The Jenkins job takes care that the resources would be synchronized with the integration flow once they are committed to Git. By this you enable the integration developers to work independently on integration resources while another integration developer works on the integration flow parallelly.

 A typical flow would look something like this:

 * Create a new integration flow in Cloud Integration web tooling with a script or XSLT mapping and store it in Git. Refer related recipes section for this.
 * Clone the git repository in external IDEs like Visual Studio.
 * Push the changes of the XSLT file using external IDE to the same Git.
 * The Git push would trigger the Jenkins Pipeline (with the help of a web hook) which then uploads only the changed/added/deleted resources to the Integration Flow.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file.md)

### Environment Variables List
Configure the following environment variables before executing the Jenkins Job

Name|Example|Description
----|----|----
CPIHost| "${env.CPI_HOST}" <br/> Neo: ```"xxxxxx-tmn.hci.eu2.hana.ondemand.com"``` <br/>CF: ```"xxxxxx.it-cpi001.cfapps.eu10.hana.ondemand.com"```|The hostname (without HTTPS) of your Cloud Integration tenant |
CPIOAuthHost | "${env.CPI_OAUTH_HOST}" <br/>```"xxxxxx.authentication.sap.hana.ondemand.com"``` |The hostname (without HTTPS) of the OAuth token server of your Cloud Integration tenant |
CPIOAuthCredentials | "${env.CPI_OAUTH_CRED}" <br/> ```"CPIOAuthCredentials"``` |The alias of the OAuth credentials for the Cloud Integration tenant which is deployed on your build server (like Jenkins) |
GITRepositoryURL | "${env.GIT_REPOSITORY_URL}" <br/>```"https://github.com/CICD/integrations.git"```|The full URL of the source code repository without HTTPS |
GitCredentials | "${env.GIT_CRED}" <br/> ```"GitCredentials"``` |The alias of the source code repository credentials which is deployed on your build server (like Jenkins)|
GITBranch | "${env.GIT_BRANCH_NAME}" <br/> ```"refs/heads/master"``` |Specify the source code repository branch that you want to work with |
GITFolder | ```"IntegrationContent/IntegrationArtefacts/Flow_with_XSLT_mapping"``` |Specify the integration flow root folder name in your source code repository from where you like to read the intgration flow resources |

## Related Recipes
* [CICD - Upload Integration Artefact](../CICD-UploadIntegrationArtefact)
* [CICD - Store Integration Artefact](../CICD-StoreIntegrationArtefact)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/01/ci-cd-for-sap-integration-suite-here-you-go/)
* [Cloud Integration OData APIs](https://api.sap.com/package/CloudIntegrationAPI?section=Artifacts)
* [Generic Webhook Trigger Jenkins Plugin](https://github.com/jenkinsci/generic-webhook-trigger-plugin/)
