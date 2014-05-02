package com.ekoconnect.afriphotos;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.appsng.adapters.PhotoGridAdapter;
import com.appsng.connectors.LocalDataBase;
import com.appsng.models.Photo;
import com.appsng.models.PhotoList;
import com.appsng.reusables.Utilities;

public class MyPhotos extends Activity {
	
	GridView grid_menu;
	ArrayList<Photo> photos = new ArrayList<Photo>();
	LocalDataBase localDataBase;
	static ArrayList<PhotoList> photolist = new ArrayList<PhotoList>();

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myphotos);        
        grid_menu = (GridView)findViewById(R.id.gridView1);
        
        localDataBase = new LocalDataBase(this);
		setTitle("My Photos");

        loadDataFromDb();
    }

	private void loadDataFromDb() {
		photos = localDataBase.getMyPhotos();
        setUpGridView(photos);
	}

	private void setUpGridView(ArrayList<Photo> photos2) {
		PhotoGridAdapter madapter = new PhotoGridAdapter(this, photos2);
		grid_menu.setAdapter(madapter);
		
		
		setClickListener();
	}

	private void setClickListener() {
        // Listening to GridView item click
		for(int g = 0;g < photos.size();g++) {
			photolist.add(new PhotoList(
					photos.get(g).getId(), 
					photos.get(g).getCaption(), 
					photos.get(g).getUrl(), 
					photos.get(g).getPortfolio_id(), 
					photos.get(g).getUser_id(), 
					photos.get(g).getLikes(), 
					photos.get(g).getCreated(), 
					"121 Comments", 
					"portfolio_name", 
					"portfolio_num_of_photos", 
					photos.get(g).getUsername(), 
					"user_photo_url", 
					"user_lat", 
					"user_lng"
					));
		}

		grid_menu.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	// Launch ImageViewPager.java on selecting GridView Item
            	Intent i = new Intent(getApplicationContext(), ImageViewActivity.class);
            	// Show a simple toast message for the item position
            	//Toast.makeText(MyPhotos.this, "" + position, Toast.LENGTH_SHORT).show();
            	// Send the click position to ImageViewPager.java using intent
            	i.putExtra("id", position);
            	Utilities.photolist = new ArrayList<PhotoList>();
            	Utilities.photolist.clear();
            	Utilities.photolist = photolist;
            	//i.putStringArrayListExtra("photos", photos);
                // Start ImageViewPager
                startActivity(i);
                overridePendingTransition(R.anim.slide_out_left_, R.anim.slide_in_left_);

            }
        });		
	}
   
}
