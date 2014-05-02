package com.appsng.connectors;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;

import com.appsng.models.Comment;
import com.appsng.models.Photo;
import com.appsng.models.PhotoList;
import com.appsng.models.Portfolio;
import com.appsng.reusables.TransparentDialog;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphoto.fragments.Home;
import com.ekoconnect.afriphotos.AwesomeActivity;
import com.ekoconnect.afriphotos.NotificationActivity;
import com.ekoconnect.afriphotos.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;




public class AppUtility {
	
	static LocalDataBase dataBase;
	static Context context;
	static SharedPreferences shePreferences;
	static String PASSWORD,EMAIL,NAME,PHOTO,USERID,PORTFOLIO_ID,CREATED,
	DEVICE_ID,REG_ID,REG_TYPE,PHOTO_ID,COMMENT,OBJECT_ID,OBJECT;
	public static String LAT,LNG;
	static String updatestatus;
    static String MESSAGE;
    static boolean STATUS = false;
	double USER_LAT,USER_LNG;
	boolean userLocation = false;
	ArrayList<Photo> photos = new ArrayList<Photo>();
	ArrayList<PhotoList> photolist = new ArrayList<PhotoList>();
	ArrayList<Comment> comments = new ArrayList<Comment>();
	ArrayList<com.appsng.models.Notification> notification = new ArrayList<com.appsng.models.Notification>();
    
	public AppUtility(Context activity) {
		AppUtility.context = activity;
		AppUtility.shePreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		dataBase = new LocalDataBase(activity);
	}



	
	
