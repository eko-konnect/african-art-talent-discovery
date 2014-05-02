package com.ekoconnect.afriphotos;




import static com.ekoconnect.afriphotos.GCM.SENDER_ID;
import static com.ekoconnect.afriphotos.GCM.displayMessage;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appsng.connectors.AppUtility;
import com.appsng.reusables.Utilities;
import com.google.android.gcm.GCMBaseIntentService;
 
public class GCMIntentService extends GCMBaseIntentService {
 
    private static final String TAG = "GCMIntentService";
 
    public GCMIntentService() {
        super(SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
    	try{
    	Log.i(TAG, "Device registered: regId = " + registrationId);
        GCM.displayMessage(context, "Your device registred with GCM");
        
        Log.d("DEVICEID", GCM.deviceid);
        //GCM.register(context, GCM.deviceid, registrationId); 
	        if(registrationId.equals("")) {
	        	Log.i(TAG, "Device ID not registered");
	        }else {
	        	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	        	SharedPreferences.Editor editor = preferences.edit();
	        	editor.putString("regid", registrationId);
	        	editor.commit();
	        	
	        	AppUtility appUtility = new AppUtility(context);
	        	appUtility.registerUnregisterDevice(GCM.deviceid, registrationId, "register_push");
	        }
        	
    	}catch(Exception d){
    		Utilities.log("ERROR REGISTERING",d.toString());
    	}
    }
 

    /** Method called on Receiving a new message */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        AppUtility appUtility = new AppUtility(context);
        appUtility.notiFyDevice(
        		intent.getExtras().getString("Notification"),
        		intent.getExtras().getString("sender"),
        		intent.getExtras().getString("Comment"),
        		intent.getExtras().getString("Photo")
        		);
    }
 ;

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, "GCM Error"+ errorId);
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, "Received recoverable error:" +errorId);
        return super.onRecoverableError(context, errorId);
    }
 
 @Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
 
}