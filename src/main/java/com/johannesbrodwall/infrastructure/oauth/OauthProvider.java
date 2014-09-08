package com.johannesbrodwall.infrastructure.oauth;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.AppConfiguration;

import java.net.URLEncoder;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class OauthProvider {

    private AppConfiguration configuration;

    private String getRequiredProperty(String property) {
        return configuration.getRequiredProperty("oauth2." + providerName + "." + property);
    }

    @Getter
    private String providerName;

    @Getter @Setter
    private String signupPicture;

    @Getter @Setter
    private String clientSignup;

    @Getter @Setter
    private String scope;

    @Getter @Setter
    private String authUrl;

    @Getter @Setter
    private String tokenUrl;

    @Getter @Setter
    private String profileUrl;

    public OauthProvider(String providerName, AppConfiguration configuration) {
        this.providerName = providerName;
        this.configuration = configuration;
    }

    @SneakyThrows String getAuthUrl(String redirectUrl) {
        if (getClientId() == null || getAuthUrl() == null) return null;

        return getAuthUrl()
            + "?response_type=code"
            + "&client_id=" + getClientId()
            + "&redirect_uri=" + URLEncoder.encode(redirectUrl, "UTF-8")
            + "&scope=" + getScope()
            + "&state=" + providerName;
    }

    private String getClientId() {
        return getRequiredProperty("clientId");
    }

    private String getClientSecret() {
        return getRequiredProperty("clientSecret");
    }

    String getTokenRequestPayload(String code, String redirectUri) {
        return ("code=" + code
                + "&client_id=" + getClientId()
                + "&client_secret=" + getClientSecret()
                + "&redirect_uri=" + redirectUri
                + "&grant_type=authorization_code");
    }

    JSONObject toJSON(String redirectUri) {
        return new JSONObject()
            .put("providerName", getProviderName())
            .put("displayName", providerName)
            .put("clientSignup", getClientSignup())
            .put("url", getAuthUrl(redirectUri))
            .put("signupImg", signupPicture);
    }

}
