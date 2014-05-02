package com.ekoconnect.afriphotos;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.appsng.connectors.LocalDataBase;
import com.appsng.models.Photo;
import com.appsng.models.PhotoList;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphoto.fragments.FragmentImageView;

public class ImageViewActivity extends SherlockFragmentActivity{

    MyAdapter mAdapter;
    ViewPager mPager;
	LocalDataBase helper;
	Bundle extras;
	int selectedPos,row_id;
	static Context context;	
	//static ArrayList<Photo> myphotos = new ArrayList<Photo>();
	//static ArrayList<PhotoList> photos = new ArrayList<PhotoList>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		helper = new LocalDataBase(this);
	//	myphotos = helper.getMyPhotos();
		
/*		for(int g = 0;g < Utilities.photolist.size();g++) {
			photos.add(new PhotoList(
					myphotos.get(g).getId(), 
					myphotos.get(g).getCaption(), 
					myphotos.get(g).getUrl(), 
					myphotos.get(g).getPortfolio_id(), 
					myphotos.get(g).getUser_id(), 
					myphotos.get(g).getLikes(), 
					myphotos.get(g).getCreated(), 
					"121 Comments", 
					"portfolio_name", 
					"portfolio_num_of_photos", 
					myphotos.get(g).getUsername(), 
					"user_photo_url", 
					"user_lat", 
					"user_lng"
					));
		}
*/
        extras = getIntent().getExtras();
        selectedPos = extras.getInt("id");

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(selectedPos);
        //mPager.setHapticFeedbackEnabled(true);
        
      

	}

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return Utilities.photolist.size();
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentImageView.newInstance(Utilities.photolist.get(position));
        }
        
        

    }

    
	


	
}