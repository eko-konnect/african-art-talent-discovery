package com.appsng.models;

public class Portfolio {

	  public String name,user_id,number_of_photos,likes,created,portfolio_id;

	      public Portfolio(String name,String user_id,String number_of_photos,String likes,String created,String portfolio_id){

	      this.name = name;
	      this.user_id = user_id;
	      this.number_of_photos = number_of_photos;
	      this.likes = likes;
	      this.created = created;
	      this.portfolio_id = portfolio_id;
	      
	}


	     //// getters ////

	      public String getName(){
	      return this.name;
	     }

	      public String getUser_id(){
	      return this.user_id;
	     }

	      public String getNumber_of_photos(){
	      return this.number_of_photos;
	     }

	      public String getLikes(){
	      return this.likes;
	     }

	      public String getCreated(){
	      return this.created;
	     }
	      public String getPortfolioid(){
	      return this.portfolio_id;
	     }

	     //// setters ////

	      public void setName(String value){
	      this.name = value;
	     }

	      public void setUser_id(String value){
	      this.user_id = value;
	     }
	      public void setPortfolioid(String value){
	      this.portfolio_id = value;
	     }

	      public void setNumber_of_photos(String value){
	      this.number_of_photos = value;
	     }

	      public void setLikes(String value){
	      this.likes = value;
	     }

	      public void setCreated(String value){
	      this.created = value;
	     }



	     public String toString(){
	      return this.name;
	      }

	}