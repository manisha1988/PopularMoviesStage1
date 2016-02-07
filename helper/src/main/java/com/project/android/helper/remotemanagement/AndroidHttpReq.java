package com.project.android.helper.remotemanagement;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AndroidHttpReq {

    private URL url;
    private JSONObject params;

    public AndroidHttpReq(String urlString) {
        try {
            url = new URL(urlString);
            params = new JSONObject();
        } catch (MalformedURLException e) {
            url = null;
            params = null;
        }
    }

    public void put(String key, String value) {
        try {
            params.put(key, value);
        } catch (Exception e) {
            //Ignore Exception
        }
    }

    /**
     * Get JSON data
     * @return
     * @throws Exception
     */
    public String httpGetRequest() {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            //Open Connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(20 * 1000);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                if (response.length() != 0) {
                    return response.toString();
                }
            }

            return null;
        } catch (IOException e) {
            Log.e(AndroidHttpReq.class.getSimpleName(), "Error", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(AndroidHttpReq.class.getSimpleName(), "Error Closing stream", e);
                }
            }
        }
    }

    /**
     * Post JSON request
     * @return
     * @throws Exception
     */
    public String httpPostRequest() {

        if (params.length() == 0)
            return null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        OutputStreamWriter writer = null;

        try {

            //Open Connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(20 * 1000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();

            if (outputStream != null) {
                writer = new OutputStreamWriter(outputStream);
                writer.write(params.toString());
                writer.flush();
            }

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                if (response.length() != 0) {
                    return response.toString();
                }
            }

            return null;
        } catch (IOException e) {
            Log.e(AndroidHttpReq.class.getSimpleName(), "Error", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(AndroidHttpReq.class.getSimpleName(), "Error Closing stream", e);
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(AndroidHttpReq.class.getSimpleName(), "Error Closing stream", e);
                }
            }
        }
    }

}
