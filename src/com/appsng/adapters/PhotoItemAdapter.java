package com.appsng.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsng.models.PhotoList;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PhotoItemAdapter extends BaseAdapter {
    
    private static Context activity;
    LayoutInflater inflater;
    SharedPreferences sharedPref;
	public ImageLoader imageLoader;
	public DisplayImageOptions iconOptions;
	ArrayList<PhotoList> photos = new ArrayList<PhotoList>();
	


    
	public PhotoItemAdapter(Context a,ArrayList<PhotoList> photos) {
        activity = a;
        this.photos = photos;

		sharedPref = PreferenceManager.getDefaultSharedPreferences(a);
        	
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		iconOptions = new DisplayImageOptions.Builder()
	    .showStubImage(R.drawable.ic_launcher_transparent)
	    .showImageOnFail(R.drawable.ic_launcher_transparent)
	    .displayer(new FadeInBitmapDisplayer(500))
	    .cacheInMemory(true)
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
        	
        	
        		 convertView = inflater.inflate(R.layout.adapter_photo_item, null);
                 holder = new ViewHolder();
                 
                 holder.caption = (TextView) convertView.findViewById(R.id.caption);
                 holder.name = (TextView) convertView.findViewById(R.id.name);
                 holder.likes = (TextView) convertView.findViewById(R.id.likes);
                 holder.comments = (TextView) convertView.findViewById(R.id.comments);
                 holder.userimage = (ImageView) convertView.findViewById(R.id.user_image);
                 holder.image = (ImageView) convertView.findViewById(R.id.image);
                 convertView.setTag(holder);        	
        	
           
         }else{
             holder = (ViewHolder) convertView.getTag();
         }
               

        holder.caption.setTypeface(Utilities.robotoLight(activity));
        holder.name.setTypeface(Utilities.robotoLight(activity));
        holder.likes.setTypeface(Utilities.robotoLight(activity));
        holder.comments.setTypeface(Utilities.robotoLight(activity));
        holder.name.setText(Html.fromHtml(photos.get(position).getUser_name()));  
        holder.likes.setText(Html.fromHtml(photos.get(position).getLikes()));  
        holder.caption.setText(Html.fromHtml(photos.get(position).getCaption()));  

        int co = Integer.parseInt(photos.get(position).getNum_of_comments());
		String comment= "";
		if(co > 1) {
			comment = " comments";
		}else {
			comment = " comment";
		}
        holder.comments.setText(Html.fromHtml(photos.get(position).getNum_of_comments()+comment));  

		imageLoader.displayImage(photos.get(position).getUser_photo_url(),holder.userimage, iconOptions);	 		 		   
		imageLoader.displayImage(photos.get(position).getUrl(),holder.image, iconOptions);	 		 		   

    	
    	
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
        TextView caption,name,likes,comments;
        ImageView image,userimage;
    }

 
	
	
}