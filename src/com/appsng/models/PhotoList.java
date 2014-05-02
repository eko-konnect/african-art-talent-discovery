package com.appsng.models;

import java.io.Serializable;


public class PhotoList implements Serializable {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id,caption,url,portfolio_id,user_id,likes,created,num_of_comments,portfolio_name,portfolio_num_of_photos,user_name,user_photo_url,user_lat,user_lng;

	      public PhotoList(String id,String caption,String url,String portfolio_id,String user_id,String likes,String created,String num_of_comments,String portfolio_name,String portfolio_num_of_photos,String user_name,String user_photo_url,String user_lat,String user_lng){

	      this.id = id;
	      this.caption = caption;
	      this.url = url;
	      this.portfolio_id = portfolio_id;
	      this.user_id = user_id;
	      this.likes = likes;
	      this.created = created;
	      this.num_of_comments = num_of_comments;
	      this.portfolio_name = portfolio_name;
	      this.portfolio_num_of_photos = portfolio_num_of_photos;
	      this.user_name = user_name;
	      this.user_photo_url = user_photo_url;
	      this.user_lat = user_lat;
	      this.user_lng = user_lng;

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

	      public String getPortfolio_id(){
	      return this.portfolio_id;
	     }

	      public String getUser_id(){
	      return this.user_id;
	     }

	      public String getLikes(){
	      return this.likes;
	     }

	      public String getCreated(){
	      return this.created;
	     }

	      public String getNum_of_comments(){
	      return this.num_of_comments;
	     }

	      public String getPortfolio_name(){
	      return this.portfolio_name;
	     }

	      public String getPortfolio_num_of_photos(){
	      return this.portfolio_num_of_photos;
	     }

	      public String getUser_name(){
	      return this.user_name;
	     }

	      public String getUser_photo_url(){
	      return this.user_photo_url;
	     }

	      public String getUser_lat(){
	      return this.user_lat;
	     }

	      public String getUser_lng(){
	      return this.user_lng;
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

	      public void setLikes(String value){
	      this.likes = value;
	     }

	      public void setCreated(String value){
	      this.created = value;
	     }

	      public void setNum_of_comments(String value){
	      this.num_of_comments = value;
	     }

	      public void setPortfolio_name(String value){
	      this.portfolio_name = value;
	     }

	      public void setPortfolio_num_of_photos(String value){
	      this.portfolio_num_of_photos = value;
	     }

	      public void setUser_name(String value){
	      this.user_name = value;
	     }

	      public void setUser_photo_url(String value){
	      this.user_photo_url = value;
	     }

	      public void setUser_lat(String value){
	      this.user_lat = value;
	     }

	      public void setUser_lng(String value){
	      this.user_lng = value;
	     }



	     public String toString(){
	      return this.id;
	      }

	}