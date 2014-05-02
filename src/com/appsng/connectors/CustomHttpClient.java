package com.appsng.connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class CustomHttpClient {
	/** The time it takes for our client to timeout */
    public static final int HTTP_TIMEOUT = 100000 * 1000; // milliseconds

    /** Single instance of our HttpClient */
    private static HttpClient mHttpClientz;
    private static boolean gzipEnabled = false;
    /**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     */
    public static HttpClient getHttpClient() {
        if (mHttpClientz == null) {
        	mHttpClientz = new DefaultHttpClient();
            final HttpParams params = mHttpClientz.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClientz;
    }

    
    public class HttpSigleton {
    	  private HttpClient instance = null;
    	  protected HttpSigleton() {

    	  }
    	  public HttpClient getInstance() {
    	    if(instance == null) {
    	       instance =new DefaultHttpClient();
    	    }
    	    return instance;
    	 }
    	}
    /**
     * Performs an HTTP Post request to the specified url with the
     * specified parameters.
     *
     * @param url The web address to post the request to
     * @param postParameters The parameters to send via the request
     * @return The result of the request
     * @throws Exception
     */
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            
           
            
              
            HttpResponse response = client.execute(request);
            
            InputStream instream = response.getEntity().getContent();
            in = new BufferedReader(new InputStreamReader(instream));

            
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Performs an HTTP GET request to the specified url.
     *
     * @param url The web address to post the request to
     * @return The result of the request
     * @throws Exception
     */
    public static String executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
