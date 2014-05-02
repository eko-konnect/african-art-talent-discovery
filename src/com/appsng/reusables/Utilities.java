package com.appsng.reusables;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.R;
import com.appsng.models.PhotoList;





public class Utilities {
	public static String API = "http://50.118.30.151/api/";
	public static String URL = "http://50.118.30.151/api/uploadphotos";
	private static final boolean isProduction = false;
	public static String NO_INTERNET_TOAST = "You are not online! Please connect to the internet and try again.";
    public static final String DISPLAY_MESSAGE_ACTION = "com.ekoconnect.afriphoto.FETCH_COMMENT";


    // Google project id
    public static final String SENDER_ID = "836176299401";

    
    // Google Analytic id
	public static String GOOGLE_ANALYTIC = "UA-43114740-1";

    public static final String TAG = "AfriPhoto GCM";
    static boolean refresh = false;

 
    public static final String EXTRA_MESSAGE = "message";
	public static final String DATABASE = "afriphoto_db";
	public static Fragment newFragment = null;
	static boolean status = false;
    static Context contexxt;
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    public static String showoption = "";

	public static boolean LoggedIn;
	public static ArrayList<PhotoList> photolist;
	public static boolean completeStatus = false;
	public static JSONObject jsonObject = null;

	static Typeface defaultTypeface(Activity activity){
    Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/robotolight.ttf");
	return face;
	}

	public static String path(Activity activity){
		   return "/"+activity.getResources().getString(R.string.app_name)+"/";
	}
	
    public static void Vibrate(Context context,long duration){
  	  Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
  	  v.vibrate(duration);
  }
    
    public static boolean loggedIn(SharedPreferences sharedPreferences) {
		boolean status = false;
		status = sharedPreferences.getBoolean("loggedIn", false);
    	return status;
	}

    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    
	
	public static Typeface robotoLight(Context context){
	    Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/robotolight.ttf");
		return face;
	}	
	public static Typeface robotoRegular(Context context){
	    Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/robotoregular.ttf");
		return face;
	}	
	public static Typeface robotoMedium(Context context){
	    Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/robotomedium.ttf");
		return face;
	}
	public static Typeface robotoBold(Context context){
	    Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/robotobold.ttf");
		return face;
	}
    

	public static String fileExt(String url) {
	    if (url.indexOf("?")>-1) {
	        url = url.substring(0,url.indexOf("?"));
	    }
	    if (url.lastIndexOf(".") == -1) {
	        return null;
	    } else {
	        String ext = url.substring(url.lastIndexOf(".") );
	        if (ext.indexOf("%")>-1) {
	            ext = ext.substring(0,ext.indexOf("%"));
	        }
	        if (ext.indexOf("/")>-1) {
	            ext = ext.substring(0,ext.indexOf("/"));
	        }
	        return ext.toLowerCase();
	    }
	}
	

