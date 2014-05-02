package com.ekoconnect.afriphotos;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.appsng.adapters.PortfolioAdapter;
import com.appsng.connectors.AppUtility;
import com.appsng.connectors.LocalDataBase;
import com.appsng.models.PhotoList;
import com.appsng.reusables.TransparentDialog;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphoto.fragments.Register;

public class Portfolio extends Activity {
	
	static GridView listView;
	static ArrayList<com.appsng.models.Portfolio> portfolios = new ArrayList<com.appsng.models.Portfolio>();
	static LocalDataBase localDataBase;
	static ArrayList<PhotoList> photolist = new ArrayList<PhotoList>();
	static PortfolioAdapter adapter;
	static EditText name;
	static Button create;
	boolean haveAccount = false;
	static SharedPreferences sharedPreferences;
	static Context context;
	String userid;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = this;
        setContentView(R.layout.portfolio);        
        listView = (GridView)findViewById(R.id.portfolio);
        
        name = (EditText)findViewById(R.id.name);
        create = (Button)findViewById(R.id.create);
        
        localDataBase = new LocalDataBase(this);
		setTitle("Portfolio");
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		loadDataFromDb();
		
        userid = sharedPreferences.getString("userid", "");
		if(userid != "") {
			haveAccount = true;
			showUploadButtons(haveAccount);
		}else {
			showUploadButtons(false);
			Utilities.showAlert(this, "Need an account", "Hi there, you need to create an account to upload your art photos.");
			Utilities.newFragment = new Register();
			startActivity(new Intent(this, ActivityFragment.class));
			overridePendingTransition(R.anim.slide_in_left_, R.anim.slide_out_left_);
		}
    }
    
    
    private void showUploadButtons(boolean show) {
    	if(show) {
        	((Button)findViewById(R.id.create)).setVisibility(View.VISIBLE);
        	((EditText)findViewById(R.id.name)).setVisibility(View.VISIBLE);
    	}else {
    		((Button)findViewById(R.id.create)).setVisibility(View.GONE);
    		((EditText)findViewById(R.id.name)).setVisibility(View.GONE);
    	}
	}

    

    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
        userid = sharedPreferences.getString("userid", "");
    	if(userid != "") {
			showUploadButtons(true);
		}else {
			showUploadButtons(false);
		}
    	super.onResume();
    }



	public static void loadDataFromDb() {
		portfolios = localDataBase.getPortfolios();
        setUpListView();
        localDataBase.close();
	}

	private static void setUpListView() {
		adapter = new PortfolioAdapter(context, portfolios);
		listView.setAdapter(adapter);		
		setClickListener();
	}

	private static void setClickListener() {
		create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(name.getText().toString().length() < 3) {
					Utilities.Toaster(context, "Invalid name");
				}else {
					TransparentDialog dialog = new TransparentDialog(context, R.drawable.ajax_loader);
					AppUtility appUtility = new AppUtility(context);
					appUtility.createPortfolio(name.getText().toString(), sharedPreferences.getString("userid", ""), dialog);
					name.setText("");
				}
			}
		});
		
	}
	
	
	
   
}
