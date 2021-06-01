# CICD - Upload Single Key Value Map

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Sunny Kapoor](https://github.com/simplykapoor.png?size=50 ) | [Sunny Kapoor](https://github.com/author-profile ) |
----|----|

This CICD Jenkins job allows you to checkout the configured Key Value Map from the source code repository and either update or create the artefact on the API Portal tenant.

[Instructions to consume the CICD Jenkins file](http-link)

### Environment Variables List
Configure the following environment variables before executing the Jenkins Job

Name|Example|Description
----|----|----
KeyValueMapName |```"Sample KVM"``` |The name of the Key Value Map that needs to be uploaded to the API Portal tenant |
IsDeleteRequired | ```"Yes"``` <br/> ```"No"``` |Assign "Yes", in case the artefact has to be deleted from the API Portal before uploading the artefact from Git. Else set it to "No". In case you set it to "No" and if configured Key Value Map exists on the API portal, then the job will fail|
APIPortalHost | "${env.API_HOST}" <br/>```"eu20apiportal.cfapps.eu20.hana.ondemand.com"``` |The hostname (without HTTPS) of your API Portal tenant |
APIPortalOAuthHost | "${env.API_OAUTH_HOST}" <br/> ```"xxxxxx.authentication.eu20.hana.ondemand.com"``` |The hostname (without HTTPS) of the OAuth token server of your API Portal tenant |
APIPortalOAuthCredentials | ${env.API_OAUTH_CRED}" <br/>```"API_OAuth_Credentials"``` |The alias of the OAuth credentials for the API Portal tenant which is deployed on your build server (like Jenkins) |
GITRepositoryURL | "${env.GIT_REPOSITORY_URL}" <br/>```"github.com/CICD/integrations.git"``` |The full URL of the source code repository without HTTPS |
GITCredentials | "${env.GIT_CRED}" <br/> ```"GIT_Credentials"``` |The alias of the source code repository credentials which is deployed on your build server (like Jenkins) |
GITBranch | "${env.GIT_BRANCH_NAME}" <br/> ```"refs/heads/master"``` |Specify the source code repository branch that you want to work with |
GITFolder | ```"IntegrationContent/API"``` |Specify the folder structure in your source code repository from where you like to read the Key Value Map |

## Related Recipes
* [CICD - Store Single Key Value Map](../CICD-StoreSingleKeyValueMap)
* [More CICD Recipes](../../readme.md#CICD)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/01/ci-cd-for-sap-integration-suite-here-you-go/)
* [API Management OData APIs](https://api.sap.com/package/APIMgmt)
