\|  [Browse by Topic](readme.md)  \| [Browse by Author](author.md) \| [Browse by Artefact Type](for/readme.md) \| [Request a Recipe](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=Recipe%20Request&template=recipe-request.md&title=How+to++) \| [Report a broken link](https://github.com/SAP/apibusinesshub-integration-recipes/issues/new?assignees=&labels=documentation&template=bug_report.md&title=Broken%20Link) \| [Contribute](../CONTRIBUTING.md)\|

# Build and use CPI ADK compliant Apache Camel Community Adapters

## Requirements
1. SAP Cloud Platform Integration Tenant. You will need the following roles in the CPI to develop integration flow using these adapters
     * ESBMessaging.send
     * AuthGroup.IntegrationDevelope
     * AuthGroup.BusinessExpert
3. On Local system
    * [Eclipse tooling for SAP Cloud Platform Integration](https://tools.hana.ondemand.com/#cloudintegration)
    * [Maven installation](https://maven.apache.org/download.cgi)

## Download and Installation
 You can build the adapter either from adapter project source.

### Build .esa file using Adapter Project Source
1. Download the Project Source of Adapter
2. Navigate to the project source folder
3. From the command line issue the command ```mvn clean install```

### Deploy the .esa
1.  Use the Eclipse tooling Deploy Artifacts wizard to deploy Camel Community adapter to SAP Cloud Platform Integration runtime.

### Test the Adapter using Sample Integration to test
1. Import the integration flow sample provided in the adapter samples to the SAP Cloud Platform Integration Design time
2. Configure the integration flow with adapter specific configuration
3. Deploy the integration flow and monitor its message processing status

## How to obtain support
Please raise an issue on this repository.

## CONTRIBUTING
Please read the [CONTRIBUTING.md](../CONTRIBUTING.md) for more details.
