package com.johannesbrodwall.infrastructure;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtils {

    public static JSONObject httpPostJson(URL requestUrl, String payload) throws IOException {
        log.info("{}\n\tRequest:{}", requestUrl, payload);

        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        try (Writer writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(payload);
        }
        if (connection.getResponseCode() < 400) {
            try (InputStream inputStream = connection.getInputStream() ) {
                String response = slurp(inputStream);
                log.info("{}\n\tResponse:{}", requestUrl, response);
                return new JSONObject(response);
            }
        } else {
            try (InputStream inputStream = connection.getErrorStream() ) {
                throw new RuntimeException("Request to " + requestUrl + " failed: " + slurp(inputStream));
            }
        }
    }

    public static JSONObject httpGetJson(String requestUrl) throws IOException {
        return new JSONObject(httpGetString(requestUrl));
    }

    public static String httpGetString(String requestUrl) throws IOException {
        log.info(requestUrl.toString());
        HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() < 400) {
            try (InputStream inputStream = connection.getInputStream() ) {
                String response = slurp(inputStream);
                log.info("{}\n\tResponse:{}", requestUrl, response);
                return response;
            }
        } else {
            try (InputStream inputStream = connection.getErrorStream() ) {
                throw new RuntimeException("Request to " + requestUrl + " failed: " + slurp(inputStream));
            }
        }
    }

    public static Map<String, String> parseQuery(String response) {
        String[] propertyValues = response.split("&");
        Map<String,String> properties = new HashMap<String, String>();
        for (String property : propertyValues) {
            properties.put(property.substring(0, property.indexOf('=')),
                    property.substring(property.indexOf('=') + 1));
        }
        return properties;
    }

    private static String slurp(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream)).lines()
            .reduce((a, b) -> a + b)
            .get();
    }

    public static JSONObject httpGetWithToken(String requestUrl, String accessToken) throws IOException {
        log.info(requestUrl.toString());
        HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        if (connection.getResponseCode() < 400) {
            try (InputStream inputStream = connection.getInputStream() ) {
                String response = slurp(inputStream);
                log.info("{}\n\tResponse:{}", requestUrl, response);
                return new JSONObject(response);
            }
        } else {
            try (InputStream inputStream = connection.getErrorStream() ) {
                throw new RuntimeException("Request to " + requestUrl + " failed: " + slurp(inputStream));
            }
        }
    }
}
