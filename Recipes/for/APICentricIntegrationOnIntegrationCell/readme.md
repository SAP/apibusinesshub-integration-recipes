# Model API-centric integration on Integration Cell

[Recipes by Topic](../../readme.md#api-centric-integration-and-mcp) | [Recipes by Artefact Type](../readme.md#api-and-mcp-configuration-recipes)

## Motivation

Expose a backend through an API artifact that applies access controls and performs integration processing. For example, a product lookup API can validate a request, call a backend, and transform its response within one artifact.

Use a simple governed proxy when the backend already provides the required processing. Use API-centric integration when you need mediation, enrichment, or service orchestration. See [API-Centric Integration](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/api-centric-integration).

## Prerequisites

Follow [Get Started with Integration Cell](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/get-started-with-integration-cell) to enable API Management and activate the runtime. Obtain developer access and prepare an integration package, a non-production backend endpoint, and its authentication configuration.

This recipe targets Integration Cell. Select the runtime before creating the API: the runtime profile cannot subsequently be switched between Integration Cell and Edge Integration Cell. See [Creating an API Artifact](https://help.sap.com/docs/integration-suite/sap-integration-suite/add-api-artifact). Check target-tenant availability and supported steps before modeling.

## Recipe

1. Open **Design > Integrations and APIs**, edit the package, and choose **Artifacts > Add > API**.
2. Select **Integration Cell**. Use **URL or Specification** to configure the backend endpoint or supply an API definition, following the creation methods in [Creating an API Artifact](https://help.sap.com/docs/integration-suite/sap-integration-suite/add-api-artifact).
3. Configure the public base path and virtual host, backend connection, and resources. For the example, expose a product lookup operation and define its input and output contract.
4. In **Policies**, configure authentication, authorization, and traffic controls suitable for the consumer. Add supported integration steps only where the contract requires processing.
5. When a second HTTP(S) service call is necessary, follow [Creating Additional Request Reply Policy for API Artifact](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/creating-additional-request-reply-policy-for-api-artifact). Retain the original request-reply step; SAP documents that it cannot be deleted.
6. Save a version and deploy. Follow [Deploy an API Artifact](https://help.sap.com/docs/integration-suite/sap-integration-suite/deploying-api-artifact) and inspect status and processing logs with [Monitor APIs](https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/monitor-apis).

## Verification

Use your non-production backend and record the expected results before executing:

| Case | Check |
| --- | --- |
| Authorized product lookup | Response matches the API contract and backend result. |
| Missing authentication or insufficient authorization | Policies deny access before a protected backend operation. |
| Invalid input | Validation behavior matches the documented contract. |
| Backend failure or timeout | Consumer receives the designed error response; logs identify the failing call. |
| Traffic above the configured allowance | Traffic policy enforces the configured limit. |

Review logs for unexpected payload or credential exposure. Record the artifact version, runtime, policy settings, and actual results.

## Related recipe

[Expose an API through MCP Gateway](../ExposeAPIThroughMCPGateway/readme.md) builds on an eligible API deployed on Integration Cell.

Documentation checked on 2026-10-06. This is a configuration guide, not an exported API bundle. No SAP tenant deployment or execution was performed.
