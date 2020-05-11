# Simulate Response from Datastore  Select Operation and  Write Variable

![Abhinav Verma](https://github.com/abhinavverma0501.png?size=50 )|[Abhinav Verma](https://github.com/abhinavverma0501)|
----|----|

Use this recipe to 
* Simulate read the value of Write Variable in Content Modifer
* Simulate the Datastore Select Operation.  

[Download the integration flow sample](Simulate-Response-from-Write-Variable-and-DataStore-Select.zip)

## Recipe

Step|Screenshots|Why?
----|----|----
Open integration flow in read mode | | Simulation is available in the read-mode of integration flow. 
Switch on simulation mode| | Enables the simulation tool bar to run simulation or clear simulation.
Define start point || Click on the connector line where you need to define the start of your simulation. In our scenario, it will be after start timer event.
Define end point || Click on the connector where you need to define the end of your simulation. Here, it will be before End Message.
Provide an input||To start simulation, message input is required. Click start point to open the simulation input dialog.Enter the input payload in the body.
Simulate response from Datastore Select Operation|| Click Add Simulation  Response speed button would appear to enter the response Datastore Select Operation.
Read the value of Write Variable in Content Modifier|| 
Run Simulation|| 
Clear Simulation|| 


## References
* [Integration Flow Simulation product documentation](https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/2e2210b6db0c4fdb937b3a57d952f582.html)
* [Integration Flow Simulation blog post](https://blogs.sap.com/2020/04/13/integration-flow-simulation-in-sap-cloud-platform-integration/)


## Sample integration flow
The scenario will show how we can simulate and read the value of Write  variable in content modifier. Also, it will show how we can simulate the Datastore Select Operation.

Integration flow constitutes of various steps like content modifier, Write variable, Datastore: Write and Select.

![iflowimage](Integration-Flow-Scenario.jpg)


### Input Payload
This is the simulation input payload used in the sample integration flow. Select the Start point, the XML payload provide in should be feed in the Simulation Input Dialog.
```
<Products>
    <Product>
      <CurrencyCode>EUR</CurrencyCode>
      <Price>799.000</Price>
      <ProductId>HT-7010</ProductId>
      <Name>Silverberry</Name>
    </Product>
    <Product>
      <CurrencyCode>EUR</CurrencyCode>
      <Price>44.900</Price>
      <ProductId>HT-2025</ProductId>
      <Name>CD/DVD case: 264 capacity</Name>
    </Product>
    <Product>
      <CurrencyCode>EUR</CurrencyCode>
      <Price>1230.000</Price>
      <ProductId>HT-1137</ProductId>
      <Name>Flat X-large II</Name>
    </Product>
    <Product>
      <CurrencyCode>EUR</CurrencyCode>
      <Price>900.000</Price>
      <ProductId>HT-1601</ProductId>
      <Name>Family PC Pro</Name>
    </Product>
    <Product>
      <CurrencyCode>EUR</CurrencyCode>
      <Price>34.000</Price>
      <ProductId>HT-1106</ProductId>
      <Name>Smart Firewall</Name>
    </Product>
</Products>

```

![Input payload](Simulation-Input-payload.jpg)

Content Modifier1 will receive the  input payload.

Add the Write variable after the Content Modifier1 define the variable name "Timestamp".We will read the value of Write variable in Content Modifier2 by leveraging Integration Flow Simulation feature.

![Define Write Variable](Write-Variable-Input.jpg)

Add below properties in Content Modifier2
"Update_Timestamp"and "Payload".

![Content Modifier2 Property Details](Content-Modifer2-Property.jpg)

### Response payload from Select DataStore
Add this below payload as Simulated response from the Select Datastore flowstep.
```
<Products>
    <Product>
      <CurrencyCode>EUR</CurrencyCode>
      <Price>799.000</Price>
      <ProductId>HT-7010</ProductId>
      <Name>Silverberry</Name>
    </Product>
</Products>
```

![Click Add Simulation Response](Add-Simulation-Response-from-Select.jpg)

### Simulate the value of Write Variable

Go to the Content Modifier2 and click on add Simulation Response.Click on add Variable and write the Variable name i.e "Timestamp"and put the below value.

```
Timestamp 2020-04-27 12:11:00
```
![Click Add Simulation Response](Add-Simulation-Response-from-Write-Variable.jpg)

This is how we can simulate and read the value of Write  variable in content modifier.


### Run Simulation

Once all the necessary input and response payload is provided, you can run the simulation. Once the simulation is complete,we can read the simulated value of  Write Variable in Content Modifier2 as property in Trace Envelope.After that click on the Message Body of Trace Envelope of Content Modifer2,you will see the payload updated after simulating the Datastore Select operation.

![Write Variable simulated](Write-Variable-Timestamp-simulated.jpg)

![Response payload](Payload-Updated-after-Simulating-Select-Step.jpg)

This ends the recipe of integration flow simulation for Datastore Select and Write Variable.
