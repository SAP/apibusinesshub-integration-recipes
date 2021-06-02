# CICD - Upload Single API Provider

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Sunny Kapoor](https://github.com/simplykapoor.png?size=50 ) | [Sunny Kapoor](https://github.com/simplykapoor  ) |
----|----|

This CICD Jenkins job allows you to checkout the configured API Provider from the source code repository and either update or create the artefact on the API Portal tenant.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file.md)

### Environment Variables List
Configure the following environment variables before executing the Jenkins Job

Name|Example|Description
----|----|----
APIProviderName |```"Northwind_APIs"``` |The name of the API Provider that needs to be uploaded to the API Portal tenant |
IsDeleteRequired | ```"Yes"``` <br/> ```"No"``` |Assign "Yes", in case the artefact has to be deleted from the API Portal before uploading the artefact from Git. Else set it to "No". In case you set it to "No" and if configured API Provider exists on the API portal, then the job will fail|
APIPortalHost | "${env.API_HOST}" <br/>```"eu20apiportal.cfapps.eu20.hana.ondemand.com"``` |The hostname (without HTTPS) of your API Portal tenant |
APIPortalOAuthHost | "${env.API_OAUTH_HOST}" <br/> ```"xxxxxx.authentication.eu20.hana.ondemand.com"``` |The hostname (without HTTPS) of the OAuth token server of your API Portal tenant |
APIPortalOAuthCredentials | ${env.API_OAUTH_CRED}" <br/>```"API_OAuth_Credentials"``` |The alias of the OAuth credentials for the API Portal tenant which is deployed on your build server (like Jenkins) |
GITRepositoryURL | "${env.GIT_REPOSITORY_URL}" <br/>```"github.com/CICD/integrations.git"``` |The full URL of the source code repository without HTTPS |
GITCredentials | "${env.GIT_CRED}" <br/> ```"GIT_Credentials"``` |The alias of the source code repository credentials which is deployed on your build server (like Jenkins) |
GITBranch | "${env.GIT_BRANCH_NAME}" <br/> ```"refs/heads/master"``` |Specify the source code repository branch that you want to work with |
GITFolder | ```"IntegrationContent/API"``` |Specify the folder structure in your source code repository from where you like to read the API Provider |

## Related Recipes
* [CICD - Store Single API Provider](../CICD-StoreSingleAPIProvider)
* [CICD - Store All API Providers](../CICD-StoreAllAPIProviders)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/02/ci-cd-for-sap-integration-suite-here-you-go/)
* [API Management OData APIs](https://api.sap.com/package/APIMgmt)
