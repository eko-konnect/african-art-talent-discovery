package com.ekoconnect.afriphoto.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsng.adapters.NotificationAdapter;
import com.appsng.connectors.AppUtility;
import com.appsng.models.Notification;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.R;
import com.ekoconnect.afriphotos.RefreshListView;


public class Notifications extends BaseFragment {
	String photo_id;
	ArrayList<Notification> notifications = new ArrayList<Notification>();
	NotificationAdapter notificationAdapter;
	RefreshListView listview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
		acBar.setTitle("Notifications");
		}catch(Exception s){
			
		}
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View viewer =  (View)inflater.inflate(R.layout.fragment_notification, container, false);
		listview = (RefreshListView)viewer.findViewById(R.id.notification_list);
		
		getNotificationFromLocalDatabase();
		return viewer;
	}
	


	
	private void getNotificationFromLocalDatabase() {
		notifications.clear();
		notifications.addAll(localDataBase.getNotifications());
		notificationAdapter = new NotificationAdapter(context, notifications);
		listview.setAdapter(notificationAdapter);
		
		setClickEvent();
	}

	private void setClickEvent() {
		listview.setRefreshListener(new com.ekoconnect.afriphotos.RefreshListView.OnRefreshListener() {
			public void onRefresh(com.ekoconnect.afriphotos.RefreshListView listView) {
				listview.finishRefreshing();		

			}
		});
	}



	private void checkCommentsOnline() {
		if(Utilities.isOnline((Activity) context)) {
			AppUtility appUtility = new AppUtility(context);
			appUtility.getPhotoComments(photo_id);
		}else {
			Utilities.Toaster(context, Utilities.NO_INTERNET_TOAST);
		}
	}
	
/*	
    *//**
     * Receiving push messages
     * *//*
   public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean fetch = intent.getExtras().getBoolean("fetch_notifications");
           if(fetch) {
        	   getNotificationFromLocalDatabase();
           }
        }
    };
	*/

}
