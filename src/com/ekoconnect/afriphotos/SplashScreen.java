package com.ekoconnect.afriphotos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphoto.fragments.Home;

public class SplashScreen extends Activity {

	Context context;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        finish();
        if(Utilities.isLoggedIn(context)) {
      		 startActivity(new Intent(getBaseContext(),AwesomeActivity.class));
         	//Utilities.newFragment = new Home();
      		// startActivity(new Intent(getBaseContext(),ActivityFragment.class));

        }else {
      		 startActivity(new Intent(getBaseContext(),ActivityFragment.class));

        }

	}
	
}
