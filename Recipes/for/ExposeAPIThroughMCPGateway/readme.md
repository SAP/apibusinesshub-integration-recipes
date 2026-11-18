# Expose an API through MCP Gateway

[Recipes by Topic](../../readme.md#api-centric-integration-and-mcp) | [Recipes by Artefact Type](../readme.md#api-and-mcp-configuration-recipes)

## Motivation

Make a governed product lookup API discoverable and callable by an AI client as an MCP tool. SAP implements its MCP Server capability through MCP Gateway. See [Model Context Protocol (MCP)](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/model-context-protocol-mcp).

This recipe uses an existing API artifact. HTTP endpoint and RFC sources have separate setup procedures; consult [Creating an MCP Server](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/creating-mcp-server) for those alternatives.

## Prerequisites

- Confirm that the tenant's service plan supports the feature. SAP Help refers to SAP Note 2903776; check it using an authorized SAP account.
- Enable API Management and activate Integration Cell, with developer access to the integration package.
- Prepare an OAuth-secured REST or OData API artifact already deployed on Integration Cell. For this source type, SAP documents OpenAPI versions 3.0.0 through 3.0.3 and excludes Swagger 2.0.
- Prepare an AI client compatible with the server's authentication flow and arrange Developer Hub publication and subscription access.

See [Configure an MCP Server Created from an API Artifact](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/govern-and-manage-mcp-server-created-from-api-artifact). The current [deployment support](https://help.sap.com/docs/integration-suite/sap-integration-suite/manage-apis) lists Integration Cell for MCP artifacts; this recipe does not assume MCP deployment on Edge Integration Cell.

## Recipe

1. In the integration package, add an **MCP Server** artifact and choose the API-artifact source. Follow the API-source workflow linked from [Creating an MCP Server](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/creating-mcp-server).
2. Select the eligible deployed product API. In **MCP Configuration > Tools**, expose its lookup operation. Review its description and derived input/output schemas against the source contract. Start with a read-only operation for this example.
3. In **Policies**, review the mandatory authentication and authorization policies and configure traffic controls. Authentication is inherited from the source API for this source type. If the source API has an Authorization policy, follow SAP's documented **Trust Upstream MCP Authorization** configuration.
4. Deploy the MCP artifact on Integration Cell, then discover and publish it as a product in Developer Hub. Follow [How MCP Server Enables AI Integration](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/how-model-context-protocol-mcp-enables-ai-integration-with-apis), including its linked deployment and publication procedures.
5. For internal XSUAA authentication, create an Agent subscription and register the AI application's actual callback URL. Configure the client with the issued credentials and deployed MCP endpoint using [Configure MCP Server Access Using Internal Authentication](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/configure-mcp-server-access-using-internal-authentication-xsuaa). Keep credentials outside the recipe and repository.

## Verification

| Case | Check |
| --- | --- |
| Authorized client discovery | Only the intended lookup tool and its expected schema appear. |
| Lookup tool invocation | Result agrees with the same request made to the source API. |
| Missing or insufficient credentials | Protected operations are denied. |
| Invalid tool arguments | Behavior matches the source contract. |
| Configured traffic limit exceeded | Policy enforcement matches the configured allowance. |

Record the artifact version, runtime, client, and actual results. Treat adding write operations as a separate design change requiring explicit business authorization.

## Maintain source alignment

After updating the source API, use **Synchronize** as described in the [configuration guide](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/govern-and-manage-mcp-server-created-from-api-artifact). Review affected tools and schemas, deploy the update, and repeat discovery and invocation checks.

[Model API-centric integration on Integration Cell](../APICentricIntegrationOnIntegrationCell/readme.md) provides the preceding API configuration workflow.

Documentation checked on 2026-10-06. This is a configuration guide, not an exported MCP artifact. No SAP tenant deployment or AI-client execution was performed.
