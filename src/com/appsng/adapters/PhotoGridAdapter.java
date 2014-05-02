package com.appsng.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.appsng.models.Photo;
import com.ekoconnect.afriphotos.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PhotoGridAdapter extends BaseAdapter {
    
    private static Activity activity;
    LayoutInflater inflater;
    SharedPreferences sharedPref;
	public ImageLoader imageLoader;
	public DisplayImageOptions iconOptions;
	ArrayList<Photo> photos = new ArrayList<Photo>();


    
	public PhotoGridAdapter(Activity a,ArrayList<Photo> photos) {
        activity = a;
        this.photos.addAll(photos);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(a);
        	
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		iconOptions = new DisplayImageOptions.Builder()
	    .showStubImage(R.drawable.ic_launcher_transparent)
	    .showImageOnFail(R.drawable.ic_launcher_transparent)
	    .cacheInMemory()
	    .cacheOnDisc()
	    .build();
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
    }

    public int getCount() {
        return photos.size();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
       
        ViewHolder holder = null;
        if (convertView == null) {
        	
        	
        		 convertView = inflater.inflate(R.layout.adapter_grid_item, null);
                 holder = new ViewHolder();
                 
                 holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                 convertView.setTag(holder);        	
        	
           
         }else{
             holder = (ViewHolder) convertView.getTag();
         }
               
  
		imageLoader.displayImage(photos.get(position).getUrl(),holder.icon, iconOptions);	

    	
    	
        return convertView;
    }
    
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	

    class ViewHolder {
        ImageView icon;
    }

 
	
	
}