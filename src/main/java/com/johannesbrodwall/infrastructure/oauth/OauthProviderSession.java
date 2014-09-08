package com.johannesbrodwall.infrastructure.oauth;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.AppConfiguration;
import com.johannesbrodwall.infrastructure.HttpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OauthProviderSession {

    protected final OauthProvider provider;

    @Getter
    protected String username;

    @Getter
    protected String fullName;

    @Setter
    private String errorMessage;

    protected String accessToken;

    public OauthProviderSession(OauthProvider provider) {
        this.provider = provider;
    }

    public boolean isLoggedIn() {
        return accessToken != null;
    }

    public void fetchAuthToken(String code, String redirectUri) throws IOException {
        JSONObject tokenResponse = HttpUtils.httpPostJson(
                new URL(provider.getTokenUrl()),
                provider.getTokenRequestPayload(code, redirectUri));
        parseToken(tokenResponse);
    }

    private void parseToken(JSONObject tokenResponse) {
        String idToken = tokenResponse.getString("id_token");
        String idTokenPayload = base64Decode(idToken.split("\\.")[1]);
        log.info("ID token: {}", idTokenPayload);
        JSONObject payload = new JSONObject(idTokenPayload);
        username = payload.getString("email");
        accessToken = tokenResponse.getString("access_token");
    }

    private String base64Decode(String jwt) {
        return new String(Base64.getDecoder().decode(jwt));
    }

    public void fetchProfile() throws IOException {
        JSONObject object = HttpUtils.httpGetWithToken(provider.getProfileUrl(), accessToken);
        fullName = object.getString("displayName");
    }

    public static OauthProviderSession createGoogleSession(AppConfiguration configuration) {
        OauthProvider provider = new OauthProvider("google", configuration);
        provider.setClientSignup("https://console.developers.google.com/project");
        provider.setAuthUrl("https://accounts.google.com/o/oauth2/auth");
        provider.setTokenUrl("https://accounts.google.com/o/oauth2/token");
        provider.setProfileUrl("https://www.googleapis.com/plus/v1/people/me");
        provider.setSignupPicture("sign-in-with-google.png");
        provider.setScope("profile email");
        return new OauthProviderSession(provider);
    }

    public String getAuthUrl(String redirectUri) {
        return provider.getAuthUrl(redirectUri);
    }

}
