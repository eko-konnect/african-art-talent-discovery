package com.ekoconnect.afriphotos;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ekoconnect.afriphoto.fragments.Home;
import com.ekoconnect.afriphotos.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;

public class CopyOfMainActivity extends SherlockFragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_layout);
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Tab 1",
                        getResources().getDrawable(android.R.drawable.star_on)),
                Home.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Tab 2",
                        getResources().getDrawable(android.R.drawable.star_on)),
                        Home.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Tab 3",
                        getResources().getDrawable(android.R.drawable.star_on)),
                        Home.class, null);
    }
    
	public void openCameraActivity(View b)
	{
	/*	Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);*/
	}
}