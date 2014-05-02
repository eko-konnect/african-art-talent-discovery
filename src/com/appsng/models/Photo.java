package com.appsng.models;

public class Photo {

	  public String id,caption,url,portfolio_id,user_id,username,likes,created,num_of_comments;

	      public Photo(String id,String caption,String url,String portfolio_id,String user_id,String username,
	    		  String likes,String created,String num_of_comments){

	      this.id = id;
	      this.caption = caption;
	      this.url = url;
	      this.portfolio_id = portfolio_id;
	      this.user_id = user_id;
	      this.username = username;
	      this.likes = likes;
	      this.created = created;
	      this.num_of_comments = num_of_comments;

	}


	     //// getters ////

	      public String getId(){
	      return this.id;
	     }

	      public String getCaption(){
	      return this.caption;
	     }

	      public String getUrl(){
	      return this.url;
	     }

	      public String getNumberOfComment(){
	      return this.num_of_comments;
	     }

	      public String getPortfolio_id(){
	      return this.portfolio_id;
	     }

	      public String getUser_id(){
	      return this.user_id;
	     }

	      public String getUsername(){
	      return this.username;
	     }

	      public String getLikes(){
	      return this.likes;
	     }

	      public String getCreated(){
	      return this.created;
	     }

	     //// setters ////

	      public void setId(String value){
	      this.id = value;
	     }

	      public void setCaption(String value){
	      this.caption = value;
	     }

	      public void setUrl(String value){
	      this.url = value;
	     }

	      public void setPortfolio_id(String value){
	      this.portfolio_id = value;
	     }

	      public void setUser_id(String value){
	      this.user_id = value;
	     }

	      public void setNumberOfComment(String value){
	      this.num_of_comments = value;
	     }

	      public void setUsername(String value){
	      this.username = value;
	     }

	      public void setLikes(String value){
	      this.likes = value;
	     }

	      public void setCreated(String value){
	      this.created = value;
	     }



	     public String toString(){
	      return this.id;
	      }

	}

