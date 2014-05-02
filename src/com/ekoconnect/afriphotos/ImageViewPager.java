package com.ekoconnect.afriphotos;

import java.util.ArrayList;
import java.util.List;

import com.appsng.adapters.ImagePagerAdapter;
import com.appsng.connectors.LocalDataBase;
import com.appsng.models.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

public class ImageViewPager extends Activity {
	// Declare Variable
	int position;
	LocalDataBase localDataBase;
	ArrayList<Photo> photos = new ArrayList<Photo>();
	public ImageLoader imageLoader;
	public DisplayImageOptions iconOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set title for the ViewPager
		setTitle("ViewPager");
		// Get the view from view_pager.xml
		setContentView(R.layout.view_pager);

		
		// Initiallize Universal image loader
		imageLoader = ImageLoader.getInstance();
		iconOptions = new DisplayImageOptions.Builder()
	    .showStubImage(R.drawable.ic_launcher)
	    .delayBeforeLoading(3)
	    .showImageOnFail(R.drawable.thumbnail)
	    .displayer(new FadeInBitmapDisplayer(800))
	    .cacheInMemory()
	    .cacheOnDisc()
	    .build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		
		// Retrieve data from MainActivity on item click event
		Intent p = getIntent();
		position = p.getExtras().getInt("id");
		
        localDataBase = new LocalDataBase(this);
		photos = localDataBase.getMyPhotos();

		List<ImageView> images = new ArrayList<ImageView>();
		for (int i = 0; i < photos.size(); i++) {
			ImageView imageView = new ImageView(this);
			imageLoader.displayImage(photos.get(position).getUrl(),imageView, iconOptions);	 		 		   
			//imageView.setImageResource(imageAdapter.mThumbIds[i]);
			imageView.setScaleType(ImageView.ScaleType.CENTER);
			images.add(imageView);
		}
		

		// Set the images into ViewPager
		ImagePagerAdapter pageradapter = new ImagePagerAdapter(images);
		ViewPager viewpager = (ViewPager) findViewById(R.id.pager);
		viewpager.setAdapter(pageradapter);
		viewpager.setCurrentItem(position);
	}
}