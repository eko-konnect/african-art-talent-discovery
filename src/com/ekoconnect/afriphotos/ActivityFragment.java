package com.ekoconnect.afriphotos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphoto.fragments.Index;

public class ActivityFragment extends SherlockFragmentActivity{

	Bundle extras;
	SharedPreferences sharedPreferences;

    private static final int CONTENT_VIEW_ID = 10101010;
	public void onCreate(Bundle savedInstanceState) {  
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		
		super.onCreate(savedInstanceState);
     
		  FrameLayout frame = new FrameLayout(this);
	        frame.setId(CONTENT_VIEW_ID);
	        setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	        if (savedInstanceState == null) {
	            
	        	Fragment newFragment = null;
	        	if(Utilities.newFragment == null) {
	        		Utilities.newFragment =  new Index();
	        	}
	        	sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	        	newFragment = Utilities.newFragment;
	            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	            ft.add(CONTENT_VIEW_ID, newFragment).commit();

		        	
	        	}
	        	
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left_, R.anim.slide_out_left_);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Utilities.Toaster(context, "Helloow");
		switch (item.getItemId()) {
		case android.R.id.home:
			
			finish();
			return true;
		}
		//toggle();
		return super.onOptionsItemSelected(item);
	}

	
	
}