package com.ekoconnect.afriphoto.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appsng.connectors.AppUtility;
import com.appsng.reusables.ImageHelper;
import com.appsng.reusables.TransparentDialog;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.ActivityFragment;
import com.ekoconnect.afriphotos.AwesomeActivity;
import com.ekoconnect.afriphotos.R;

public class Index extends BaseFragment{
	Button login,register,proceed;
	ImageView placeholder,close;
	LinearLayout loginlayout;
	EditText email,password;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View viewer = (View) inflater.inflate(R.layout.fragment_index, container, false);
		login = (Button)viewer.findViewById(R.id.login);
		proceed = (Button)viewer.findViewById(R.id.proceed);
		register = (Button)viewer.findViewById(R.id.register);
		email = (EditText)viewer.findViewById(R.id.email);
		password = (EditText)viewer.findViewById(R.id.password);
		loginlayout = (LinearLayout)viewer.findViewById(R.id.login_layout);
		acBar.hide();
		
		placeholder = (ImageView)viewer.findViewById(R.id.placeholder);
		close = (ImageView)viewer.findViewById(R.id.close);
		Bitmap  mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		placeholder.setImageBitmap(ImageHelper.getRoundedCornerBitmap(mBitmap, 500));
		setClickEvents();
		return viewer;
	}

	
	
	private void setClickEvents() {
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleLogin(true);
			}
		});
		
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleLogin(false);
			}
		});
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.newFragment = new Register();
				startActivity(new Intent(getActivity(), ActivityFragment.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left_, R.anim.slide_out_left_);
			}
		});
		
		proceed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(validate()) {
					if(Utilities.isOnline(getActivity())) {
						TransparentDialog dialog = new TransparentDialog(getActivity(), R.drawable.ajax_loader);
						dialog.show();
						AppUtility appUtility = new AppUtility(getActivity());
						appUtility.LoginUser(
								dialog, 
								email.getText().toString(), 
								password.getText().toString()
								);
						
					}else {
						Utilities.Toaster(getActivity(), Utilities.NO_INTERNET_TOAST);
					}
				}
			}
		});
		
		placeholder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
          	  	Intent intent = new Intent(context, AwesomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				context.startActivity(intent);
			}
		});
	}
	
	
	protected boolean validate() {
		if(email.getText().toString().isEmpty()) {
			Utilities.Toaster(getActivity(), "Enter your email");
			return false;
		}else if(password.getText().toString().isEmpty()) {
			Utilities.Toaster(getActivity(), "Enter your password");
			return false;
		}else {
			return true;
		}
	}

	


	private void toggleLogin(boolean showlogin) {
		Animation bottom = AnimationUtils.loadAnimation(getSherlockActivity(),R.anim.bottom_down);
		Animation bottomUp = AnimationUtils.loadAnimation(getSherlockActivity(),R.anim.bottom_up);
		if(showlogin) {
			loginlayout.setVisibility(View.VISIBLE);
			loginlayout.startAnimation(bottomUp);
			placeholder.setVisibility(View.GONE);
			placeholder.startAnimation(bottom);
			
		}else {
			placeholder.startAnimation(bottomUp);
			loginlayout.setVisibility(View.GONE);
			loginlayout.startAnimation(bottom);
			placeholder.setVisibility(View.VISIBLE);
		}
	}
	
	
	private void AttachDialog() {
		  final Dialog dialog = new Dialog(getActivity());
		    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    dialog.setContentView(R.layout.single_image_view);
		    final Window window = dialog.getWindow();
		    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		    dialog.show();		
	}

	
	
}
