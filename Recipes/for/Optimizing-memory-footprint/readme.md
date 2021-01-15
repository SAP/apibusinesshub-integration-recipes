# Optimizing Memory Footprint of your integration flow

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Optimizing-memory-footprint ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Optimizing-memory-footprint ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Optimizing-memory-footprint ) \|

![Meghna Shishodiya](https://github.com/author-profile.png?size=50 ) | [Meghna Shishodiya](https://github.com/author-profile ) |
----|----|

This recipe will provide insights about what happens behind the scenes in the integration pipeline, which if taken into account can result in reducing the memory requirement of your integration flow.

## Recipe

1.	When your flow has multiple branches, through a multicast or router, ensure that before ending the branch, you empty or reset all the data that is not required beyond that step. Unless explicitly reset, the data will be kept in memory until the process ends. The property and headers automatically reset when the context switches to the next branch; however, the body and variables continue to hold the data. Release all unwanted data before exiting the branch.
2.	Empty the header and property maps after you are done with retrieving all the required information in the script. In order to access a property or header in a script, you retrieve the entire list into a variable and then get the required property/header value. There are cases when there are many properties/headers or there is large amount of data assigned to any property/header.
3.	Avoid multicasting wherever possible – it multiplies the data and stores that in the memory.
4.	Try to use splitter wherever possible to break a large message into small chunks. Process the small chunks and if possible send the processed chunk to the receiver before working on the next chunk. If the receiver expects the entire request together, collect the processed data before sending it to the receiver.
5.	Use of Byte instead of String reduces the memory consumption, unless you want to process the message as a string in the next step.
Make sure that the step after the mapping, especially a script can take in a byte data.

6.	Global variables make use of headers to perform db persist. Memory to these headers remain allocated even after the flow ends. In case the header is holding a large amount of data, it may fail the iflow processing. It is important to release the memory allocated to the header (having the same name as the global variable) before the iflow exits. This however cannot be performed from a content modifier - it will need to be done via a script.

7.	Local variables are alive even after the iflow execution is over. Even though the local variable is visible only to the iflow, it is important to note that the memory allocated to the variable is not released when the iflow execution is over. It is important to reset the local variable in the beginning of the flow to avoid any values getting used from the previous flow. It is specifically relevant when the previous flow ended abruptly and the variable may be holding invalid data. For better memory management, it also makes sense to reset the local variables before exiting the iflow. This is only relevant for those variables whose value does not need to be preserved until the next run.
8.	When using XPATHs, try to use absolute path as much as possible; relative XPATH expressions are very expensive.
9.	Avoid datatype conversion of the message.
10.	Consider the parser capabilities and footprint before choosing the parser -
Although it may be easy DOM parsing the XML in the script, it is important to note that DOM parsers are memory intensive as they load the entire XML in memory and create the XML tree from it. This is an important consideration especially when working with big datasets as they can cause an out-of-memory situation. However, if the dataset is not huge, DOM parsing can prove to be more performant. DOM parsing can also be a better choice when you need to parse the XML back and forth.
SAX/STAX are very helpful when working with huge datasets as they stream the XML and do not load the entire XML in memory. Moving the XML back and forth may be expensive with these parsers. See also https://blogs.sap.com/2017/06/20/stream-the-xmlslurper-input-in-groovy-scripts/.

11.	Keep the tracing turned off unless it is required for troubleshooting. Writing trace adds a lot of overhead on performance as every stage of message processing is persisted along with the message at every step.

## References
* [Additional tips and tricks](https://apps.support.sap.com/sap/support/knowledge/public/en/2737526)
