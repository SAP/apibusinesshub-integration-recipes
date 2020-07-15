# Apache Camel Redis Component 

you can see Apache Camel redis Component being used in the SAP Cloud Platform Integration Flow, i.e. a simple key/value will be persisted to the redis cache. 

## Install

import the camel-spring-redis.esa adapter file in to SAP Cloud Platform Integration Runtime using Eclipse tooling Deploy Artifacts wizard. 
import the redis.zip integration flow in to SAP Cloud Platform Integration Design time using WebiDE tooling. this iFlow shows how to use the imported adapter in the previous step
   

## Usage
Configure the imported integration flow( a sample configuration already provided) with the redis server details, and deploy it.  


## Tests

Check that the iFlow message processing successfully completed in the CPI monitoring. CPI sent key/value can be seen in the Redis cache


## Credits


## License
