package com.appsng.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.appsng.connectors.LocalDataBase;
import com.appsng.models.Portfolio;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PortfolioAdapter extends BaseAdapter {
    
    private static Context context;
    LayoutInflater inflater;
    SharedPreferences sharedPref;
	ArrayList<Portfolio> portfolio = new ArrayList<Portfolio>();
	public ImageLoader imageLoader;
	public DisplayImageOptions iconOptions;
    LocalDataBase databaseHelper;

    
	public PortfolioAdapter(Context a,ArrayList<Portfolio> portfolios) {
		context = a;
        this.portfolio.addAll(portfolios);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
        imageLoader = ImageLoader.getInstance();
		iconOptions = new DisplayImageOptions.Builder()
	    .showImageOnLoading(R.drawable.ic_launcher_transparent)
	    .showImageForEmptyUri(R.drawable.ic_launcher_transparent)
	    .displayer(new FadeInBitmapDisplayer(300))
	    .cacheInMemory(true)
	    .cacheOnDisc(true)
	    .build();

		
		try{
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		}catch(Exception f){
		}
		
		databaseHelper = new LocalDataBase(context);

    }

    public int getCount() {
        return portfolio.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
       
        ViewHolder holder = null;
        if (convertView == null) {
        	
        	
        		 convertView = inflater.inflate(R.layout.adapter_portfolio_item, null);
                 holder = new ViewHolder();
                 
                 holder.photo = (ImageView) convertView.findViewById(R.id.photo);
                 holder.name = (TextView) convertView.findViewById(R.id.name);
                 holder.position = (TextView) convertView.findViewById(R.id.position);

                 convertView.setTag(holder);        	
        	
           
         } else {
             holder = (ViewHolder) convertView.getTag();
         }
               

        holder.name.setText(portfolio.get(position).getName());  
        holder.position.setText(portfolio.get(position).getNumber_of_photos()); 
        
 		imageLoader.displayImage(portfolio.get(position).getNumber_of_photos(),holder.photo, iconOptions);	

    
    	
    	
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
        TextView name,position;
        ImageView photo;        
    }

 
	
	
}