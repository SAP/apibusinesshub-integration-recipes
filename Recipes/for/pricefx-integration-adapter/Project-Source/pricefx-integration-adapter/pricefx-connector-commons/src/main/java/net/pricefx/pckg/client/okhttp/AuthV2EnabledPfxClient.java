package net.pricefx.pckg.client.okhttp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.client.okhttp.authenticator.AuthV2;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Objects;

import static net.pricefx.connector.common.validation.ConnectorException.ErrorType.HTTP_INTERNAL_ERROR;


public class AuthV2EnabledPfxClient extends PfxClient {
    private static final String JSON_MEDIA_TYPE = "application/json; charset=utf-8";
    private static final MediaType JSON = MediaType.parse(JSON_MEDIA_TYPE);

    protected AuthV2EnabledPfxClient(Builder builder) {
        super(builder);
    }

    public void updateOAuthToken(String token) {
        if (authenticator instanceof AuthV2) {
            ((AuthV2) authenticator).setToken(token);
        }
    }

    @Override
    public String getToken() {
        if (authenticator instanceof AuthV2) {
            return ((AuthV2) authenticator).getToken();
        } else {
            return super.getToken();
        }

    }

    public void deleteAccessToken(String apiPath) throws IOException {
        okhttp3.Request.Builder builder = createTokenRequest(apiPath);
        builder.delete();
        Response response = null;
        try {
            response = this.execute(builder);

            if (!response.isSuccessful()) {
                throw (new PfxClientException(String.format("HTTP request failed, HTTP status code: %d ", response.code()))).withHttpStatus(response.code());
            }

        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private okhttp3.Request.Builder createTokenRequest(String apiPath) {

        okhttp3.HttpUrl.Builder urlBuilder = this.baseUrl.newBuilder(apiPath);
        if (urlBuilder == null) {
            throw new ConnectorException(HTTP_INTERNAL_ERROR, "unable to resolve TOKEN path");
        } else {
            Objects.requireNonNull(urlBuilder);
            return (new okhttp3.Request.Builder()).url(urlBuilder.build());
        }

    }

    public ObjectNode getAccessToken(String apiPath, Object body) throws IOException {
        body = this.bodyTransformation.apply(this, "POST", apiPath, body);
        okhttp3.Request.Builder builder = createTokenRequest(apiPath);
        builder.post(RequestBody.create(JSON, this.mapper.writeValueAsString(body)));
        builder.addHeader("Content-Type", JSON_MEDIA_TYPE);
        builder.addHeader("Accept", "application/json");

        Response response = null;

        try {
            response = this.execute(builder);
            ResponseBody responseBody = response.body();

            if (!response.isSuccessful() || responseBody == null) {
                String bodyString = responseBody != null ? responseBody.string() : null;
                PfxClientException ex = (new PfxClientException(String.format("HTTP request failed, HTTP status code: %d , body: %s", response.code(), bodyString))).withHttpStatus(response.code());

                try {
                    JsonNode jsonBody = this.mapper.readTree(bodyString);
                    ex = ex.withBody(jsonBody);
                } catch (Exception e) {
                    //ignore
                }

                throw ex;
            }

            JsonNode tokenResponse = this.mapper.readTree(responseBody.charStream());
            if (tokenResponse != null && tokenResponse.isObject()) {
                return (ObjectNode) tokenResponse;
            }

            return new ObjectNode(JsonNodeFactory.instance);
        } finally {
            if (response != null) {
                response.close();
            }
        }


    }


}
