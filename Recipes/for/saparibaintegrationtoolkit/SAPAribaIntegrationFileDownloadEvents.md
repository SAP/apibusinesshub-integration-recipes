SAP Ariba Integration File Download Events

# Table of Contents
[Table of Contents	1](#_Toc107415011)

[Overview	2](#_Toc107415012)

[Prerequisites	2](#_Toc107415013)

[Download Events	2](#_Toc107415014)

[Security	3](#_Toc107415015)

[Data center value mappings	3](#_Toc107415016)

[Parameter Reference	4](#_Toc107415017)

[Receiver - SFTPserver	4](#_Toc107415018)

[Receiver – EmailReceiver	6](#_Toc107415019)

[Receiver – SAPAriba	10](#_Toc107415020)

[More – All Parameters	10](#_Toc107415021)

[Data stores	12](#_Toc107415022)

[Helpful tips	12](#_Toc107415023)

[Troubleshooting	12](#_Toc107415024)

[Typical issues for file download:	12](#_Toc107415025)





# Overview
SAP Ariba ITK package includes iFlows “SAP Ariba Integration File Download Events – Buying” and “SAP Ariba Integration File Download Events – Sourcing”, which are used to transfer master and transactional data from your Ariba service to your SFTP server. Data is transferred in the form of comma-separated-value (CSV) files. The underlying protocol used by the iFlow is an HTTP.

This document:

- Describes the information needed to transfer data from your Ariba service
- Provides reference information for each field in the iFlow parameters

# Prerequisites
Before working with this document, make sure you have met the following prerequisites:

- You have an SFTP (Secure File Transfer Protocol) server available 
- You have an email server available 
- You know the site ID and datacenter location of your Ariba realm. Realm enablement team or Ariba deployment team can provide you with this ID
- You have configured the integration password. Follow these steps in Ariba to complete the configuration:
  - Log in to your site.
  - Choose Manage > Core Administration (for Buying), or Manage > Administration (for Sourcing)
  - In the left menu, choose Integration Administration > Integration Toolkit Security.
  - Enter the password twice.
  - Select Save. The integration password is now set, and data transfers using the Data Transfer Tool, or the other protocols described in this document are now enabled.
- You are familiar with the Ariba integration, import, and administration guides.

This document focuses on setting up data transfer iFlow. It does not provide information on master data itself. For details on what files should be uploaded, please refer to the guides listed above.

# Download Events
You can use the SAP Ariba data transfer tool to perform these tasks:

- **Download transactional data from your SAP Ariba system**: Synchronize your back-end system with SAP Ariba by transferring transactional data from an SAP Ariba application. Transactional data that you can download includes expense reports, payment requests, and accruals.
- **Download master data from your SAP Ariba system**. 


To use these iFlows, you configure set of parameters, where you specify your company’s site name, partition, authentication information, and a location to receive the data exported from the SAP Ariba application, along with other options.

The options also include where to send an email notification reporting the status of the data transfer.

The events that are preconfigured to be used with the iFlows have an SAP Ariba integration toolkit name that you must use. 


# Security
This section describes how to use shared secret-based authentication.

SAP Ariba:

You can get the shared secret configured by your Administrator from the Integration Toolkit Security page in Ariba Administrator.

1. In Ariba Administrator, under Integration Manager workspace, click Integration Toolkit Security workspace
1. Under Select the Authentication Method, select Shared Secret from the pull-down menu
1. Under Shared Secret, enter the shared secret twice to confirm it
1. Click Save

SAP Integration Suite, Cloud Integration:

1. In Monitor, under Manage Security section, click Security Material
1. Under Create, select Secure Parameter
1. Create a new Secure Parameter with descriptive name such as “AribaBuyingSharedSecretTest” or “AribaBuyingSharedSecretProd”, enter the shared secret twice to Secure Parameter and Repeat Secure Parameter
1. Click Deploy

Note: Upload and download iFlows need to use the same shared secret. Please use secure value.

# Data center value mappings
One of the artifacts in the SAP Ariba Integration toolkit package is the SAP Ariba Datacenter Value mappings. This is a key component for the flow to work and **must be deployed!** It contains a standard list of datacenters which are mapped to the integration URL equivalent. You will use one of these datacenters when configuring your iflows. If it happens that your SAP Ariba realm is hosted on a different datacenter than the ones listed below, then please use the line marked as “other”, update the integration path next to it and redeploy the mapping.

![](Aspose.Words.bc375156-f807-49ff-b347-465fd4a999c6.001.png)

Note: no additional lines are supported in this mapping, for any nonstandard datacenters, please use the line “other”, the url must end with /
# Parameter Reference
The following table provides reference information for each parameter of the download.


### Receiver - SFTPserver
- The setup is the same for Buying and Sourcing

![](Aspose.Words.bc375156-f807-49ff-b347-465fd4a999c6.002.png)

|**Parameter**|**Example Value and Notes**|
| :- | :- |
|<p>**realm**</p><p></p>|<p>Site ID to which to direct HTTP requests </p><p>Use the actual realm ID of your site (Buying site for Buying iFlow, Sourcing one for Sourcing iFlow)</p>|
|**Address**|Host name or IP address of the SFTP server and an optional port, for example, wdfd00213123:22.|
|**Proxy Type**|<p>Proxy type that you are using to connect to the target system.</p><p>- Select Internet if you are connecting directly to the SFTP server.</p><p>- Select On-Premise if you are connecting to an on-premise SFTP server.</p><p></p>|
|<p>**Location ID**</p><p>(only if **On-Premise** is selected for **Proxy Type**)</p>|To connect to an SAP Cloud Connector instance associated with your account, enter the location ID that you defined for this instance in the destination configuration of SAP BTP cockpit.|
|**Authentication**|<p>Authentication option for the connection to the SFTP server. You've the following options:</p><p>- Public Key</p><p>SFTP server authenticates the calling component (tenant) based on a public key.</p><p>- User Name/Password</p><p>SFTP server authenticates the calling component (tenant) based on the user name and password. To make this configuration setting work, you need to define the user name and password in a User Credential artifact and deploy the artifact on the tenant.</p><p>- Dual</p><p>SFTP server authenticates the calling component (tenant) with two authentication methods: based on a public key and based on user credentials.</p><p>At runtime, the system evaluates the values of additional parameters in the following way:</p><p>- For the authentication step based on user credentials: Credentials from the deployed artifact with the name given by the Credential Name parameter are evaluated by the system to authenticate the tenant against the SFTP server.</p><p>- For the authentication step based on public key: User name contained in the deployed artifact with name given by the Credential Name parameter and the key identified by the Private Key Alias parameter are evaluated by the system to authenticate the tenant against the SFTP server.</p><p>If selected, you can specify the User Credentials artifact (that contains user name and password) with the Credential Name parameter and the key to be used from the keystore with the Private Key Alias parameter.</p><p></p>|
|<p>**Credential Name**</p><p>(only if **User Name/Password** or **Dual** is selected for **Authentication**)</p><p></p>|Name of the deployed **User Credentials** artifact that contains the user name and password.|
|<p>**User Name**</p><p>(only if **Public Key** is selected for **Authentication**)</p><p></p>|<p>ID of the user performing the file transfer.</p><p>Make sure that the user name contains no other characters than A-z, 0-9, \_ (underscore), - (hyphen), / (slash), ? (question mark), @ (at), ! (exclamation mark), $ (dollar sign ), ' (apostrophe), (, ) (brackets), \* (asterisk), + (plus sign), , (comma), ; (semicolon), = (equality sign), . (dot), or ~ (tilde). Otherwise, an attempt for anonymous login is made which results in an error.</p><p></p>|
|<p>**Private Key Alias**</p><p>(only if **Public Key** or **Dual** is selected for **Authentication**)</p><p></p>|Alias to identify the private key in the keystore used for the communication with the SFTP server.|
|**Timeout (in ms)**|<p>Maximum time (in milliseconds) to wait for the SFTP server to be contacted while establishing connection or performing a read operation.</p><p>Default value: 10000</p><p>The timeout should be more than 0, but less than five minutes.</p>|
|**Maximum Reconnect Attempts**|<p>Maximum number of attempts allowed to reconnect to the SFTP server.</p><p>Default value: 3</p><p>Use 0 to disable this behavior.</p>|
|**Reconnect Delay (in ms)**|<p>Time (in milliseconds) the system waits before attempting to reconnect to the SFTP server.</p><p>Default value: 1000</p><p></p>|

Successfully created files are saved in the downloaddir folder, in case of any errors during the flow, a logdir is utilized to store the error message. 

For more info, refer also to SAP Cloud Integration guide: <https://help.sap.com/docs/CLOUD_INTEGRATION/368c481cd6954bdfa5d0435479fd4eaf/2de9ee58737247969eb7dc9e68b1b121.html>

### Receiver – EmailReceiver
The mail adapter allows you to connect the tenant to an email server. The sender mail adapter can download e-mails and access the e-mail body content as well as attachments. The receiver mail adapter allows you to send encrypted messages by e-mail.

![](Aspose.Words.bc375156-f807-49ff-b347-465fd4a999c6.003.png)


|**Parameter**|**Example Value and Notes**|
| :- | :- |
|<p>**Address**</p><p></p>|<p>Specifies the host name and (optionally) a port number of the SMTP server.</p><p>An external mail server can be used.</p><p>Use one of the following open ports for external mail servers:</p><p>- 587 for SMTP+STARTTLS</p><p>- 465 for SMTPS</p><p>Example (to connect to Yahoo mail server):</p><p>smtp.mail.yahoo.com:465</p>|
|**Proxy Type**|<p>The type of proxy that you’re using to connect to the target system.</p><p>- Select **Internet** if you’re connecting to a cloud mail server.</p><p>- Select **On-Premise** if you’re connecting to an on-premise mail server.</p>|
|**Location ID** (only if **On-Premise** is selected for **Proxy Type**)|To connect to a cloud connector instance associated with your account, enter the location ID that you defined for this instance in the destination configuration on the cloud side.|
|**Timeout (in ms)**|<p>Specifies the network timeout for the connection attempt to the server.</p><p>The default value is 30000. The timeout must be larger than 0, but less than five minutes.</p>|
|**Protection** |<p>Defines whether encryption is used. The possible values are:</p><p>- **Off**</p><p>No encryption is initiated, whether the server requests it or not.</p><p>- **STARTTLS Mandatory**</p><p>If the server supports STARTTLS, the client initiates encryption using TLS. If the server doesn’t support this option, the connection fails.</p><p>- **STARTTLS Optional**</p><p>If the server supports the STARTTLS command, the connection is upgraded to Transport Layer Security encryption (works with the normal port 25).</p><p>If the server supports STARTTLS, the client initiates encryption using TLS. If the server doesn’t support this option, client and server remain connected but communicate without encryption.</p><p>- **SMTPS** (only when **None** has been selected for **Proxy Type**)</p><p>The TCP connection to the server is encrypted using SSL/TLS. This step usually requires an SSL proxy on the server side and access to the port it runs on.</p><p></p>|
|**Authentication**|<p>Specifies which mechanism is used to authenticate against the server with a user name and password combination. Possible values are:</p><p>- **None**</p><p>No authentication is attempted. No credential can be chosen.</p><p>- **Plain User Name/Password**</p><p>The user name and password are sent in plain text. Only use this option together with SSL or TLS, as otherwise an attacker could obtain the password.</p><p>- **Encrypted User/Password**</p><p>The user name and password are hashed before being sent to the server. This authentication mechanism (CRAM-MD5 and DIGEST-MD5) is secure even without encryption.</p><p>- **OAuth2 Authorization Code**</p><p>The authentification is done via an authorization server as an intermediary step. The client can exchange the OAuth2 Authorization Code for an access token. Your user credentials are never shared with the client.</p><p></p>|
|<p>**Credential Name**</p><p>(only if **Plain User Name/Password** or **Encrypted User/Passwor**d is selected for **Authentication**)</p>|Specifies the name of a deployed credential to use for authentication.|
|**From**|E-mail address that the message comes from.|
|**To**|<p>E-mail address that the message is sent to.</p><p>If you want to configure multiple mail receivers, use a comma (,) to separate the addresses.</p><p>Example: name1@example.com, name2@example.com, name3@example.com</p>|
|<p>**download\_ftp**</p><p></p>|<p>Specifies the host name and (optionally) a port number of the SMTP server.</p><p>Duplicated from above configuration.</p>|

Each successfully ran or failed iflow will send a respective message to the chosen list of email addresses. In case of successful run the message contain the name of event, f.e.  “This is an information message for your BTP Integration flow - Download Events, the event "Export Payment Requests””
In the case of failed flow you will get details of the error. This includes the latest payload in a form of attachment (if you reached Ariba unsuccessfully then this contains Ariba response), short description of the exception, the latest message in the body of your flow, which would contain Ariba response if you reached it. The same information is also stored in your sftp server in the logdir directory. We would suggest to enable monitoring on your iflow with a trace level, rerun the flow with the failed file (which is stored in the logdirectory with the same name as the logtrace file) and review the logs for this specific run.




For more info, refer also to <https://help.sap.com/docs/CLOUD_INTEGRATION/368c481cd6954bdfa5d0435479fd4eaf/f68d5e03fd574f509f89474f6a6e272a.html> 

### Receiver – SAPAriba
![](Aspose.Words.bc375156-f807-49ff-b347-465fd4a999c6.004.png)

|**Parameter**|**Example Value and Notes**|
| :- | :- |
|<p>**realm**</p><p></p>|<p>Site ID to which to direct HTTP requests </p><p>Use the actual realm ID of your site (Buying site for Buying iFlow, Sourcing one for Sourcing iFlow)</p>|
|**Timeout (in ms)**|<p>Specifies the network timeout for the connection attempt to the server.</p><p>The default value is 30000. The timeout must be larger than 0. Helpful tip – for the first run, we suggest to use larger values especially if you have bigger amount of data. After the first run you can decrease this number as the rest of the downloads will be incremental (at least for transactional data). If you have so much data, that the flow is unable to download them even with large timeouts, then we recommend to download the data first time manually in SAP Ariba and set the initial start date to the day of the manual download. </p><p></p>|

### More – All Parameters
![](Aspose.Words.bc375156-f807-49ff-b347-465fd4a999c6.005.png)


|**Parameter**|**Example Value and Notes**|
| :- | :- |
|<p>**datacenter**</p><p></p>|<p>Datacenter of your Ariba realm(s). Value has to match input in Value mappings</p><p>e.g. us, eu, cn, other…</p>|
|**event**|<p>The name of the integration event to run. You can define within one flow as many events as you wish. **They have to be separated with ; with no spaces before and after the event name.** Each event flows will be processed and saved separately, however in case that one of them will fail with a large timeout, the flow can stop completely, so we recommend to put large files into separate flows.  </p><p>Download events preconfigured for Ariba Buying are:</p><p>- User Consolidated Export</p><p>- Export Purchase Order</p><p>- Export Cancel Purchase Order</p><p>- Export Change Purchase Order</p><p>- Export Payment Requests</p><p>- Export Receipts</p><p>- Group Mapping Consolidated Export</p><p>Download events preconfigured for Ariba Sourcing are:</p><p>- Export Enterprise Users</p><p>- Export Supplier Profiles</p>|
|**first\_run\_start\_date**|The date from when the transactional data will be exported during the first run. After successful export, the system creates a timestamp and every other run will be a download from the latest timestamp. <br>The default value is  2022-01-01T00:00:00Z , if you need to download older data you can change this stamp to the past, however please note that very large files might end up in a timeout. If you have so much data, that the flow is unable to download them even with large timeouts, then we recommend to download the data first time manually in SAP Ariba and set the initial start date to the day of the manual download. When updating the value, please respect the format of the date.|
|**realm**|<p>Site ID of your Ariba realm </p><p>Use the actual realm ID of your site (Buying site for Buying iFlow, Sourcing one for Sourcing iFlow).</p><p>Duplicated from above configuration.</p>|
|**sharedsecretpath**|Name of security parameter created in the Security section above e.g. AribaBuyingSharedSecretTest|

# Data stores
The download events flows are using SAP BTP data stores in order to store the successful event run. You can review your data stores created in the monitor section – manage data stores. Data store is specific to the iflow where it was created, so you cannot reuse timestamps across different flows. Each event creates information of its successful run as “runtime”. This defines that during the next run, the latest timestamp of the flow will be used. If the flow hasn’t run more than two days, then the timestamp will be marked as overdue. If the flow hasn’t run more than a month than the timestamp would be invalidated and the next run would use the initial start date. To avoid this, please make sure that your flows are running on daily basis. 

![](Aspose.Words.bc375156-f807-49ff-b347-465fd4a999c6.006.png)

# Helpful tips

- User credentials for SFTP and mail server as well as secure parameters are created under the monitor section – manage security – security material
- You can test your SFTP and email connection credentials in the same section in the tile connectivity tests, use them in your flows only if the test passed
- Please follow the SAP Ariba documentation in order to use the proper event names of your downloaded content
- Buying and sourcing flows are not interchangeable so please make sure to use the correct combination of flow and solution

# Troubleshooting
In case of correctly setup flow, in case of any issues you will get an email as well as log stored into your SFTP server into logdir directory. This includes the latest payload in a form of attachment (if you reached Ariba unsuccessfully then this contains Ariba response), short description of the exception, the latest message in the body of your flow, which would contain Ariba response if you reached it. The same information is also stored in your sftp server in the logdir directory. We would suggest to enable monitoring on your iflow with a trace level, rerun the flow with the failed file (which is stored in the logdirectory with the same name as the logtrace file) and review the logs for this specific run.

# Typical issues for file download:
- Wrong shared secret or shared secret not enabled on SAP Ariba site, SAP Ariba will throw an authentication issue in its response
- Secure Parameter not available – the shared secret was not setup following the guidelines above
- HTTP operation failed invoking with statusCode: 500, details from Ariba Throwable caught: Unexpected Error: Can't coerce variant – this means wrong event name specified, please double check for any spacing before and after the event name as well as validity of the name according to the documentation and that it is enabled for file export
- Timeout exception – if you file is too large or the servers too slow, you might need to update timeouts set, if you are unable to find a suitable timeout, we suggest to download the file manually the first time and update the first\_run\_start\_date to the date of the download
- SFTP issues – please review the documentation of the SFTP connector
- Email issues – please review the documentation of the Email connector
- No such file exist error - this can happen in some of the SFTP servers in case that they do not support special characters in file names, to try to resolve this, you can edit the package, update the SFTP connection from "Saving downloaded files", go to target, and remove ${header.timestamp} from the file name


