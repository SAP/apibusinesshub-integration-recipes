# CICD - Store Single API Provider

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20escaped-do-some-code-thing-%20 ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20escaped-do-some-code-thing-%20 ) \| [Fix documentation](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20escaped-do-some-code-thing-%20 ) \|

![Sunny Kapoor](https://github.com/simplykapoor.png?size=50 ) | [Sunny Kapoor](https://github.com/simplykapoor) |
----|----|

This CICD Jenkins job simply enables you to download an API Provider from the API Portal and store it in your source code repository like Git.

[Instructions to consume the CICD Jenkins file](../../instructions-to-consume-the-CICD-jenkins-file.md)

### Environment Variables List
Configure the following environment variables before executing the Jenkins Job

Name|Example|Description
----|----|----
APIProviderName |```"Northwind_APIs"``` |The name of the API Provider that needs to be stored in the source code repository |
APIPortalHost | "${env.API_HOST}" <br/>```"eu20apiportal.cfapps.eu20.hana.ondemand.com"``` |The hostname (without HTTPS) of your API Portal tenant |
APIPortalOAuthHost | "${env.API_OAUTH_HOST}" <br/> ```"xxxxxx.authentication.eu20.hana.ondemand.com"``` |The hostname (without HTTPS) of the OAuth token server of your API Portal tenant |
APIPortalOAuthCredentials | ${env.API_OAUTH_CRED}" <br/>```"API_OAuth_Credentials"``` |The alias of the OAuth credentials for the API Portal tenant which is deployed on your build server (like Jenkins) |
GITRepositoryURL | "${env.GIT_REPOSITORY_URL}" <br/>```"github.com/CICD/integrations.git"``` |The full URL of the source code repository without HTTPS |
GITCredentials | "${env.GIT_CRED}" <br/> ```"GIT_Credentials"``` |The alias of the source code repository credentials which is deployed on your build server (like Jenkins) |
GITBranch | "${env.GIT_BRANCH_NAME}" <br/> ```"refs/heads/master"``` |Specify the source code repository branch that you want to work with |
GITFolder | ```"IntegrationContent/API"``` |Specify the folder structure in your source code repository where you like to store the API Provider |
GITComment | ```"API Provider update from CICD pipeline"``` |Specify the text to be used during check-in to your source code repository |

## Related Recipes
* [CICD - Store All API Providers](../CICD-StoreAllAPIProviders)
* [CICD - Upload Single API Provider](../CICD-UploadSingleAPIProvider)
* [More CICD Recipes](../../readme.md#cicd)

## References
* [CICD Blog Post](https://blogs.sap.com/2021/06/01/ci-cd-for-sap-integration-suite-here-you-go/)
* [API Management OData APIs](https://api.sap.com/package/APIMgmt)