	public void registerUser(String name,String password,String email,String photo,final TransparentDialog dialog) {
		AppUtility.NAME= name;
		AppUtility.EMAIL = email;
		AppUtility.PASSWORD = password;
		AppUtility.PHOTO = photo;
		
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
					getLocation("registerUser");
					handlert.post(new Runnable() {
					public void run() {
						Utilities.Toaster(context, MESSAGE);
						if(!STATUS) {
							Utilities.showAlert((Activity) context, "Not Successful", MESSAGE);
						}else {
							((Activity) context).finish();
//							/Utilities.newFragment = new Home();
							context.startActivity(new Intent(context, AwesomeActivity.class));
						}
						dialog.cancel();
						Thread.interrupted();
					}
				});
			}
		}).start();
	}
	
	
	private void serverRegister(){
		boolean status = false;
        ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
        pp.add(new BasicNameValuePair("user_id", shePreferences.getString("userid", "")));
        pp.add(new BasicNameValuePair("email", AppUtility.EMAIL));
    	pp.add(new BasicNameValuePair("name", AppUtility.NAME));
        pp.add(new BasicNameValuePair("password", AppUtility.PASSWORD));
        pp.add(new BasicNameValuePair("photo", AppUtility.PHOTO));
        pp.add(new BasicNameValuePair("lat", AppUtility.LAT));
        pp.add(new BasicNameValuePair("lng",  AppUtility.LNG));

        try {
			updatestatus = CustomHttpClient.executeHttpPost(Utilities.API+"register", pp);
			Utilities.log("RETURNED", updatestatus);
			status = true;
		} catch (Exception e) {
			Utilities.log("FETCHING DATA ERROR",e.toString());
			status = false;
		}
		
		//Utilities.writeToSDFile(updatestatus, "afriphotos_register");

				if(status){
			        try {
						JSONObject baseObject = new JSONObject(updatestatus);
						if(baseObject.getBoolean("status")) {
					        saveUserData(baseObject.getJSONObject("data").getJSONObject("User"));
							MESSAGE = baseObject.getString("message");
							STATUS = true;
						}else {
							MESSAGE = baseObject.getString("message");
							STATUS = false;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//Utilities.log("ERROR", e.toString());
						MESSAGE = e.toString();
						e.printStackTrace();
					}

			
				}
		
		
	}


	
	
	
	public void LoginUser(final TransparentDialog dialog,String email,String password) {
		AppUtility.EMAIL = email;
		AppUtility.PASSWORD = password;
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
				calltoLoginServer();
				handlert.post(new Runnable() {
					public void run() {
						Utilities.Toaster(context, MESSAGE);
						if(!STATUS) {
							Utilities.showAlert((Activity) context, "Not Successful", MESSAGE);
						}else {
							dataBase.saveMyPhoto(photos);
							dataBase.close();
							photos.clear();
							context.startActivity(new Intent(context, AwesomeActivity.class));
							((Activity) context).finish();
							//Utilities.newFragment = new Home();
						}
						dialog.cancel();
						Thread.interrupted();
					}
				});
			}
		}).start();		
	}

	protected void calltoLoginServer() {
		 boolean status = false;
	        ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
	        pp.add(new BasicNameValuePair("email", EMAIL));
	        pp.add(new BasicNameValuePair("password", PASSWORD));
	        
			try {
				updatestatus = CustomHttpClient.executeHttpPost(Utilities.API+"login", pp);
				Utilities.log("RETURNED", updatestatus);
				status = true;
			} catch (Exception e) {
				Utilities.log("FETCHING DATA ERROR",e.toString());
				status = false;
			}
			
			//Utilities.writeToSDFile(updatestatus, "afriphotos_login");
			
			MESSAGE ="";
			if(status){
		        try {
					JSONObject baseObject = new JSONObject(updatestatus);
					if(baseObject.getBoolean("status")) {
						
						getPortfolios(baseObject.getJSONArray("portfolios"));
						getPhotos(baseObject.getJSONArray("photos"));
						saveUserData(baseObject.getJSONObject("user").getJSONObject("User"));
						MESSAGE = baseObject.getString("message");
						STATUS = true;

					}else {
						MESSAGE = baseObject.getString("message");
						STATUS = false;
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		
			
			}
		
	}

	private void getPortfolios(JSONArray jsonArray) {
		ArrayList<Portfolio> portfolios = new ArrayList<Portfolio>();
		try {
			for(int y = 0;y < jsonArray.length();y++) {
				JSONObject Object = jsonArray.getJSONObject(y);
				JSONObject portfolioObject = Object.getJSONObject("Portfolio");
				portfolios.add(new Portfolio(
						portfolioObject.getString("name"),
						portfolioObject.getString("user_id"), 
						portfolioObject.getString("number_of_photos"), 
						portfolioObject.getString("likes"),
						portfolioObject.getString("created"),
						portfolioObject.getString("id")
						));
			}
		}catch(Exception e) {
			Utilities.log("ERROR: getPortfolios", e.toString());
		}
		dataBase.savePortfolio(portfolios);
		dataBase.close();
	}
	
	private void getPhotos(JSONArray jsonArray) {
		try {
			for(int y = 0;y < jsonArray.length();y++) {
				JSONObject Object = jsonArray.getJSONObject(y);
				JSONObject photoObject = Object.getJSONObject("Photo");
				photos.add(new Photo(
						photoObject.getString("id"), 
						photoObject.getString("caption"), 
						photoObject.getString("url"),
						photoObject.getString("portfolio_id"), 
						photoObject.getString("user_id"), 
						"", 
						photoObject.getString("likes"), 
						photoObject.getString("created"),
						photoObject.getString("num_of_comments")
						));
			}
		}catch(Exception e) {
			Utilities.log("ERROR: getPortfolios", e.toString());
		}

	}

	private void saveUserData(JSONObject userData) {
		try {
			SharedPreferences.Editor prefEditor = shePreferences.edit();
	        prefEditor.putString("userid", userData.getString("id"));
	        prefEditor.putString("name", userData.getString("name"));
	        prefEditor.putString("email",  userData.getString("email"));
	        prefEditor.putString("password", AppUtility.PASSWORD);
	        prefEditor.putString("photo",  userData.getString("photo_url"));
	        prefEditor.commit();
		}catch(Exception q) {
			Utilities.Toaster(context, q.toString());
		}
	}

	public void registerUnregisterDevice(String deviceid,String regId,String regtype){
		AppUtility.DEVICE_ID = deviceid;
		AppUtility.REG_ID = regId;
		AppUtility.REG_TYPE = regtype;
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
				registerUnregisterDeviceOnserver();
				handlert.post(new Runnable() {
					public void run() {
						
						Thread.interrupted();
					}
				});
			}
		}).start();
	}
	
	public void registerUnregisterDeviceOnserver(){
		 boolean status = false;
		 ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
	     pp.add(new BasicNameValuePair("user_id",shePreferences.getString("userid", "")));
	     pp.add(new BasicNameValuePair("gcm_regid",AppUtility.REG_ID));
	     pp.add(new BasicNameValuePair("device_id",AppUtility.DEVICE_ID));
	     
			try {
				updatestatus = CustomHttpClient.executeHttpPost(Utilities.API+"registerDevice", pp);
				status = true;
				Log.d("RETURNED",updatestatus);
			} catch (Exception e) {
				Log.d("FETCHING DATA ERROR",e.toString());
				status = false;
			}
			
			if(status) {
				try {
					JSONObject jsonObject = new JSONObject(updatestatus);
						if(jsonObject.getString("status").toString().equals("true")) {
							SharedPreferences.Editor editor= shePreferences.edit();
							editor.putBoolean("registeredOnServer",true);
							editor.commit();
						}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}

	public void getLocation(String nextCall){
		 Geocoder geocoder;
	     String bestProvider;
	     List<Address> user = null;
	     LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	     Criteria criteria = new Criteria();
	     bestProvider = lm.getBestProvider(criteria, false);
	     android.location.Location location = lm.getLastKnownLocation(bestProvider);
		     if (location == null){
		    	 userLocation = false;
		      }else{
		        geocoder = new Geocoder(context);
			        try {
					        user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
					        USER_LAT=(double)user.get(0).getLatitude();
					        USER_LNG=(double)user.get(0).getLongitude();
					        AppUtility.LAT = USER_LAT+"";
					        AppUtility.LNG = USER_LNG+"";
					        Utilities.log(" DDD lat: " +LAT,",  longitude: "+LNG);
					        userLocation = true;
			        }catch (Exception e) {
				        	userLocation = false;
					        System.out.println(e.toString());
			        }
		    }
		        Utilities.log(" DDD lat: " +LAT,",  longitude: "+LNG);

		     if(nextCall.toString().equals("registerUser")) {
		    	 	serverRegister();
		     }else  if(nextCall.toString().equals("")) {
		    	 
		     }
	}

	public void getPhotos(String portfolio_id,String created,String userid,final TransparentDialog dialog, final boolean clear) {
		AppUtility.PORTFOLIO_ID= portfolio_id;
		AppUtility.CREATED = created;
		AppUtility.USERID = userid;
		
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
					getPhotoFromServer();
					handlert.post(new Runnable() {
					public void run() {
						Utilities.Toaster(context, MESSAGE);
						if(!STATUS) {
							Utilities.showAlert((Activity) context, "Not Successful", MESSAGE);
						}else {
							Utilities.Toaster(context, photolist.size()+"");
							if(clear) {
								dataBase.EmptyTables("photolists");
								dataBase.close();
							}
							dataBase.savePhotoList(photolist);
							dataBase.close();
							
							Home.fetchFromDataBase();
						}
						dialog.cancel();
						Thread.interrupted();
					}
				});
			}
		}).start();
	}
	
	private void getPhotoFromServer(){
		boolean status = false;
        ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
        pp.add(new BasicNameValuePair("user_id", AppUtility.USERID));
        pp.add(new BasicNameValuePair("portfolio_id", AppUtility.PORTFOLIO_ID));
    	pp.add(new BasicNameValuePair("created", AppUtility.CREATED));
        try {
			updatestatus = CustomHttpClient2.executeHttpPost(Utilities.API+"photos", pp);
			Utilities.log("RETURNED", updatestatus);
			status = true;
		} catch (Exception e) {
			Utilities.log("FETCHING DATA ERROR",e.toString());
			status = false;
		}
		
		//Utilities.writeToSDFile(updatestatus, "afriphotos_photos");

				if(status){
			       try {
						JSONObject baseObject = new JSONObject(updatestatus);
						if(baseObject.getBoolean("status")) {
							getPhotoData(baseObject.getJSONArray("photos"));
							STATUS = true;

						}else {
							MESSAGE = baseObject.getString("message");
							STATUS = false;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Utilities.log("FETCHING DATA ERROR",e.toString());
					}

			
				}
		
	}

	private void getPhotoData(JSONArray jsonArray) {
		try {
			for(int j = 0;j < jsonArray.length();j++) {
				JSONObject baseObject = jsonArray.getJSONObject(j);
				JSONObject photoObject = baseObject.getJSONObject("Photo");
				JSONObject portfolioObject = baseObject.getJSONObject("Portfolio");
				JSONObject userObject = baseObject.getJSONObject("User");
				
				photolist.add(new PhotoList(
						photoObject.optString("id", ""), 
						photoObject.optString("caption", ""), 
						photoObject.optString("url", ""),
						photoObject.optString("portfolio_id", ""),
						photoObject.optString("user_id", ""), 
						photoObject.optString("likes", ""), 
						photoObject.optString("created", ""),
						
						photoObject.optString("num_of_comments", ""),
						
						portfolioObject.optString("name", ""), 
						portfolioObject.optString("num_of_photos", ""), 
						
						userObject.optString("name", ""), 
						userObject.optString("photo_url", ""),  
						userObject.optString("lat", ""),  
						userObject.optString("lng", "")
						));
				
			}
			
		}catch(Exception d) {
			Utilities.log("ERROR:getPhotoData", d.toString());
		}
	}

	public void getPhotoComments(String photo_id) {
		AppUtility.PHOTO_ID = photo_id;
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
				fetchCommentFromServer();
				handlert.post(new Runnable() {
					public void run() {
						Intent intent = new Intent(Utilities.DISPLAY_MESSAGE_ACTION);

						if(STATUS == true) {
							if(comments.size() > 0) {
								dataBase.EmptyTables("comments");
								dataBase.close();
								
								dataBase.saveComment(comments);
								dataBase.close();
								
								intent.putExtra("fetch_comments",true);
							}else {
								intent.putExtra("fetch_comments",false);
							}
						}else{
							intent.putExtra("fetch_comments",false);

							Utilities.Toaster(context, MESSAGE);
						}
						context.sendBroadcast(intent);

						Thread.interrupted();
					}
				});
			}
		}).start();
	}

	public void fetchCommentFromServer(){
		 boolean status = false;
		 ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
	     pp.add(new BasicNameValuePair("photo_id",AppUtility.PHOTO_ID));
	     
			try {
				updatestatus = CustomHttpClient.executeHttpPost(Utilities.API+"getComment", pp);
				status = true;
				Log.d("RETURNED",updatestatus);
			} catch (Exception e) {
				Log.d("FETCHING DATA ERROR",e.toString());
				status = false;
			}
			
			if(status) {
				try {
					JSONObject jsonObject = new JSONObject(updatestatus);
						if(jsonObject.getString("status").toString().equals("true")) {
							getComments(jsonObject.getJSONArray("comments"));
							STATUS = true;
						}else {
							MESSAGE = jsonObject.getString("message").toString();
							STATUS = false;
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		
	}





	private void getComments(JSONArray jsonArray) {
		try {
			for(int v = 0;v < jsonArray.length();v++) {
					JSONObject Object = jsonArray.getJSONObject(v);
					JSONObject commentObject = Object.getJSONObject("Comment");
					comments.add(new Comment(
							commentObject.optString("id",""), 
							commentObject.optString("user_id",""), 
							commentObject.optString("photo_id",""), 
							commentObject.optString("name",""), 
							commentObject.optString("avatar",""), 
							commentObject.optString("comment",""), 
							commentObject.optString("created","")
							));
			}
		}catch(Exception s) {
			
		}
	}

	public void sendComment(String photo_id,String comment_text, String name, String userid, String avatar, final TransparentDialog dialog) {
		AppUtility.PHOTO_ID = photo_id;
		AppUtility.COMMENT = comment_text;
		AppUtility.NAME = name;
		AppUtility.USERID = userid;
		AppUtility.PHOTO = avatar;
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
				sendCommentTotheServer();
				handlert.post(new Runnable() {
					public void run() {
						
						Intent intent = new Intent(Utilities.DISPLAY_MESSAGE_ACTION);
						if(STATUS == true) {
							if(comments.size() > 0) {
								dataBase.EmptyTables("comments");
								dataBase.close();
								
								dataBase.saveComment(comments);
								dataBase.close();
								intent.putExtra("fetch_comments",true);
							}else {
								intent.putExtra("fetch_comments",false);
							}
						}else{
							intent.putExtra("fetch_comments",false);
							Utilities.Toaster(context, MESSAGE);
						}
						context.sendBroadcast(intent);		
						try {
						dialog.cancel();
						}catch(Exception s) {
							
						}
					Thread.interrupted();	
					}
				});
			}
		}).start();		
	}
	
	public void sendCommentTotheServer(){
		 boolean status = false;
		 ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
	     pp.add(new BasicNameValuePair("user_id",shePreferences.getString("userid", "")));
	     pp.add(new BasicNameValuePair("photo_id",AppUtility.PHOTO_ID));
	     pp.add(new BasicNameValuePair("comment",AppUtility.COMMENT));
	     pp.add(new BasicNameValuePair("name",AppUtility.NAME));
	     pp.add(new BasicNameValuePair("avatar",AppUtility.PHOTO));
	     
			try {
				updatestatus = CustomHttpClient.executeHttpPost(Utilities.API+"addcomment", pp);
				status = true;
				Log.d("RETURNED",updatestatus);
			} catch (Exception e) {
				Log.d("FETCHING DATA ERROR",e.toString());
				status = false;
			}
			
			if(status) {
				try {
					JSONObject jsonObject = new JSONObject(updatestatus);
					if(jsonObject.getString("status").toString().equals("true")) {
						getComments(jsonObject.getJSONArray("data"));
						STATUS = true;
					}else {
						MESSAGE = jsonObject.getString("message").toString();
						STATUS = false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
	
	
	
	public void createPortfolio(String name, String userid,final TransparentDialog dialog) {
		AppUtility.NAME = name;
		AppUtility.USERID = userid;
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
				sendcreatePortfolio();
				handlert.post(new Runnable() {
					public void run() {
						Utilities.Toaster(context, MESSAGE);
						if(STATUS == true) {
							com.ekoconnect.afriphotos.Portfolio.loadDataFromDb();
						}else{
							Utilities.Toaster(context, MESSAGE);
						}
						try {
						dialog.cancel();
						}catch(Exception s) {
							
						}
					Thread.interrupted();	
					}
				});
			}
		}).start();		
	}
	
	public void sendcreatePortfolio(){
		 boolean status = false;
		 ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
	     pp.add(new BasicNameValuePair("user_id",shePreferences.getString("userid", "")));
	     pp.add(new BasicNameValuePair("name",AppUtility.NAME));
	     
			try {
				updatestatus = CustomHttpClient.executeHttpPost(Utilities.API+"createPortfolio", pp);
				status = true;
				Log.d("RETURNED",updatestatus);
			} catch (Exception e) {
				Log.d("FETCHING DATA ERROR",e.toString());
				status = false;
			}
			
			if(status) {
				try {
					JSONObject jsonObject = new JSONObject(updatestatus);
					if(jsonObject.getString("status").toString().equals("true")) {
						getPortfolios(jsonObject.getJSONArray("data"));
						STATUS = true;
					}else {
						MESSAGE = jsonObject.getString("message").toString();
						STATUS = false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
	
	
	public void likeObject(String object,String object_id) {
		AppUtility.OBJECT = object;
		AppUtility.OBJECT_ID = object_id;
		final Handler handlert = new Handler();
		new Thread(new Runnable() {
			public void run() {
				sendlikeObject();
				handlert.post(new Runnable() {
					public void run() {
						Utilities.Toaster(context, MESSAGE);
					Thread.interrupted();	
					}
				});
			}
		}).start();		
	}
	
	public void sendlikeObject(){
		 boolean status = false;
		 ArrayList<NameValuePair> pp = new ArrayList<NameValuePair>();
	     pp.add(new BasicNameValuePair("user_id",shePreferences.getString("userid", "")));
	     pp.add(new BasicNameValuePair("object",AppUtility.OBJECT));
	     pp.add(new BasicNameValuePair("object_id",AppUtility.OBJECT_ID));
	     
			try {
				updatestatus = CustomHttpClient.executeHttpPost(Utilities.API+"likeObject", pp);
				status = true;
				Log.d("RETURNED",updatestatus);
			} catch (Exception e) {
				Log.d("FETCHING DATA ERROR",e.toString());
				status = false;
			}
			
			if(status) {
				try {
					JSONObject jsonObject = new JSONObject(updatestatus);
					if(jsonObject.getString("status").toString().equals("true")) {
						STATUS = true;
					}else {
						STATUS = false;
					}
					MESSAGE = jsonObject.getString("message").toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
	
	
	
	
	public void notiFyDevice(String notifications, String sender, String comments, String photo) {
		String sender_name= "",sender_avatar = "";
		JSONObject photoObject = null ;
		JSONObject commentObject = null;
		String comment = "",photo_id = "",photo_url = "",photo_caption ="",
				created = "",photo_likes = "",photo_num_of_comment = "";

		try {
			JSONObject senderObject = new JSONObject(sender);
			sender_name = senderObject.getJSONObject("User").getString("name");
			sender_avatar = senderObject.getJSONObject("User").getString("photo_url");
		}catch(Exception p) {
			sender_name = sender;
		}
		
		try {
			commentObject = new JSONObject(comments);
			comment = commentObject.getString("comment");
			created = commentObject.getString("created");
		}catch(Exception p) {
			
		}
		
		try {
			photoObject = new JSONObject(photo);
			photo_id = photoObject.getJSONObject("Photo").optString("id","");
			photo_url = photoObject.getJSONObject("Photo").optString("url","");
			photo_caption = photoObject.getJSONObject("Photo").optString("caption","");
			photo_num_of_comment = photoObject.getJSONObject("Photo").optString("num_of_comments","");
			photo_likes = photoObject.getJSONObject("Photo").optString("likes","");
		}catch(Exception p) {

		}
		
		try {
		
		notification.add(new com.appsng.models.Notification(
				"0",
				notifications,
				sender_name,
				sender_avatar,
				comment,
				photo_id,
				photo_url,
				photo_caption,
				created,
				photo_likes
				));
		}catch(Exception c) {
			Utilities.log("", c.toString());
		}
		/*
		Utilities.log("SENDER", sender);
		Utilities.log("COMMENT", comments);
		Utilities.log("PHOTO", photo);*/
		
		dataBase.saveNotification(notification);
		dataBase.close();
		
		dataBase.updateTables("my_photos", "num_of_comments", photo_num_of_comment, "id", photo_id);
		dataBase.close();	
		dataBase.updateTables("my_photos", "likes", photo_likes, "id", photo_id);
		dataBase.close();	
		
		dataBase.updateTables("photolists", "num_of_comments", photo_num_of_comment, "id", photo_id);
		dataBase.close();
		dataBase.updateTables("photolists", "likes", photo_likes, "id", photo_id);
		dataBase.close();	
		
		
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.drawable.thumbnail);
        contentView.setTextViewText(R.id.title, notifications);
        
        
		ImageLoader imageLoader;
		DisplayImageOptions iconOptions;
		imageLoader = ImageLoader.getInstance();
		iconOptions = new DisplayImageOptions.Builder()
	    .showStubImage(R.drawable.ic_launcher_transparent)
	    .showImageOnFail(R.drawable.ic_launcher_transparent)
	    .displayer(new FadeInBitmapDisplayer(500))
	    .cacheInMemory(true)
	    .cacheOnDisc()
	    .build();
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		
    /*    Notification noti = new Notification.BigPictureStyle(
        	      new Notification.Builder(context)
        	         .setContentTitle("New photo from " + sender.toString())
        	         .setContentText(subject)
        	         .setSmallIcon(R.drawable.ic_launcher)
        	         .setLargeIcon(aBitmap))
        	      .bigPicture(aBigBitmap)
        	      .build();
*/

        
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
	    .setSmallIcon(R.drawable.ic_launcher)
	    .setContentTitle(sender_name)
	    .setLargeIcon(imageLoader.loadImageSync(photo_url, iconOptions))
	    .setDefaults(Notification.DEFAULT_ALL)
	    .setContentText(Html.fromHtml(notifications));
		Intent notificationIntent = new Intent(context, NotificationActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack
		stackBuilder.addParentStack(NotificationActivity.class);
		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntent(notificationIntent);
		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

	    
		NotificationManager mNotificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
		}


	
	
	}
