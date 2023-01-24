package net.pricefx.pckg.client.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.pckg.client.okhttp.authenticator.AuthV1;
import net.pricefx.pckg.client.okhttp.authenticator.AuthV2;
import okhttp3.Authenticator;
import okhttp3.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

public class PfxClientBuilder extends PfxClient.Builder {
    private String appKey = null;
    private boolean keepConnectionAlive = false;

    public PfxClientBuilder(String rootUrl, String partition) {
        super(rootUrl, partition);
    }

    public PfxClient.Builder appKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public PfxClient.Builder keepConnectionAlive(boolean keepConnectionAlive) {
        this.keepConnectionAlive = keepConnectionAlive;
        return this;
    }

    @Override
    public AuthV1 getDefaultAuthenticator() {
        if (!StringUtils.isEmpty(appKey)) {
            return new AuthV2(appKey);
        } else {
            AuthV1 authenticator = new AuthV1();
            authenticator.setBasicCredentials(credentials);
            authenticator.setJwt(jwt);
            return authenticator;
        }
    }

    @Override
    public PfxClient build() {
        if (this.mapper == null) {
            this.mapper = new ObjectMapper();
        }

        PfxSession pfxSession = new PfxSession();
        this.builder.cookieJar(pfxSession);
        this.builder.networkInterceptors().removeIf(PfxSession.class::isInstance);
        this.builder.networkInterceptors().add(0, pfxSession);
        if (this.authenticator == null) {
            this.authenticator(this.getDefaultAuthenticator());
        }

        if (!this.preemptiveAuthentication && !this.authenticator.isJwt()) {
            this.builder.authenticator(this.authenticator);
        } else {
            this.builder.networkInterceptors().removeIf(Authenticator.class::isInstance);
            this.builder.networkInterceptors().add(0, this.authenticator);
        }

        this.builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        return new PFXOperationClient(this, keepConnectionAlive);
    }

}
