package com.ekoconnect.afriphotos;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * @author Adil Soomro
 *
 */
@SuppressWarnings("deprecation")
public class AwesomeActivity extends TabActivity {
	
	TabHost tabHost;

	/** Called when the activity is first created. */
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tabHost = getTabHost();
		setTabs();
	}
	
	private void setTabs()
	{
		addTab("Home", R.drawable.tab_home, HomeActivity.class);
		addTab("My Photos", R.drawable.tab_search, MyPhotos.class);
		addTab("Upload", R.drawable.tab_search, CameraActivity.class);
		addTab("Portfolio", R.drawable.tab_home, Portfolio.class);
		addTab("Notifications", R.drawable.tab_search, NotificationActivity.class);
	}
	
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
        overridePendingTransition(R.anim.slide_out_left_, R.anim.slide_in_left_);

	}
	public void openCameraActivity(View b)
	{
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}
}