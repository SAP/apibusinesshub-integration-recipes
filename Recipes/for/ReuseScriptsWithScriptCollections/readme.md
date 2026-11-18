# Reuse scripts with Script Collections

[Recipes by Topic](../../readme.md) | [Recipes by Artefact Type](../readme.md)

## Motivation

Separate reusable script maintenance from individual integration flows. This is a configuration recipe; it does not include a deployable artifact.

## Recipe

1. In an integration package, create a Script Collection artifact and add the script resources.
2. In a consuming integration flow in that package, reference the collection and select its script in the Script step.
3. Deploy the collection, then save and deploy the consuming flow. See [Working with Script and Script Collection](https://help.sap.com/docs/integration-suite/sap-integration-suite/e60f7061fe4b45d69d028b24d5a76901.html) and [Deploying a Script Collection](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/deploying-script-collection).
4. Validate the collection using the tenant's enabled [design guidelines](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/design-guidelines-for-script-collection). Resolve essential findings before release.

Use native flow steps where they meet the requirement. Keep mapping UDF scripts local: the current [scripting guidance](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/use-scripting-appropriately) identifies UDFs in mappings as unsupported in collections.

## Verification

Use a non-production package with two consuming flows. Execute representative inputs through both, update the shared script, redeploy the affected artifacts, and verify both outputs. Test the release procedure with the collection dependency present before deploying consumers.

## Version considerations

The [Cloud Integration release notes](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/what-s-new-for-cloud-integration) include script-version upload and collection-validation enhancements. Check availability on the target tenant; uploading a resource is not proof that a historical script is compatible. Validate and exercise scripts before changing their version.

Documentation checked on 2026-10-06. No tenant execution was performed for this recipe.
