package com.appsng.connectors;
import java.util.ArrayList;

import com.appsng.models.Comment;
import com.appsng.models.Notification;
import com.appsng.models.Photo;
import com.appsng.models.PhotoList;
import com.appsng.models.Portfolio;
import com.appsng.reusables.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class LocalDataBase extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = Utilities.DATABASE ;
		private static final int SCHEMA_VERSION=4;
	  
		public LocalDataBase(Context context) {
			super(context, DATABASE_NAME, null, SCHEMA_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) { 
			db.execSQL("CREATE TABLE portfolios (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,user_id TEXT,number_of_photos TEXT,likes TEXT,created TEXT,portfolio_id TEXT);");
			db.execSQL("CREATE TABLE my_photos (_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT,caption TEXT,url TEXT,portfolio_id TEXT,user_id TEXT,username TEXT,likes TEXT,created TEXT,num_of_comments TEXT);");
			db.execSQL("CREATE TABLE photolists (_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT,caption TEXT,url TEXT,portfolio_id TEXT,user_id TEXT,likes TEXT,created TEXT,num_of_comments TEXT,portfolio_name TEXT," +
					"portfolio_num_of_photos TEXT,user_name TEXT,user_photo_url TEXT,user_lat TEXT,user_lng TEXT);");		
			db.execSQL("CREATE TABLE comments (_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT,user_id TEXT,photo_id TEXT,name TEXT,avatar TEXT,comment TEXT,created TEXT);");	
			db.execSQL("CREATE TABLE notifications (_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT,notification_message TEXT,sender_name TEXT,sender_avatar TEXT,sender_comment TEXT,photo_id TEXT,photo_url TEXT,photo_caption TEXT,created TEXT,photo_likes TEXT);");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(this.toString(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS categories");

			onCreate(db);
		}

		
	
		 public void savePortfolio(ArrayList <Portfolio> portfolios){
	          for(int g = 0;g< portfolios.size();g++){
	          String query = " SELECT * FROM portfolios WHERE name = '"+portfolios.get(g).getName()+"'";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	               if(cursor.getCount() + 0 == 0){
	                    ContentValues cv = new ContentValues();
	                    cv.put("name", portfolios.get(g).getName());
	                    cv.put("user_id", portfolios.get(g).getUser_id());
	                    cv.put("number_of_photos", portfolios.get(g).getNumber_of_photos());
	                    cv.put("likes", portfolios.get(g).getLikes());
	                    cv.put("created", portfolios.get(g).getCreated());
	                    cv.put("portfolio_id", portfolios.get(g).getPortfolioid());
	                    getWritableDatabase().insert("portfolios","name", cv);
	               }
	                    cursor.close();
	          }
	     }


	     public ArrayList <Portfolio> getPortfolios(){
	     ArrayList <Portfolio> portfolio = new ArrayList<Portfolio>();
	          String query = " SELECT * FROM portfolios ORDER BY _id DESC";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	          while(cursor.moveToNext()){
	               portfolio.add(new Portfolio(
	                    cursor.getString(cursor.getColumnIndex("name")),
	                    cursor.getString(cursor.getColumnIndex("user_id")),
	                    cursor.getString(cursor.getColumnIndex("number_of_photos")),
	                    cursor.getString(cursor.getColumnIndex("likes")),
	                    cursor.getString(cursor.getColumnIndex("created")),
	                    cursor.getString(cursor.getColumnIndex("portfolio_id"))
	               ));
	          }
	          return portfolio;
	     }
		
	     public void saveMyPhoto(ArrayList <Photo> photos){
	          for(int g = 0;g< photos.size();g++){
	          String query = " SELECT * FROM my_photos WHERE id = '"+photos.get(g).getId()+"'";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	              ContentValues cv = new ContentValues();
	              cv.put("id", photos.get(g).getId());
	              cv.put("caption", photos.get(g).getCaption());
	              cv.put("url", photos.get(g).getUrl());
	              cv.put("portfolio_id", photos.get(g).getPortfolio_id());
	              cv.put("user_id", photos.get(g).getUser_id());
	              cv.put("username", photos.get(g).getUsername());
	              cv.put("likes", photos.get(g).getLikes());
	              cv.put("created", photos.get(g).getCreated());
	              cv.put("num_of_comments", photos.get(g).getNumberOfComment());
	               if(cursor.getCount() + 0 == 0){
	                    getWritableDatabase().insert("my_photos","id", cv);
	               }else {
	        			String[] args={photos.get(g).getId()};					
	        			getWritableDatabase().update("my_photos", cv, "id=?", args);	               
	               }
	                    cursor.close();
	          }
	     }
	     public ArrayList <Photo> getMyPhotos(){
	     ArrayList <Photo> photo = new ArrayList<Photo>();
	          String query = " SELECT * FROM my_photos";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	          while(cursor.moveToNext()){
	               photo.add(new Photo(
	                    cursor.getString(cursor.getColumnIndex("id")),
	                    cursor.getString(cursor.getColumnIndex("caption")),
	                    cursor.getString(cursor.getColumnIndex("url")),
	                    cursor.getString(cursor.getColumnIndex("portfolio_id")),
	                    cursor.getString(cursor.getColumnIndex("user_id")),
	                    cursor.getString(cursor.getColumnIndex("username")),
	                    cursor.getString(cursor.getColumnIndex("likes")),
	                    cursor.getString(cursor.getColumnIndex("created")),
	                    cursor.getString(cursor.getColumnIndex("num_of_comments"))
	    	               ));
	          }
	          return photo;
	     }
	     
	     
	     
	     public void savePhotoList(ArrayList <PhotoList> photolists){
	          for(int g = 0;g< photolists.size();g++){
	          String query = " SELECT * FROM photolists WHERE id = '"+photolists.get(g).getId()+"'";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
              ContentValues cv = new ContentValues();
              cv.put("id", photolists.get(g).getId());
              cv.put("caption", photolists.get(g).getCaption());
              cv.put("url", photolists.get(g).getUrl());
              cv.put("portfolio_id", photolists.get(g).getPortfolio_id());
              cv.put("user_id", photolists.get(g).getUser_id());
              cv.put("likes", photolists.get(g).getLikes());
              cv.put("created", photolists.get(g).getCreated());
              cv.put("num_of_comments", photolists.get(g).getNum_of_comments());
              cv.put("portfolio_name", photolists.get(g).getPortfolio_name());
              cv.put("portfolio_num_of_photos", photolists.get(g).getPortfolio_num_of_photos());
              cv.put("user_name", photolists.get(g).getUser_name());
              cv.put("user_photo_url", photolists.get(g).getUser_photo_url());
              cv.put("user_lat", photolists.get(g).getUser_lat());
              cv.put("user_lng", photolists.get(g).getUser_lng());
	               if(cursor.getCount() + 0 == 0){
	                    getWritableDatabase().insert("photolists","id", cv);
	               }else {
	        			String[] args={photolists.get(g).getId()};					
	        			getWritableDatabase().update("photolists", cv, "id=?", args);	               
	               }
	                    cursor.close();
	          }
	     }




	     public ArrayList <PhotoList> getPhotolists(){
	     ArrayList <PhotoList> photolist = new ArrayList<PhotoList>();
	          String query = " SELECT * FROM photolists ORDER BY _id ASC";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	          while(cursor.moveToNext()){
	               photolist.add(new PhotoList(
	                    cursor.getString(cursor.getColumnIndex("id")),
	                    cursor.getString(cursor.getColumnIndex("caption")),
	                    cursor.getString(cursor.getColumnIndex("url")),
	                    cursor.getString(cursor.getColumnIndex("portfolio_id")),
	                    cursor.getString(cursor.getColumnIndex("user_id")),
	                    cursor.getString(cursor.getColumnIndex("likes")),
	                    cursor.getString(cursor.getColumnIndex("created")),
	                    cursor.getString(cursor.getColumnIndex("num_of_comments")),
	                    cursor.getString(cursor.getColumnIndex("portfolio_name")),
	                    cursor.getString(cursor.getColumnIndex("portfolio_num_of_photos")),
	                    cursor.getString(cursor.getColumnIndex("user_name")),
	                    cursor.getString(cursor.getColumnIndex("user_photo_url")),
	                    cursor.getString(cursor.getColumnIndex("user_lat")),
	                    cursor.getString(cursor.getColumnIndex("user_lng"))
	               ));
	          }
	          return photolist;
	     }
	     
	     
	     
	     
	     
	     
	     
	     public void saveComment(ArrayList <Comment> comments){
	          for(int g = 0;g< comments.size();g++){
	          String query = " SELECT * FROM comments WHERE id = '"+comments.get(g).getId()+"'";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	               if(cursor.getCount() + 0 == 0){
	                    ContentValues cv = new ContentValues();
	                    cv.put("id", comments.get(g).getId());
	                    cv.put("user_id", comments.get(g).getUser_id());
	                    cv.put("photo_id", comments.get(g).getPhoto_id());
	                    cv.put("name", comments.get(g).getName());
	                    cv.put("avatar", comments.get(g).getAvatar());
	                    cv.put("comment", comments.get(g).getComment());
	                    cv.put("created", comments.get(g).getCreated());
	                    getWritableDatabase().insert("comments","id", cv);
	               }
	                    cursor.close();
	          }
	     }

	     public ArrayList <Comment> getComments(String photo_id){
	     ArrayList <Comment> comment = new ArrayList<Comment>();
	          String query = " SELECT * FROM comments WHERE photo_id ='"+photo_id+"'";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	          while(cursor.moveToNext()){
	               comment.add(new Comment(
	                    cursor.getString(cursor.getColumnIndex("id")),
	                    cursor.getString(cursor.getColumnIndex("user_id")),
	                    cursor.getString(cursor.getColumnIndex("photo_id")),
	                    cursor.getString(cursor.getColumnIndex("name")),
	                    cursor.getString(cursor.getColumnIndex("avatar")),
	                    cursor.getString(cursor.getColumnIndex("comment")),
	                    cursor.getString(cursor.getColumnIndex("created"))
	               ));
	          }
	          return comment;
	     }
	     
	     
	     
	     
	     public void saveNotification(ArrayList <Notification> notifications){
	          for(int g = 0;g< notifications.size();g++){
	                    ContentValues cv = new ContentValues();
	                    cv.put("id", notifications.get(g).getId());
	                    cv.put("notification_message", notifications.get(g).getNotification_message());
	                    cv.put("sender_name", notifications.get(g).getSender_name());
	                    cv.put("sender_avatar", notifications.get(g).getSender_avatar());
	                    cv.put("sender_comment", notifications.get(g).getSender_comment());
	                    cv.put("photo_id", notifications.get(g).getPhoto_id());
	                    cv.put("photo_url", notifications.get(g).getPhoto_url());
	                    cv.put("photo_caption", notifications.get(g).getPhoto_caption());
	                    cv.put("created", notifications.get(g).getCreated());
	                    cv.put("photo_likes", notifications.get(g).getPhoto_likes());
	                    getWritableDatabase().insert("notifications","created", cv);
	          }
	     }




	     public ArrayList <Notification> getNotifications(){
	     ArrayList <Notification> notification = new ArrayList<Notification>();
	          String query = " SELECT * FROM notifications";
	          Cursor cursor = getReadableDatabase().rawQuery(query,null);
	          while(cursor.moveToNext()){
	               notification.add(new Notification(
	                    cursor.getString(cursor.getColumnIndex("id")),
	                    cursor.getString(cursor.getColumnIndex("notification_message")),
	                    cursor.getString(cursor.getColumnIndex("sender_name")),
	                    cursor.getString(cursor.getColumnIndex("sender_avatar")),
	                    cursor.getString(cursor.getColumnIndex("sender_comment")),
	                    cursor.getString(cursor.getColumnIndex("photo_id")),
	                    cursor.getString(cursor.getColumnIndex("photo_url")),
	                    cursor.getString(cursor.getColumnIndex("photo_caption")),
	                    cursor.getString(cursor.getColumnIndex("created")),
	                    cursor.getString(cursor.getColumnIndex("photo_likes"))
	               ));
	          }
	          return notification;
	     }


	     
		/*
		 * Update a table
		 */
		public void updateTables(String table_name,String column_name,String value,String row,String id) {
			String query = "Update "+table_name+" SET "+column_name+" = '"+value+"' where "+row+" ='"+id+"'";
			Log.i("Query", query);
			getWritableDatabase().execSQL(query);
		}

		/*
		 * Empty a table
		 */
		public void EmptyTables(String table_name) {
			getWritableDatabase().execSQL("DELETE FROM  "+table_name);
		}
		
		
		/*
		 * Empty data base;
		 */
		public void flushDatabase() {
			SQLiteDatabase db= this.getWritableDatabase();
			
		}
		
}


