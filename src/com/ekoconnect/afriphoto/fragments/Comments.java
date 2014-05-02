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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appsng.adapters.CommentAdapter;
import com.appsng.connectors.AppUtility;
import com.appsng.models.Comment;
import com.appsng.reusables.TransparentDialog;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.R;
import com.ekoconnect.afriphotos.RefreshListView;


public class Comments extends BaseFragment implements OnClickListener {
	String photo_id;
	ArrayList<Comment> comments = new ArrayList<Comment>();
	CommentAdapter commentAdapter;
	RefreshListView listview;
	ImageButton send;
	TextView comment_text;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		photo_id = getArguments().getString("photo_id");
		context.registerReceiver(mHandleMessageReceiver, new IntentFilter(Utilities.DISPLAY_MESSAGE_ACTION));
		checkCommentsOnline();
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View viewer =  (View)inflater.inflate(R.layout.fragment_comment, container, false);
		listview = (RefreshListView)viewer.findViewById(R.id.comment_list);
		send = (ImageButton)viewer.findViewById(R.id.send);
		comment_text = (TextView)viewer.findViewById(R.id.comment_text);
		send.setOnClickListener(this);
		
		getCommentFromLocalDatabase();
		return viewer;
	}
	

	@Override
	public void onClick(View v) {
		if(Utilities.isOnline((Activity) context)) {
				if(v.getId() == R.id.send) {
						if(comment_text.getText().toString().length() < 2) {
							Utilities.Toaster(context, "Please enter a comment");
						}else {
							TransparentDialog dialog = new TransparentDialog(context, R.drawable.ajax_loader);
							AppUtility appUtility = new AppUtility(context);
							String name = sharedPref.getString("name", "");
							String avatar = sharedPref.getString("photo", "");
							dialog.show();
							appUtility.sendComment(
									photo_id,
									comment_text.getText().toString(),
									name,
									sharedPref.getString("userid", ""),
									avatar,
									dialog
									);
						}
				}
		}else {
			Utilities.Toaster(context, Utilities.NO_INTERNET_TOAST);
		}
	}
	
	
	
	private void getCommentFromLocalDatabase() {
		comments.clear();
		comments.addAll(localDataBase.getComments(photo_id));		
		commentAdapter = new CommentAdapter(context, comments);
		listview.setAdapter(commentAdapter);
		
		setClickEvent();
	}

	private void setClickEvent() {
		listview.setRefreshListener(new com.ekoconnect.afriphotos.RefreshListView.OnRefreshListener() {
			public void onRefresh(com.ekoconnect.afriphotos.RefreshListView listView) {
				Utilities.playSound(context, R.raw.woosh);
				checkCommentsOnline();
			}
		});
		listview.finishRefreshing();			
	}



	private void checkCommentsOnline() {
		if(Utilities.isOnline((Activity) context)) {
			AppUtility appUtility = new AppUtility(context);
			appUtility.getPhotoComments(photo_id);
		}else {
			Utilities.Toaster(context, Utilities.NO_INTERNET_TOAST);
		}
	}
	
	
    /**
     * Receiving push messages
     * */
   public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean fetch = intent.getExtras().getBoolean("fetch_comments");
           if(fetch) {
        	   //Utilities.Toaster(context, "boadcast received");
        	   comment_text.setText("");
        	   getCommentFromLocalDatabase();
           }
        }
    };
	
	
	@Override
	public void onResume() {
		super.onResume();
		context.registerReceiver(mHandleMessageReceiver, new IntentFilter(Utilities.DISPLAY_MESSAGE_ACTION));
	}
    
/*
	@Override
	public void onDestroy() {
		super.onDestroy();
		//context.unregisterReceiver(mHandleMessageReceiver);
	}*/

}
