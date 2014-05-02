package com.ekoconnect.afriphoto.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appsng.connectors.AppUtility;
import com.appsng.connectors.LocalDataBase;
import com.appsng.models.PhotoList;
import com.appsng.reusables.Utilities;
import com.appsng.reusables.ZoomImage;
import com.ekoconnect.afriphotos.ActivityFragment;
import com.ekoconnect.afriphotos.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class FragmentImageView extends BaseFragment {
    /** Called when the activity is first created. */
    // Menu identifiers
    static final int FIRST = Menu.FIRST;
    static final int SECOND = Menu.FIRST+1;
    static final int SHARE_ID = Menu.FIRST+2;
    static final int REPORT_ID = Menu.FIRST+3;
    Fragment newContent = null;
	Cursor cursor;
	LocalDataBase helper;
    public static final String EXTRA_TITLE = "title";
	TextView comments,username;
	ZoomImage img;
	ImageView avatar,like;
	ProgressBar progress;
	public ImageLoader imageLoader;
	public DisplayImageOptions iconOptions;
	

    public static FragmentImageView newInstance(PhotoList photo) {
    	FragmentImageView f = new FragmentImageView();
        Bundle args = new Bundle();
        args.putSerializable("data", photo);
        f.setArguments(args);
        return f;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        helper = new LocalDataBase(getActivity());
        setRetainInstance(false);
        
    	getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

		
		
		imageLoader = ImageLoader.getInstance();
		iconOptions = new DisplayImageOptions.Builder()
	    .showImageOnFail(R.drawable.ic_launcher_transparent)
	    .displayer(new FadeInBitmapDisplayer(600))
	    .showImageForEmptyUri(R.drawable.ic_launcher_transparent)
	    .cacheInMemory(true)
	    .build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getSherlockActivity()));
	
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View viewer = (View) inflater.inflate(R.layout.single_image_view, container, false);
		
    	final PhotoList photolists = (PhotoList)getArguments().getSerializable("data");

		img = (ZoomImage)viewer.findViewById(R.id.img);

		avatar = (ImageView)viewer.findViewById(R.id.avatar);
		comments = (TextView)viewer.findViewById(R.id.comments);
		username = (TextView)viewer.findViewById(R.id.username);
		like = (ImageView)viewer.findViewById(R.id.like);
		

		comments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Utilities.newFragment = new Comments();
				 Bundle args = new Bundle();
				 args.putString("photo_id", photolists.getId());
				 Utilities.newFragment.setArguments(args);
				 startActivity(new Intent(getActivity(), ActivityFragment.class));
			}
		});
		
		like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 AppUtility appUtility = new AppUtility(context);
				 appUtility.likeObject("photo", photolists.getId());
			}
		});
		
		
		updateFragment(photolists);
		return viewer;
	}
	
	
	
		private void updateFragment(PhotoList photolists) {
			//img.setScaleType(ScaleType.CENTER_CROP);
			imageLoader.displayImage(photolists.getUrl(),img, iconOptions);
			imageLoader.displayImage(photolists.getUser_photo_url(),avatar, iconOptions);
			
			username.setText(photolists.getUser_name());
			
			try {
			int co = Integer.parseInt(photolists.getNum_of_comments());
			String comment= "";
			if(co > 1) {
				comment = " comments";
			}else {
				comment = " comment";
			}
			comments.setText(photolists.getNum_of_comments()+ comment);
			}catch(Exception s) {
				
			}

	
	}

	

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     	 MenuItem populateItems = null;
         populateItems = menu.add(Menu.NONE, SHARE_ID, 0, "Share");
         populateItems.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }
      
  
	public void onDestroy() {
		   super.onDestroy();
	}	
    
	   
    public void shareOption(){
    	ImageView newimage = img;
		Bitmap icon = CovetViewToBitmap(newimage);
		Intent share = new Intent(Intent.ACTION_SEND);
		share.putExtra(Intent.EXTRA_SUBJECT,"Funny pics from "+getActivity().getString(R.string.app_name));
		share.setType("image/*");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
		try {
		    f.createNewFile();
		    FileOutputStream fo = new FileOutputStream(f);
		    fo.write(bytes.toByteArray());
		    fo.close();
		} catch (IOException e) {                       
		        e.printStackTrace();
		}
		img.setScaleType(ScaleType.FIT_XY);

		share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
		startActivity(Intent.createChooser(share, "Share Image"));
    }
    
	public Bitmap CovetViewToBitmap(ImageView userpicse){
        Bitmap bm = null;
        userpicse.buildDrawingCache();
        bm = userpicse.getDrawingCache();

        userpicse.setDrawingCacheEnabled(true);
        userpicse.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
                           MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        userpicse.layout(0, 0, 
        		userpicse.getMeasuredWidth(), userpicse.getMeasuredHeight()); 
        userpicse.buildDrawingCache(true);
        bm = Bitmap.createBitmap(userpicse.getDrawingCache());
        userpicse.setDrawingCacheEnabled(false);
         
        return bm;
	}
	 
    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	getActivity().finish();
        	return true;        
        case SHARE_ID:
        	shareOption();
        	return true;
            default:
            return false;
          }
    }
    
 
	public static Bundle createBundle( String title ) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE,title);
        return bundle;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
	@Override
	public void onResume() {
		
		super.onResume();
		Utilities.GoogleAnalytic(getActivity()).sendView("Movie Detail");

	}
	
	@Override
	public void onStart() {
	  super.onStart();

	  EasyTracker.getInstance().activityStart(getActivity()); // Add this method.
	   
	}

	@Override
	public void onStop() {
	  super.onStop();
	    EasyTracker.getInstance().activityStop(getActivity()); // Add this method.
	}
    */
        
	
}