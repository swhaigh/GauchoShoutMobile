package com.gauchoshout.android.http;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;

import com.gauchoshout.android.util.SessionStore;

/**
 * This abstract class handles all the heavy http work that is common to
 * all web service calls that we make. 
 *
 */
public abstract class WebServiceTask extends AsyncTask<NameValuePair, Void, Boolean> {
    
    private final String uri;
    protected String raw_json;
    private final String useragent;
    protected boolean tokenInvalid = false;
    // user agents of the form GAUCHOSHOUT/<tokenstring>
    // Each request to to the web server verifies the user agent before fulfilling requests.
    public WebServiceTask(String uri, Context ctx) {
        this.uri = uri;
        this.useragent = "GAUCHOSHOUT/" + SessionStore.getToken(ctx);
    }

    @Override
    protected Boolean doInBackground(NameValuePair... pairs) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(pairs.length);
        for (NameValuePair pair : pairs) {
            params.add(pair);
        }
        
        try {
        	// Tell all webservice calls to use https 
        	// http://stackoverflow.com/questions/2603691/android-httpclient-and-https
        	SchemeRegistry schemeRegistry = new SchemeRegistry();
        	schemeRegistry.register(new Scheme("https", 
        	            SSLSocketFactory.getSocketFactory(), 443));
        	HttpParams httpParams = new BasicHttpParams();
        	SingleClientConnManager mgr = new SingleClientConnManager(httpParams, schemeRegistry);
            HttpClient client = new DefaultHttpClient(mgr, httpParams);
            HttpPost post = new HttpPost(uri);
            post.setEntity(new UrlEncodedFormEntity(params));
            post.setHeader("User-Agent", useragent);
            HttpResponse response = client.execute(post);
            
            // if server returns 403, token is now invalid, user must reauthenticate
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_FORBIDDEN) {
            	tokenInvalid = true;
            }
            
            HttpEntity entity = response.getEntity();
            raw_json = EntityUtils.toString(entity);
        } catch (IOException ioe) {
            return false;
        }
        
        return true;
    }
    
}
