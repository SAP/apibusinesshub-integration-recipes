# Generate AWS4-HMAC-SHA256 Signature
\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Generate%20AWS4-HMAC-SHA256%20Signature ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Generate%20AWS4-HMAC-SHA256%20Signature ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Generate%20AWS4-HMAC-SHA256%20Signature ) \|

![Sunny Kapoor](https://github.com/simplykapoor.png?size=50 )|[Sunny Kapoor](https://github.com/simplykapoor)|
----|----|

This reusable recipe generates an AWS specific AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header.\
The main challenge is to generate AWS4-HMAC-SHA256 signature and pass it as a HTTP Authorization header. Creating this signature is a multi-step procedure and if single step goes wrong, AWS gives the signature mismatch error.\
You can directly use the reusable integration flow extension to avoid building this functionality again.

[Download the reusable Integration Flow](Generate_AWS4-HMAC-SHA256_Authorization_Header.zip)

## Recipe

Step|Code|Why?
----|----|----
Access all the required headers| Refer the given sample script| This is required to create a Canonical Request
Retrieve the AWS Access Key and Secret Key| Refer [How to read user credentials in Groovy Script via API](../AccessCredentialGroovy) | AWS Secret Key is required for calculating the Signature and AWS Access Key is passed in the Authorization header
Create a Canonical Request| Refer the given sample script| Refer [Signature Version 4 Signing Process](https://docs.aws.amazon.com/general/latest/gr/signature-version-4.html) to know about the Canonical Request
Create the String To Sign| Refer the given sample script| Refer [Signature Version 4 Signing Process](https://docs.aws.amazon.com/general/latest/gr/signature-version-4.html) to know about how to create a string for signing.
Calculate the Signature| Refer the given sample script| Refer [HMAC-SHA256 Signatures for REST Requests](https://docs.aws.amazon.com/AWSECommerceService/latest/DG/HMACSignatures.html) to know about HMAC-SHA256 Signatures for REST Requests
Put the Signature and Timestamp as headers|```message.setHeader("Authorization", authorization_header);``` ```message.setHeader("X-Amz-Date",amz_date);``` |This is required to authenticate the AWS Service Rest Request

### Related Recipes
* [How to read user credentials in Groovy Script via API](../AccessCredentialGroovy)

## References
* [HMAC-SHA256 Signatures for REST Requests](https://docs.aws.amazon.com/AWSECommerceService/latest/DG/HMACSignatures.html)
* [Signature Version 4 Signing Process](https://docs.aws.amazon.com/general/latest/gr/signature-version-4.html)

## Reusable Integration Flow
![iflowimage](Generate_AWS4_HMAC_SHA256_Snapshot.JPG)

This reusable integration flow gets triggered via ProcessDirect Adapter and accepts all the required headers using "Allowed Headers" configuration.

The groovy script then access those headers and generates the corresponding AWS4-HMAC-SHA256 Signature and set it as Authorization header.

### Sample Script
This is the script is a sample, it __skips complexities__ like reading credentials but instead _hard codes_ for simplicity. Please avoid such hard coding in the productive code.
```
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
def Message processData(Message message) {
    //************* REQUEST VALUES *************
    def headers = message.getHeaders();

    String method = headers.get("HTTPMethod");
    String host = headers.get("Host");
    String region = headers.get("AWS-Region");
    String service = headers.get("AWS-Service");
    String content_type = headers.get("Content-Type");
    String amz_target = headers.get("X-Amz-Target");

    // Request parameters for Create/Update new item--passed in a JSON block.
    String request_parameters = message.getBody(java.lang.String) as String;

    // Read AWS access key from security artifacts. Best practice is NOT to embed credentials in code and retrieve from User Credentials
    String access_key = '<Your AWS Access Key>';
    String secret_key = '<Your AWS Secret Key>';

    // Create a date for headers and the credential string
    def date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));//server timezone
    String amz_date = dateFormat.format(date);
    dateFormat = new SimpleDateFormat("yyyyMMdd");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));//server timezone
    String date_stamp = dateFormat.format(date);

    // ************* TASK 1: CREATE A CANONICAL REQUEST *************
    // http://docs.aws.amazon.com/general/latest/gr/sigv4-create-canonical-request.html

    // Step 1 is to define the verb (GET, POST, etc.)--already done.

    // Step 2: Create canonical URI--the part of the URI from domain to query
    // string (use '/' if no path)
    String canonical_uri = '/';

    // Step 3: Create the canonical query string. In this example, request
    // parameters are passed in the body of the request and the query string is blank.
    String canonical_querystring = '';

    // Step 4: Create the canonical headers. Header names must be trimmed
    // and lowercase, and sorted in code point order from low to high. Note that there is a trailing \n.
    String canonical_headers = 'content-type:' + content_type + '\n' + 'host:' + host + '\n' + 'x-amz-date:' + amz_date + '\n' + 'x-amz-target:' + amz_target + '\n';

    // Step 5: Create the list of signed headers. This lists the headers
    // in the canonical_headers list, delimited with ";" and in alpha order.
    // Note: The request can include any headers; canonical_headers and
    // signed_headers include those that you want to be included in the
    // hash of the request. "Host" and "x-amz-date" are always required.
    String signed_headers = 'content-type;host;x-amz-date;x-amz-target';

    // Step 6: Create payload hash. In this example, the payload (body of the request) contains the request parameters.
    String payload_hash = generateHex(request_parameters);

    // Step 7: Combine elements to create canonical request
    String canonical_request = method + '\n' + canonical_uri + '\n' + canonical_querystring + '\n' + canonical_headers + '\n' + signed_headers + '\n' + payload_hash;

    // ************* TASK 2: CREATE THE STRING TO SIGN*************
    // Match the algorithm to the hashing algorithm you use, either SHA-1 or SHA-256 (recommended)
    String algorithm = 'AWS4-HMAC-SHA256';
    String credential_scope = date_stamp + '/' + region + '/' + service + '/' + 'aws4_request';
    String string_to_sign = algorithm + '\n' +  amz_date + '\n' +  credential_scope + '\n' +  generateHex(canonical_request);

    // ************* TASK 3: CALCULATE THE SIGNATURE *************
    // Create the signing key using the function defined above.
    byte[] signing_key = getSignatureKey(secret_key, date_stamp, region, service);

    // Sign the string_to_sign using the signing_key
    byte[] signature = HmacSHA256(string_to_sign,signing_key);

     /* Step 3.2.1 Encode signature (byte[]) to Hex */
    String strHexSignature = bytesToHex(signature);

    // ************* TASK 4: ADD SIGNING INFORMATION TO THE REQUEST *************
    // Put the signature information in a header named Authorization.
    String authorization_header = algorithm + ' ' + 'Credential=' + access_key + '/' + credential_scope + ', ' +  'SignedHeaders=' + signed_headers + ', ' + 'Signature=' + strHexSignature;

    // set X-Amz-Date Header and Authorization Header
    message.setHeader("Authorization", authorization_header);
    message.setHeader("X-Amz-Date",amz_date);

    return message;
}

String bytesToHex(byte[] bytes) {
    char[] hexArray = "0123456789ABCDEF".toCharArray();            
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars).toLowerCase();
}

String generateHex(String data) {
    MessageDigest messageDigest;

    messageDigest = MessageDigest.getInstance("SHA-256");
    messageDigest.update(data.getBytes("UTF-8"));
    byte[] digest = messageDigest.digest();
    return String.format("%064x", new java.math.BigInteger(1, digest));
}

byte[] HmacSHA256(String data, byte[] key) throws Exception {
    String algorithm="HmacSHA256";
    Mac mac = Mac.getInstance(algorithm);
    mac.init(new SecretKeySpec(key, algorithm));
    return mac.doFinal(data.getBytes("UTF8"));
}

byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
    byte[] kDate = HmacSHA256(dateStamp, kSecret);
    byte[] kRegion = HmacSHA256(regionName, kDate);
    byte[] kService = HmacSHA256(serviceName, kRegion);
    byte[] kSigning = HmacSHA256("aws4_request", kService);
    return kSigning;
}
```
