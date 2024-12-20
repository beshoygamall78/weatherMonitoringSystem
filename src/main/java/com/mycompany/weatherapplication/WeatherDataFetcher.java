package com.mycompany.weatherapplication;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class WeatherDataFetcher {
    public static WeatherDataFetcher instance = new WeatherDataFetcher();
    private String unitTemp = "Celsius"; // Default unit for temperature
    private String unitWind = "km/h";   // Default unit for wind speed

    private WeatherDataFetcher() {}

    public static WeatherDataFetcher getInstance() {
        return instance;
    }

    public void setUnitTemp(String unit) {
        this.unitTemp = unit;
    }

    public void setUnitWind(String unit) {
        this.unitWind = unit;
    }

    public String fetchWeatherData(String location) throws KeyManagementException {
        // Override SSL Verification
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCertificates, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        String apiKey = "2d7aa9f08e8f47189b7204853241112"; // Replace with your API key
        String urlString = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + location;

        try {
            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUnitTemp() {
        return unitTemp;
    }

    public String getUnitWind() {
        return unitWind;
    }
}
