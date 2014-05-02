package com.ekoconnect.afriphoto.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appsng.adapters.PhotoItemAdapter;
import com.appsng.connectors.AppUtility;
import com.appsng.models.PhotoList;
import com.appsng.reusables.TransparentDialog;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.ActivityFragment;
import com.ekoconnect.afriphotos.GCM;
import com.ekoconnect.afriphotos.ImageViewActivity;
import com.ekoconnect.afriphotos.MyPhotos;
import com.ekoconnect.afriphotos.R;
import com.ekoconnect.afriphotos.RefreshListView;
import com.google.android.gcm.GCMRegistrar;

public class Home extends BaseFragment{
	GCM GcmService;
	static ArrayList<PhotoList> photolists = new ArrayList<PhotoList>();
	static PhotoItemAdapter photoItemAdapter; 
	private static RefreshListView photo_list;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		checkIfRegisteredForPush();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		View viewer = (View) inflater.inflate(R.layout.fragment_home, container, false);
		photo_list = (RefreshListView)viewer.findViewById(R.id.photo_list);
		((Activity) context).setProgressBarIndeterminateVisibility(false);

	
		//fetchDataOnline(false);
		if(localDataBase.getPhotolists().size() == 0) {
			fetchDataOnline(true);
		}else {
			fetchFromDataBase();
		}
		
		
		return viewer;
	}
	
	private void checkIfRegisteredForPush() {
        final String regId = GCMRegistrar.getRegistrationId(context);
        //Utilities.Toaster(context, regId+":::id");
        if (regId.equals("")) {
    		GcmService = new GCM(getActivity());
            GcmService.startGCM();	
        }	
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fetchFromDataBase();
	}
	
	
	private static void fetchDataOnline(boolean clear) {
		((Activity) context).setProgressBarIndeterminateVisibility(false);

		TransparentDialog dialog = new TransparentDialog(context, R.drawable.ajax_loader);
		AppUtility appUtility = new AppUtility(context);
		appUtility.getPhotos("", "", "", dialog,clear);
		dialog.show();
	}

	public static void fetchFromDataBase() {
		photolists.clear();
		photolists.addAll(localDataBase.getPhotolists());
		photoItemAdapter = new PhotoItemAdapter(context, photolists);
		photo_list.setAdapter(photoItemAdapter);
		
		setOtherEvents();
	}

	private static void setOtherEvents() {
		photo_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				// Launch ImageViewPager.java on selecting GridView Item
            	Intent i = new Intent(context, ImageViewActivity.class);
            	// Show a simple toast message for the item position
            	// Send the click position to ImageViewPager.java using intent
            	i.putExtra("id", position);
            	Utilities.photolist = new ArrayList<PhotoList>();
            	Utilities.photolist.clear();
            	Utilities.photolist = photolists;
            	//i.putStringArrayListExtra("photos", photos);
                // Start ImageViewPager
                context.startActivity(i);		
                ((Activity) context).overridePendingTransition(R.anim.slide_out_left_, R.anim.slide_in_left_);

			}
		});
		
		photo_list.setRefreshListener(new com.ekoconnect.afriphotos.RefreshListView.OnRefreshListener() {
			public void onRefresh(com.ekoconnect.afriphotos.RefreshListView listView) {
				Utilities.playSound(context, R.raw.woosh);
	 			refreshData();
			}
		});
		photo_list.finishRefreshing();				
	}

	protected static void refreshData() {
		((Activity) context).setProgressBarIndeterminateVisibility(true);
		fetchDataOnline(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
    	menu.add(0, 102, Menu.NONE, "Refresh")
        .setIcon(R.drawable.refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);      	
    	menu.add(0, 100, Menu.NONE, "Logout")
        .setIcon(R.drawable.ic_launcher)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);      	
    	menu.add(0, 110, Menu.NONE, "Profile")
        .setIcon(R.drawable.ic_launcher)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);  
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case 102:
        	refreshData();
        	return true;
        case 100:
        	Utilities.logOut(context);
        	Utilities.newFragment = new Index();
        	startActivity(new Intent(context, ActivityFragment.class));
        	getActivity().finish();
        	return true;
            default:
            return false;
          }
	}
	
}
