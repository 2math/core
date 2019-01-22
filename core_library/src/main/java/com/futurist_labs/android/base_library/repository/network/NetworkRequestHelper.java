package com.futurist_labs.android.base_library.repository.network;

import android.util.Log;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.model.ServerError;
import com.futurist_labs.android.base_library.repository.persistence.BaseJsonParser;
import com.futurist_labs.android.base_library.utils.LogUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by Galeen on 18.1.2016 г..
 * Implements most network request, CRUD
 * in use for ServerOperation
 * all methods must be called in background
 */
public class NetworkRequestHelper {
    static final String CHARSET = "UTF-8";


    static NetworkResponse sendPost(String endpoint, String body, String authToken, ServerEvents
            callback) throws IOException {
        return sendPost(endpoint, body, authToken, "application/json", callback, null);

    }

    static NetworkResponse sendPostWithParams(String endpoint, String params, String authToken, ServerEvents
            callback) throws IOException {
        return sendPost(endpoint, params, authToken, "application/x-www-form-urlencoded", callback, null);

    }

    static NetworkResponse sendPost(String endpoint, String body, String authToken, ServerEvents
            callback, Map<String, String> headers) throws IOException {
        return sendPost(endpoint, body, authToken, "application/json", callback, headers);

    }

    static NetworkResponse sendPostWithParams(String endpoint, String params, String authToken, ServerEvents
            callback, Map<String, String> headers) throws IOException {
        return sendPost(endpoint, params, authToken, "application/x-www-form-urlencoded", callback, headers);

    }

    public static void init() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                }
        };
        try {
//            HttpsURLConnection.setDefaultHostnameVerifier(new CustomHostnameVerifier("budeshte.bg"));
            new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();

                    Log.e("adr", s);
                    return s.equalsIgnoreCase("api.kliner.fr") || s.equalsIgnoreCase("maps.googleapis.com") ||
                            s.equalsIgnoreCase("www.google.com") || s.equalsIgnoreCase("api.parse.com")
                            || s.equalsIgnoreCase("clients4.google.com") || s.equalsIgnoreCase("csi.gstatic.com");
                }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    static NetworkResponse sendPost(String endpoint, String params, String authToken, String type,
                                    ServerEvents callback, Map<String, String> headers) throws
            IOException {
        URL url = new URL(endpoint);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                HostnameVerifier hv =
//                        HttpsURLConnection.getDefaultHostnameVerifier();
//                return hv.verify("api.kliner.fr", session);
//            }
//        };
//        conn.setHostnameVerifier(hostnameVerifier);

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", type);
        if (authToken != null) {
            conn.setRequestProperty(NetConstants.HEADER_AUTHORIZATION, authToken);
//            conn.setRequestProperty("Accept", "*/*");
        }
        if (NetConstants.SHOULD_ADD_LOCALE)
            conn.setRequestProperty(NetConstants.HEADER_LOCALE_FIELD, BaseLibraryConfiguration.getInstance().getHeaderLocaleFieldValue());

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        OutputStream os = conn.getOutputStream();
        if (params != null)
            os.write(params.getBytes());
        os.flush();
        os.close();
        if (callback != null)
            callback.beforeToReceiveResponse();
//        Operation serverResult = sendDataToServer(conn);
        return sendDataToServerParams(conn);
    }

    static NetworkResponse sendAuthenticatedPut(String endpoint, String body, String authToken) throws Exception {
        return sendPut(endpoint, body, authToken);
    }

    static NetworkResponse sendPut(String endpoint, String body, String authToken) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        if (authToken != null) {
            conn.setRequestProperty(NetConstants.HEADER_AUTHORIZATION, authToken);
        }
        if (NetConstants.SHOULD_ADD_LOCALE)
            conn.setRequestProperty(NetConstants.HEADER_LOCALE_FIELD, BaseLibraryConfiguration.getInstance().getHeaderLocaleFieldValue());
//        conn.setConnectTimeout(30000);
        OutputStream os = conn.getOutputStream();
        if (body != null)
            os.write(body.getBytes());
        os.flush();
        os.close();

