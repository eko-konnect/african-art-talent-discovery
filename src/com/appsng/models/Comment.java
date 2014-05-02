package com.appsng.models;
public class Comment {

	  public String id,user_id,photo_id,name,avatar,comment,created;

	      public Comment(String id,String user_id,String photo_id,String name,String avatar,String comment,String created){

	      this.id = id;
	      this.user_id = user_id;
	      this.photo_id = photo_id;
	      this.name = name;
	      this.avatar = avatar;
	      this.comment = comment;
	      this.created = created;

	}


	     //// getters ////

	      public String getId(){
	      return this.id;
	     }

	      public String getUser_id(){
	      return this.user_id;
	     }

	      public String getPhoto_id(){
	      return this.photo_id;
	     }

	      public String getName(){
	      return this.name;
	     }

	      public String getAvatar(){
	      return this.avatar;
	     }

	      public String getComment(){
	      return this.comment;
	     }

	      public String getCreated(){
	      return this.created;
	     }

	     //// setters ////

	      public void setId(String value){
	      this.id = value;
	     }

	      public void setUser_id(String value){
	      this.user_id = value;
	     }

	      public void setPhoto_id(String value){
	      this.photo_id = value;
	     }

	      public void setName(String value){
	      this.name = value;
	     }

	      public void setAvatar(String value){
	      this.avatar = value;
	     }

	      public void setComment(String value){
	      this.comment = value;
	     }

	      public void setCreated(String value){
	      this.created = value;
	     }



	     public String toString(){
	      return this.id;
	      }

	}