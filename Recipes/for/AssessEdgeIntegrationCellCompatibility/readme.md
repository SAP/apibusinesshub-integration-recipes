# Assess recipe compatibility with Edge Integration Cell

[Recipes by Topic](../../readme.md) | [Recipes by Artefact Type](../readme.md)

## Motivation

A recipe working on a cloud runtime needs a separate compatibility review before deployment to Edge Integration Cell. This is a review recipe; it does not include a deployable artifact.

## Recipe

1. Inventory the recipe's adapters, credentials, script APIs, persistence, and network dependencies.
2. Compare them with the current [Edge Integration Cell Runtime Scope](https://help.sap.com/docs/integration-suite/sap-integration-suite/supported-features-and-limitations-of-edge-integration-cell). Follow the linked SAP Note for out-of-scope actions; check it with an authorized SAP account.
3. Check each adapter's documentation. For example, [AS2 sender configuration](https://help.sap.com/docs/integration-suite/sap-integration-suite/configure-as2-sender-adapter) states that dynamically fetching values from Partner Directory is unsupported on Edge runtime.
4. Review credentials and capacity: the runtime scope excludes OAuth2 Authorization Code credentials and describes storage-dependent persistence and JMS limits.
5. Check the flow's runtime profile and component versions before deployment. Follow the adapter documentation's Update Version guidance for older shapes.

## Verification

Deploy to a non-production edge runtime. Verify authentication, connectivity, expected outputs, failure recovery, and resource usage with representative volumes. Record the tested runtime version and dependencies. A successful cloud deployment does not establish edge compatibility.

## Maintenance

Recheck scope and adapter pages when upgrading the edge runtime or changing dependencies. Avoid copying numeric limits into the recipe because they can change with the runtime and infrastructure.

Documentation checked on 2026-10-06. No edge deployment was performed for this recipe.