//        Operation serverResult = sendDataToServer(conn);
        return sendDataToServerParams(conn);
    }

    static NetworkResponse sendDelete(String endpoint, String body, String authToken) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-Type", "application/json");
        if (authToken != null) {
            conn.setRequestProperty(NetConstants.HEADER_AUTHORIZATION, authToken);
        }
        if (NetConstants.SHOULD_ADD_LOCALE)
            conn.setRequestProperty(NetConstants.HEADER_LOCALE_FIELD,
                    BaseLibraryConfiguration.getInstance().getHeaderLocaleFieldValue());

        OutputStream os = conn.getOutputStream();
        if (body != null)
            os.write(body.getBytes());
        os.flush();
        os.close();
        return sendDataToServerParams(conn);
    }

    static NetworkResponse sendDataToServerParams(HttpURLConnection conn) {
        NetworkResponse networkResponseBody = null;
        try {
            int responseCode = conn.getResponseCode();//here is when the call is made
//            if(conn.getHeaderFields() != null && conn.getHeaderField(Constants.HEADER_AUTHORIZATION) != null)
//                PersistenceManager.setToken(conn.getHeaderField(Constants.HEADER_AUTHORIZATION));
            networkResponseBody = getResponseBody(conn, responseCode);
            networkResponseBody.url = conn.getURL().toString();
        } catch (IOException e) {
            e.printStackTrace();
            networkResponseBody = new NetworkResponse(e.getMessage());
            networkResponseBody.url = conn.getURL().toString();
        } finally {
            conn.disconnect();
        }
        return networkResponseBody;
    }

    static NetworkResponse sendGet(String serverUrl, Map<String, String> params) throws Exception {
        return sendGet(serverUrl, params, null);
    }

    static NetworkResponse sendAuthenticatedGet(String serverUrl, String token, Map<String, String> params, Map<String, String> headers)
            throws
            Exception {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (token != null)
            headers.put(NetConstants.HEADER_AUTHORIZATION, token);
        if (NetConstants.SHOULD_ADD_LOCALE)
            headers.put(NetConstants.HEADER_LOCALE_FIELD,
                    BaseLibraryConfiguration.getInstance().getHeaderLocaleFieldValue());
        return sendGet(serverUrl, params, headers);
    }

    static NetworkResponse sendGet(String serverUrl, Map<String, String> params, Map<String, String> headers) throws Exception {
//        serverUrl = serverUrl;
        URL url;
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue(), "UTF-8"));
            }
            url = new URL(serverUrl + "?" + sb.toString());
        } else {
            url = new URL(serverUrl);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
//        return sendDataToServer(conn);
        return sendDataToServerParams(conn);
    }

    static NetworkResponse sendGetPlaces(String serverUrl) throws Exception {
//        serverUrl = Constants.SERVER_ADDRESS + serverUrl;
        URL url = new URL(serverUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        return sendDataToServerParams(conn);
    }


    private static NetworkResponse getResponseBody(HttpURLConnection conn, int responseCode) throws IOException {
        StringBuilder responseBody = new StringBuilder("");
        InputStream is = null;
        boolean readError = false;
        if (responseCode < 300) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
            readError = true;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            responseBody.append(line);
        }
        br.close();
        ServerError serverError = null;
        if (readError) {
            serverError = BaseJsonParser.readError(responseBody.toString());
        }
        return new NetworkResponse(conn.getHeaderField("Last-Modified"),
                conn.getHeaderField("Set-Cookie"),
                responseBody.toString(), responseCode, serverError);
//        return responseBody;
    }

    static NetworkResponse postFile(String urlLink, File uploadFile, String token, Map<String, String>
            properties)
            throws
            IOException {
        String attachmentName = "file";
//        String attachmentFileName = "avatar.bmp";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        HttpURLConnection httpUrlConnection = null;
        URL url = null;
        try {
            url = new URL(urlLink);//NetConstants.SERVER_ADDRESS + "client/avatar");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);
//        if (token != null)
//            httpUrlConnection.setRequestProperty(NetConstants.HEADER_AUTHORIZATION, token);
        if (NetConstants.SHOULD_ADD_LOCALE)
            httpUrlConnection.setRequestProperty(NetConstants.HEADER_LOCALE_FIELD,
                    BaseLibraryConfiguration.getInstance().getHeaderLocaleFieldValue());
        DataOutputStream request = null;
        try {
            request = new DataOutputStream(
                    httpUrlConnection.getOutputStream());

            if (properties != null) {
                for (Map.Entry<String, String> property : properties.entrySet()) {
                    request.writeBytes(twoHyphens + boundary + crlf);
                    request.writeBytes("Content-Disposition: form-data; name=\"" +
                            property.getKey() + "\";" +
                            property.getValue() + "\"" + crlf);
                    request.writeBytes(crlf);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//I want to send only 8 bit black & white bitmaps
//        byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
//        for (int i = 0; i < bitmap.getWidth(); ++i) {
//            for (int j = 0; j < bitmap.getHeight(); ++j) {
//                //we're interested only in the MSB of the first byte,
//                //since the other 3 bytes are identical for B&W images
//                pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
//            }
//        }

        //for the file
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                attachmentName + "\";filename=\"" +
                uploadFile.getName() + "\"" + crlf);
        request.writeBytes(crlf);
        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            request.write(buffer, 0, bytesRead);
        }

//            request.write(pixels);
        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary +
                twoHyphens + crlf);
        request.flush();
        request.close();

        return sendDataToServerParams(httpUrlConnection);
    }

    static NetworkResponse multipartRequest(String urlTo, Map<String, String> params, File file, String fileField, String fileMimeType, String token) throws IOException {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

//        String[] q = filepath.split("/");
//        int idx = q.length - 1;

        try {
//            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            if (token != null) {
                connection.setRequestProperty(NetConstants.HEADER_AUTHORIZATION, token);
            }
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileField + "\"; filename=\"" + file
                    .getName() + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = params.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            return sendDataToServerParams(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return new NetworkResponse(e.getMessage());
        }
    }

    static NetworkResponse multipartRequest(String urlTo, Map<String, String> params, ArrayList<File> files, ArrayList<String> fileFields, String token) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

//        String[] q = filepath.split("/");
//        int idx = q.length - 1;

        try {
//            File file = new File(filepath);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            if (token != null) {
                connection.setRequestProperty(NetConstants.HEADER_AUTHORIZATION, token);
            }
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());

            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                String fileField = fileFields.get(i);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileField
                        + "\"; filename=\"" + file.getName()
                        + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: " + ServerOperation.getMimeType(file) + lineEnd);
                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

                outputStream.writeBytes(lineEnd);

                FileInputStream fileInputStream = new FileInputStream(file);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);
                fileInputStream.close();
            }

            if (params != null) {
                // Upload POST Data
                Iterator<String> keys = params.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = params.get(key);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(value);
                    outputStream.writeBytes(lineEnd);
                }
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            outputStream.flush();
            outputStream.close();

            return sendDataToServerParams(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return new NetworkResponse(e.getMessage());
        }
    }

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     *
     * @param fileURL HTTP URL of the file to be downloaded
     * @param file    File to save
     * @throws IOException
     */
    static NetworkResponse downloadFile(String fileURL, File file, String authToken, Map<String, String> headers)
            throws IOException {
        NetworkResponse networkResponse;
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        if (authToken != null) {
            httpConn.setRequestProperty(NetConstants.HEADER_AUTHORIZATION, authToken);
        }

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpConn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
            LogUtils.d("downloadFile", "File in result" +
                    "\nContent-Type = " + contentType
                    + "\nContent-Disposition = " + disposition
                    + "\nContent-Length = " + contentLength
                    + "\nfileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(file);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            networkResponse = new NetworkResponse<File>(responseCode, file);
            networkResponse.url = url.toString();
            LogUtils.d("downloadFile", "File downloaded");
        } else {
            networkResponse = new NetworkResponse("No file to download. Server replied HTTP code: " + responseCode, responseCode);
            LogUtils.d("downloadFile", "No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
        return networkResponse;
    }

    public interface ServerEvents {
        void beforeToReceiveResponse();
    }
}
