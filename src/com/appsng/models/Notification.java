package com.appsng.models;


public class Notification {

	  public String id,notification_message,sender_name,sender_avatar,sender_comment,photo_id,photo_url,photo_caption,created,photo_likes;

	      public Notification(String id,String notification_message,String sender_name,String sender_avatar,String sender_comment,String photo_id,String photo_url,String photo_caption,String created,String photo_likes){

	      this.id = id;
	      this.notification_message = notification_message;
	      this.sender_name = sender_name;
	      this.sender_avatar = sender_avatar;
	      this.sender_comment = sender_comment;
	      this.photo_id = photo_id;
	      this.photo_url = photo_url;
	      this.photo_caption = photo_caption;
	      this.created = created;
	      this.photo_likes = photo_likes;

	}


	     //// getters ////

	      public String getId(){
	      return this.id;
	     }

	      public String getNotification_message(){
	      return this.notification_message;
	     }

	      public String getSender_name(){
	      return this.sender_name;
	     }

	      public String getSender_avatar(){
	      return this.sender_avatar;
	     }

	      public String getSender_comment(){
	      return this.sender_comment;
	     }

	      public String getPhoto_id(){
	      return this.photo_id;
	     }

	      public String getPhoto_url(){
	      return this.photo_url;
	     }

	      public String getPhoto_caption(){
	      return this.photo_caption;
	     }

	      public String getCreated(){
	      return this.created;
	     }

	      public String getPhoto_likes(){
	      return this.photo_likes;
	     }

	     //// setters ////

	      public void setId(String value){
	      this.id = value;
	     }

	      public void setNotification_message(String value){
	      this.notification_message = value;
	     }

	      public void setSender_name(String value){
	      this.sender_name = value;
	     }

	      public void setSender_avatar(String value){
	      this.sender_avatar = value;
	     }

	      public void setSender_comment(String value){
	      this.sender_comment = value;
	     }

	      public void setPhoto_id(String value){
	      this.photo_id = value;
	     }

	      public void setPhoto_url(String value){
	      this.photo_url = value;
	     }

	      public void setPhoto_caption(String value){
	      this.photo_caption = value;
	     }

	      public void setCreated(String value){
	      this.created = value;
	     }

	      public void setPhoto_likes(String value){
	      this.photo_likes = value;
	     }



	     public String toString(){
	      return this.id;
	      }

	}