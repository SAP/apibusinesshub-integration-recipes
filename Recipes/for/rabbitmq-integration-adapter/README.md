# Apache Camel RabbitMQ Component 

you can see Apache Camel RabbitMQ Component being used in the SAP Cloud Platform Integration Flow, i.e. a simple message will be persisted in to the RabbitMQ message queue. 

## Install

import the camel-rabbitmq.esa adapter file in to SAP Cloud Platform Integration Runtime using Eclipse tooling Deploy Artifacts wizard. 
import the RabbitMQ.zip integration flow in to SAP Cloud Platform Integration Design time using WebiDE tooling. this iFlow shows how to use the imported adapter in the previous step
   

## Usage
Configure the imported integration flow( a sample configuration already provided) with the RabbitMQ server details, and deploy it.  


## Tests

Check that the iFlow message processing successfully completed in the CPI monitoring. configured message in the iFlow Content modifier's message body will appear in the RabbitMQ message queue which you can verify
using RabbitMQ web Console.


## Credits


## License
