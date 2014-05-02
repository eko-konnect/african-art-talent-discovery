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

import com.appsng.models.Comment;
import com.appsng.models.Notification;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class NotificationAdapter extends BaseAdapter {
    
    private static Context context;
    LayoutInflater inflater;
    SharedPreferences sharedPref;
	public ImageLoader imageLoader;
	public DisplayImageOptions iconOptions;
	ArrayList<Notification> notifications = new ArrayList<Notification>();


    
	public NotificationAdapter(Context a,ArrayList<Notification> notifications) {
        context = a;
        this.notifications = notifications;

		sharedPref = PreferenceManager.getDefaultSharedPreferences(a);
        	
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		iconOptions = new DisplayImageOptions.Builder()
	    .showImageOnFail(R.drawable.ic_launcher_transparent)
	    .cacheInMemory(true)
	    .displayer(new FadeInBitmapDisplayer(500))
	    .build();
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    public int getCount() {
        return notifications.size();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
       
        ViewHolder holder = null;
        if (convertView == null) {
        	
        	
        		 convertView = inflater.inflate(R.layout.adapter_notifications_item, null);
                 holder = new ViewHolder();
                 
                 holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                 holder.photo = (ImageView) convertView.findViewById(R.id.photo);
                 holder.comment = (TextView) convertView.findViewById(R.id.comment);
                 holder.date = (TextView) convertView.findViewById(R.id.date);
                 convertView.setTag(holder);        	
        	
           
         }else{
             holder = (ViewHolder) convertView.getTag();
         }
               
		imageLoader.displayImage(notifications.get(position).getSender_avatar(),holder.avatar, iconOptions);	
		holder.comment.setText(Html.fromHtml(notifications.get(position).getNotification_message()+
				"<br>'"+notifications.get(position).getSender_comment()+"'"));
		holder.date.setText(Utilities.daysAgo(notifications.get(position).getCreated()));
		imageLoader.displayImage(notifications.get(position).getPhoto_url(),holder.photo, iconOptions);
		
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
        ImageView avatar,photo;
        TextView comment,date;
    }

 
	
	
}