package com.ekoconnect.afriphoto.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appsng.connectors.AppUtility;
import com.appsng.reusables.CropOption;
import com.appsng.reusables.CropOptionAdapter;
import com.appsng.reusables.ImageHelper;
import com.appsng.reusables.TransparentDialog;
import com.appsng.reusables.Utilities;
import com.ekoconnect.afriphotos.R;

public class Register extends BaseFragment{

	ImageView placeholder;
    static final int FIRST = Menu.FIRST;
    EditText name,email,password;
	public static final int PICS_FROM_CAMERA = 0; 
	public static final int PICS_FROM_GALLERY = 1; 
	public static final int CROP_FROM_CAMERA = 2;
	protected static Uri mImageCaptureUri;
	private static Bitmap bm;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		acBar.setTitle("Registration");
		acBar.setDisplayUseLogoEnabled(false);
		acBar.setDisplayShowHomeEnabled(false);
		
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View viewer = (View) inflater.inflate(R.layout.fragment_register, container, false);
		placeholder = (ImageView)viewer.findViewById(R.id.placeholder);
		
		name = (EditText)viewer.findViewById(R.id.name);
		email = (EditText)viewer.findViewById(R.id.email);
		password = (EditText)viewer.findViewById(R.id.password);
		
		Bitmap  mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		placeholder.setImageBitmap(ImageHelper.getRoundedCornerBitmap(mBitmap, 500));
		
		setClickEvents();
		return viewer;
	}
	
	

	private void startSubmitProcess() {
		if(validateFields()) {
			TransparentDialog dialog = new TransparentDialog(getSherlockActivity(), R.drawable.ajax_loader);
			AppUtility appUtility = new AppUtility(context);
			appUtility.registerUser(
					name.getText().toString(), 
					password.getText().toString(),
					email.getText().toString(), 
					Utilities.EncodeBitmap(bm),
					dialog);
			dialog.show();
		}
	}
	

	
	private void animatePlaceHolder() {
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3000);
		placeholder.setAnimation(anim);
		placeholder.startAnimation(anim);
	}
	
	protected boolean validateFields(){
		boolean status = false;
		if(name.length() < 2){
			status = false; 
			Utilities.Toaster(getActivity(), "Please enter a valid name");
		}else if(email.length() == 0){
			status = false; 
			Utilities.Toaster(getActivity(), "Enter your email.");
		}else if(password.length() == 0){
			status = false; 
			Utilities.Toaster(getActivity(), "Enter your password.");
		}else{
			status = true;
		}
		return status;
	}
	
	private void setClickEvents() {
		placeholder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picSelectDialog();
			}
		});	
	}

	protected void picSelectDialog() {
		Dialog dialog = new Dialog(getActivity()); 
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
        //builder.setTitle("Add Photo");
        final String[] items = new String[]{ "Take Photo", "Select From Gallery" };
        builder.setItems(items, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
         	   if(items[which].toString() == "Take Photo"){
         		   TakePhoto();
         	   }else{
         		   SelectPhoto();
         	   }
            }
        });
        dialog = builder.create();
        dialog.show();	
	}
	
	private void TakePhoto(){
		Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
						   "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
		try {
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PICS_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			}
	}
	
	private void SelectPhoto(){
		Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      intent.putExtra("crop", "true");
      intent.putExtra("aspectX", 1);
      intent.putExtra("aspectY", 1);
      intent.putExtra("outputX", 200);
      intent.putExtra("outputY", 200);
      
      try {
          intent.putExtra("return-data", true);
          startActivityForResult(intent, PICS_FROM_GALLERY);
      } catch (ActivityNotFoundException e) {
          //Do nothing for now
      }                
	}
    
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != Activity.RESULT_OK) return;
	    switch (requestCode){
	    case PICS_FROM_CAMERA:
	    	 doCrop();
             break;
	    case PICS_FROM_GALLERY:
             bm = (Bitmap) data.getExtras().get("data"); 
            // avatar.setScaleType(ScaleType.CENTER_CROP);
             //avatar.setImageBitmap(bm); 
     		placeholder.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bm, 500));

             break;
	    case CROP_FROM_CAMERA:	    	
	        Bundle extras = data.getExtras();
	        if (extras != null) {	        	
	             bm = extras.getParcelable("data");
	      		placeholder.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bm, 500));
	        }
	        File f = new File(mImageCaptureUri.getPath());            
	        
	        if (f.exists()) f.delete();

	        break;
	    }
    };

    public void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
    	
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
        	intent.setData(mImageCaptureUri);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            
        	if (size == 1) {
        		Intent i 		= new Intent(intent);
	        	ResolveInfo res	= list.get(0);
	        	
	        	i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        	
	        	startActivityForResult(i, CROP_FROM_CAMERA);
        	} else {
		        for (ResolveInfo res : list) {
		        	final CropOption co = new CropOption();
		        	
		        	co.title 	= getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
		        	co.icon		= getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
		        	co.appIntent= new Intent(intent);
		        	
		        	co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
		        	
		            cropOptions.add(co);
		        }
	        
		        CropOptionAdapter adapter = new CropOptionAdapter(getActivity(), cropOptions);
		        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setTitle("Choose Crop App");
		        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
		            public void onClick( DialogInterface dialog, int item ) {
		                startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
		            }
		        });
	        
		        builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
		            @Override
		            public void onCancel( DialogInterface dialog ) {
		               
		                if (mImageCaptureUri != null ) {
		                	getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
		                	mImageCaptureUri = null;
		                }
		            }
		        } );
		        
		        AlertDialog alert = builder.create();
		        
		        alert.show();
        	}
        }
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
       
        
    	menu.add(0, FIRST, Menu.NONE, "Done")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);    	
    }

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().finish();
			break;
		case FIRST:
			startSubmitProcess();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
