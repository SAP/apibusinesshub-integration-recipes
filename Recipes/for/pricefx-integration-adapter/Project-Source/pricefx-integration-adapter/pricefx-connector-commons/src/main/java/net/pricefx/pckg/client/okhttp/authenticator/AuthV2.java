package net.pricefx.pckg.client.okhttp.authenticator;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;


public class AuthV2 extends AuthV1 {
    private static final String HEADER_PRICEFX_KEY = "Pricefx-Key";

    private final String appKey;
    private String token;

    public AuthV2(String appKey) {
        this.appKey = appKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Request authenticate(Route route, Response response) {
        Response prior = response.priorResponse();
        if (isUnauthorized(prior) && appKey == null) {
            return null;
        }

        return addSecurityHeaders(response.request());
    }

    private Request addSecurityHeaders(Request request) {

        Request.Builder builder = request.newBuilder();
        builder.header(HEADER_PRICEFX_KEY, appKey);
        if (token != null) {
            builder.header(HTTP_HEADER_AUTHORIZATION, "Bearer " + token);
        }

        return builder.build();
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        return chain.proceed(addSecurityHeaders(chain.request()));
    }

    @Override
    public boolean isJwt() {
        return false;
    }

    @Override
    public void setJwt(String s) {
        throw new UnsupportedOperationException("JWT token not supported in OAuth");

    }

    @Override
    public String getAuthorizationHeader() {
        return null;
    }

    @Override
    public String getTfaVerificationCode() {
        return null;
    }

    @Override
    public String getTfaToken() {
        return null;
    }

    @Override
    public String getJwt() {
        return null;
    }

    public String getToken() {
        return token;
    }


    @Override
    public void setTfaToken(String tfaToken) {
        throw new UnsupportedOperationException("TFA token not supported in OAuth");
    }

    @Override
    public void setTfaVerificationCode(String tfaVerificationCode) {
        throw new UnsupportedOperationException("TFA token not supported in OAuth");
    }

}
