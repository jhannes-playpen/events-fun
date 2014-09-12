package com.johannesbrodwall.infrastructure.oauth;

import com.johannesbrodwall.events.EventsAppConfiguration;

import java.io.IOException;

public class ClientUserSession {

    private OauthProviderSession providerSession = OauthProviderSession.createGoogleSession(EventsAppConfiguration.getInstance());
    private static ThreadLocal<ClientUserSession> current = new ThreadLocal<ClientUserSession>();

    public void fetchAuthToken(String code, String redirectUri) throws IOException {
        providerSession.fetchAuthToken(code, redirectUri);
    }

    public void fetchProfile() throws IOException {
        providerSession.fetchProfile();
    }

    public void setErrorMessage(String errorMessage) {
        providerSession.setErrorMessage(errorMessage);
    }

    public boolean isLoggedIn() {
        return providerSession.isLoggedIn();
    }

    public String getAuthUrl(String redirectUri) {
        return providerSession.getAuthUrl(redirectUri);
    }

    public static void setCurrent(ClientUserSession session) {
        current.set(session);
    }

    public static ClientUserSession getCurrent() {
        return current.get();
    }

    public String getDisplayName() {
        return providerSession.getFullName();
    }

}
