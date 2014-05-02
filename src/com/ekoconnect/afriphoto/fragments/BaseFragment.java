package com.ekoconnect.afriphoto.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.appsng.connectors.LocalDataBase;


public class BaseFragment extends SherlockFragment {
   
	ActionBar acBar;
	static Context context;
	static LocalDataBase localDataBase;
	public static SharedPreferences  sharedPref;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
		acBar = getSherlockActivity().getSupportActionBar();
		context = 	getSherlockActivity();
		localDataBase = new LocalDataBase(context);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

	}

	
	
	
}