	public static ArrayList<String> StringToArray(String string,String delimeter){
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(delimeter)));
		return list;
	}
	
	public static String ArraListToString(ArrayList<String> ids){
		String[] j = new String[ids.size()];
			for(int s = 0; s < ids.size();s++){
				j[s] = ids.get(s);
			}
		return j.toString();
	}
	
	public static void showAlert(Activity context,String title,String message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
 
			alertDialogBuilder.setTitle(title);
			// set dialog message
			alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				  });
				/*
				  .setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
				*/
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		
	}
	
	
	public static boolean confirmDialog(Context context,String title,String message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		// set dialog message
		alertDialogBuilder
			.setMessage(message)
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					status = true;
					dialog.cancel();
				}
			  })
			  .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					status = false;
					dialog.cancel();
				}
			});			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();			
			return status;
	}
	

	
	public static String getAppVersion(Context c){
		PackageInfo pInfo = null;
		try {
			pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("APP_VERSION", pInfo.versionName+"");
		return pInfo.versionName;
	}
	
	
	public static Bitmap DecodeBitmapString(String str) {
		Bitmap b = null;
		try {
			byte[] ba = Base64.decode(str);
			b = BitmapFactory.decodeByteArray(ba, 0, ba.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	
	public static Bitmap retrieveContactPhoto(String contactID,Activity context) throws IOException {
		 
	        Bitmap photo = null;
	        InputStream inputStream;
	        inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
			        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

			if (inputStream != null) {
			    photo = BitmapFactory.decodeStream(inputStream);
			    return photo;

			    //ImageView imageView = (ImageView) findViewById(R.id.img_contact);
			    //imageView.setImageBitmap(photo);
			}
			//assert inputStream != null;
			//inputStream.close();
			return photo;
	 
	    }
	
	
	public static String ToHourMinuteFormat(String minute){
		int minutes = Integer.parseInt(minute);
		int hours = minutes / 60;
		int minutesInDisplay = minutes % 60;
		NumberFormat formatter = new DecimalFormat("00");
		String display = formatter.format(hours) + "h:"
		        + formatter.format(minutesInDisplay)+"m";
		return display;
	}
	
	public static String SplitArrayListToCommaSeperated(ArrayList<String> data){
		String string = "";
		for(int y = 0; y < data.size();y++){
			string = string + data.get(y) + ", ";
		}
		return removeLastChar(string);
	}
	
	public static String SplitArrayListToNewLine(ArrayList<String> data){
		String string = "";
		for(int y = 0; y < data.size();y++){
			string = string + data.get(y) + "\n";
		}
		return string;
	}

	public ArrayList<String> StringToArray(String string){
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(" , ")));
		return list;
	}

	public static String getDeviceID(Context c){
	   	return Secure.getString(c.getContentResolver(),Secure.ANDROID_ID);
	}
	    
	
	private static String removeLastChar(String str) {
		if(str != null){
			if(str.length() > 0){
		        return str.substring(0,str.length()-2);
			}else{
				return "";
			}
		}else{
			return "";
		}
    }
	
	public static void logOut(Context activity) {
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor prefEditor = p.edit();
        prefEditor.putString("userid","");
        prefEditor.putString("name", "");
        prefEditor.putString("username", "");
        prefEditor.putString("email", "");
        prefEditor.putBoolean("loggedIn", false);	
        prefEditor.commit();
	}
	
	public static boolean isLoggedIn(Context activity) {
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(activity);
		if(p.getString("userid","").toString().length() == 0) {
			return false;
		}else {
			return true;
		}

	}
	

	public static String getDate(String timeStampStr){

	     try{
	         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         Date netDate = (new Date(Long.parseLong(timeStampStr)));
	         return sdf.format(netDate);	         
	     }
	     catch(Exception ex){
	    	 Log.d("Error", ex.toString());
	         return null;	         
	     }
	 }

	
	public static boolean isOnline(Activity a) {
	    ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
    
    public static void clearNotification(Activity d) {
    	try{
    	NotificationManager notificationManager = (NotificationManager) d.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancel(0);		
    	}catch (Exception e) {

    	}
	}
	

	public static void Toaster(Context c,String text){
			Toast.makeText(c, text, Toast.LENGTH_LONG).show();
		}
	
	
	public static String EncodeBitmap(Bitmap b) {
		if (b != null) {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.JPEG, 90, bao);
			byte[] ba = bao.toByteArray();
			String ba1 = Base64.encodeBytes(ba);
			return ba1;
		} else {
			return "";
		}
	}
	
	public final static String asUpperCaseFirstChar(final String target) {

	    if ((target == null) || (target.length() == 0)) {
	        return target; // You could omit this check and simply live with an
	                       // exception if you like
	    }
	    return Character.toUpperCase(target.charAt(0))
	            + (target.length() > 1 ? target.substring(1) : "");
	}
	
	
	public static void log(String tag, String data){
		if(!Utilities.isProduction){
			Log.d(tag.toUpperCase(), data);
		}
	}
	
	

	public static void writeToSDFile(String fileData,String filename){
	    File root = android.os.Environment.getExternalStorageDirectory(); 

	    // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

	    File dir = new File (root.getAbsolutePath() + "/a_dump");
	    dir.mkdirs();
	    File file = new File(dir, filename+".txt");

	    try {
	        FileOutputStream f = new FileOutputStream(file);
	        PrintWriter pw = new PrintWriter(f);
	        pw.println(fileData);
	        pw.flush();
	        pw.close();
	        f.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        Log.i("FILE_NOT_FOUND", "******* File not found. Did you" +
	                " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }   
	}
	

    
	public static String formatTime(long millis) {  
	    String output = "00:00";  
	    long seconds = millis / 1000;  
	    long minutes = seconds / 60;  

	    seconds = seconds % 60;  
	    minutes = minutes % 60;  

	    String sec = String.valueOf(seconds);  
	    String min = String.valueOf(minutes);  

	    if (seconds < 10)  
	        sec = "0" + seconds;  
	    if (minutes < 10)  
	        min= "0" + minutes;  

	    output = min + " : " + sec;  
	    return output;
	}
	
	
	
	public static void playSound(Context context,int file) {
		int sound_id;
		SoundPool dice_sound = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
		sound_id = dice_sound.load(context,file,1);
		dice_sound.play(sound_id,1.0f,1.0f,0,0,1.0f);
	}
	
	
	public static String daysAgo(String tochangeDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = null;
        try {
            try {
				endDate = formatter.parse(tochangeDate);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (ParseException e) {
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;

        String curentDateandTime = sdf.format(new Date());
        try {
            try {
				startDate = formatter.parse(curentDateandTime);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (ParseException e) {
        }

        long diff = (startDate.getTime()) - (endDate.getTime());

        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        int hours = (int) (diff / (1000 * 60 * 60));
        int minutes = (int) (diff / (1000 * 60));
        int seconds = (int) (diff / (1000));
		
        String h = "";
        if(numOfDays == 1){
        	h = numOfDays+" day ago";
        }else if(numOfDays > 1){
        	h = numOfDays+" days ago";
        }else if(hours == 1){
            h = hours+" hour ago";
        }else if(hours > 1){
            h = hours+" hours ago";
        }else if(minutes == 1){
            h = minutes+" minute ago";
        }else if(minutes > 1){
            h = minutes+" minutes ago";
        }else if(seconds > 0){
            h = seconds+" seconds ago";
        }
		return h;
		
	}

	


	/*public static String getRealPathFromURI(Context context, Uri contentUri) {
	  Cursor cursor = null;
	  try { 
	    String[] proj = { MediaStore.Images.Media.DATA };
	    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	  } finally {
	    if (cursor != null) {
	      cursor.close();
	    }
	  }
	}
	*/
	
	
	public static File getFile(Context context,Uri data){
        File file = null;
		try{
        AssetFileDescriptor videoAsset = context.getContentResolver().openAssetFileDescriptor(data, "r");
        FileInputStream fis = videoAsset.createInputStream();
        File root=new File(Environment.getExternalStorageDirectory(),"giditrafficVideos");

          if (!root.exists()) {
              root.mkdirs();
          }

          String videoname = "giditrafic_"+System.currentTimeMillis()+".mp4";
          file=new File(root,videoname);

        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = fis.read(buf)) > 0) {
            fos.write(buf, 0, len);
        }       
        fis.close();
        fos.close();
      } 
		catch (Exception e) 
		{
       e.printStackTrace();
		}
     
      	return file;
	}
	
	
	public static String getRealPathFromURI(final Context context, final Uri uri) {
			if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	

	
	
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	
}
