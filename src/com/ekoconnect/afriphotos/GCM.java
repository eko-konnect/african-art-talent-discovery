package com.ekoconnect.afriphotos;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import com.appsng.connectors.AppUtility;
import com.appsng.reusables.Utilities;
import com.appsng.reusables.WakeLocker;
import com.google.android.gcm.GCMRegistrar;


public class GCM {
	Activity activity;
	public static AsyncTask<Void, Void, Void> mRegisterTask;
    static String deviceid;

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    
    // Url to your server here OR file
    static final String SERVER_URL = Utilities.API;

    // Our Google project id
    public static final String SENDER_ID = "579816852696";

    /** Tag used on log messages.*/
    static final String TAG = "EkoConnect GCM";
    static final String DISPLAY_MESSAGE_ACTION = "com.ekoconnect.afriphotos.DISPLAY_MESSAGE";
    static final String EXTRA_MESSAGE = "message";
 
    
	 public GCM(Activity act){
		 activity = act;
		 deviceid = Utilities.getDeviceID(act);
	 }

	 
	 public void startGCM(){
	        GCMRegistrar.checkDevice(activity);
	        GCMRegistrar.checkManifest(activity);
	        
	        activity.registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
	        // Get GCM registration id
	        final String regId = GCMRegistrar.getRegistrationId(activity);
	        if (regId.equals("")) {
	        	//Utilities.Toaster(activity, "Reg ID is not registered yet");
	            GCMRegistrar.register(activity, SENDER_ID);
	        } else {
	            // Device is already registered on GCM
	            if (GCMRegistrar.isRegisteredOnServer(activity)) {
	                // Skips registration.             

	            } else {
	            	AppUtility appUtility = new AppUtility(activity);
	            	appUtility.registerUnregisterDevice(deviceid, regId, "register_push");
	            }
	        }
	        		
	 }
	    
	 public void disableGCM(){
	      final Context context = activity;
	      final String regId = GCMRegistrar.getRegistrationId(activity);
	      mRegisterTask = new AsyncTask<Void, Void, Void>() {
	            @Override
	          protected Void doInBackground(Void... params) {
	               // UnRegister on our server
	               // On server creates a new user
	            	Log.d("Unregistering ", regId);
	                unregister(context,regId);
	                return null;
	            }

	            @Override
	            protected void onPostExecute(Void result) {
	                mRegisterTask = null;
	            }

	        };
	        mRegisterTask.execute(null, null, null);
	    }
	    
	    /**
	     * Receiving push messages
	     * */
	   public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
	            // Waking up mobile if it is sleeping
	            WakeLocker.acquire(activity);
	             
	            try{
	            //activity.finish();
	            }catch (Exception e) {
					// TODO: handle exception
				}
	            

	            
	            try{
	            if(newMessage.toString().equals("new data")){

	            }
	            }catch(Exception d){
	            	
	            }
	            WakeLocker.release();
	        }
	    };

	    
	    
	    /*
	     * Server utility
	     */
	    
	    public static void register(final Context context, String deviceid, final String regId) {
	      /*  Log.i(TAG, "registering device (regId = " + regId + ")");
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("regid", regId);
	        params.put("deviceid", deviceid);
	         
	        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
	        
	         *  Once GCM returns a registration id, we need to register on our server
	         *  As the server might be down, we will retry it a couple times.
	         *  
	         
	        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
	            Log.d(TAG, "Attempt #" + i + " to register");
	            try {
	                displayMessage(context, "Registering on server " + i + " of "+ MAX_ATTEMPTS);
	                post(serverUrlRegister, params);
	                GCMRegistrar.setRegisteredOnServer(context, true);
	                String message = "Registration successful";
	                displayMessage(context, message);
	                return;
	            } catch (IOException e) {
	                Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
	                if (i == MAX_ATTEMPTS) {
	                    break;
	                }
	                try {
	                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
	                    Thread.sleep(backoff);
	                } catch (InterruptedException e1) {
	                    // Activity finished before we complete - exit.
	                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
	                    Thread.currentThread().interrupt();
	                    return;
	                }
	                // increase backoff exponentially
	                backoff *= 2;
	            }
	        }
	        String message = "Registring on server failed "+ MAX_ATTEMPTS;
	        displayMessage(context, message);*/
	    }
	 
	    
	    /**
	     * Unregister this account/device pair within the server.
	     */
	    public static void unregister(final Context context, final String regId) {
	     /*   Log.i(TAG, "unregistering device (regId = " + regId + ")");
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("regId", regId);
	        try {
	            post(serverUrlUnregister, params);
	            GCMRegistrar.setRegisteredOnServer(context, false);
	            String message = "Unregistered from the server";
	            displayMessage(context, message);
	        } catch (IOException e) {
	            // At this point the device is unregistered from GCM, but still
	            // registered in the server.
	            // We could try to unregister again, but it is not necessary:
	            // if the server tries to send a message to the device, it will get
	            // a "NotRegistered" error message and should unregister the device.
	            String message = "Unregistered";
	            displayMessage(context, message);
	        }*/
	    }

	    /**
	     * Sending POST request to the server with parameters
	     */
	    private static void post(String endpoint, Map<String, String> params)
	            throws IOException {   
	         
	        URL url;
	        try {
	            url = new URL(endpoint);
	        } catch (MalformedURLException e) {
	            throw new IllegalArgumentException("invalid url: " + endpoint);
	        }
	        StringBuilder bodyBuilder = new StringBuilder();
	        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	        // constructs the POST body using the parameters
	        while (iterator.hasNext()) {
	            Entry<String, String> param = iterator.next();
	            bodyBuilder.append(param.getKey()).append('=')
	                    .append(param.getValue());
	            if (iterator.hasNext()) {
	                bodyBuilder.append('&');
	            }
	        }
	        String body = bodyBuilder.toString();
	        Log.v(TAG, "Posting '" + body + "' to " + url);
	        byte[] bytes = body.getBytes();
	        HttpURLConnection conn = null;
	        try {
	            Log.e("URL", "> " + url);
	            conn = (HttpURLConnection) url.openConnection();
	            conn.setDoOutput(true);
	            conn.setUseCaches(false);
	            conn.setFixedLengthStreamingMode(bytes.length);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
	            // post the request
	            OutputStream out = conn.getOutputStream();
	            out.write(bytes);
	            out.close();
	            // handle the response
	            int status = conn.getResponseCode();
	            if (status != 200) {
	              throw new IOException("Post failed with error code " + status);
	            }
	        } finally {
	            if (conn != null) {
	                conn.disconnect();
	            }
	        }
	      }
	    
	    
	    
	    

	    /**
	     * Notifies UI to display a message.
	     * <p>
	     * This method is defined in the common helper because it's used both by
	     * the UI and the background service.
	     *
	     * @param context application's context.
	     * @param message message to be displayed.
	     */
	    static void displayMessage(Context context, String message) {
	        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
	        intent.putExtra(EXTRA_MESSAGE, message);
	        context.sendBroadcast(intent);
	    }
	    
	    
	    
	    

}
