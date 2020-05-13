package com.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HttpClient implements AutoCloseable {

    private final HttpURLConnection httpURLConnection;

    HttpClient(String urlString) throws IOException {
        URL url = new URL(urlString);
        httpURLConnection = (HttpURLConnection) url.openConnection();
    }

    String getResponse(boolean isPost) throws ProtocolException {
        if(isPost)
            httpURLConnection.setRequestMethod("POST");
        return parseResponse();
    }

    private String parseResponse() {
        try {
            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }
            return responseBuilder.toString();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
        if(httpURLConnection != null) {
            System.out.println("Disconnecting Client");
            httpURLConnection.disconnect();
        }
    }
}
