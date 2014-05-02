package com.ekoconnect.afriphotos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsng.connectors.HttpFileUpload;
import com.appsng.connectors.LocalDataBase;
import com.appsng.models.Photo;
import com.appsng.models.Portfolio;
import com.appsng.reusables.Utilities;
import com.appsng.reusables.ZoomImage;
import com.ekoconnect.afriphoto.fragments.Register;


public class CameraActivity extends Activity {
	private static ProgressDialog dialogupload;
	private static Bitmap bm;
	ZoomImage image;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int SELECT_PHOTO = 100;
	Button upload;
    String path,userid,caption,portfolio = "0";
    SharedPreferences sharedPreferences;
	boolean haveAccount = false;
    Uri uri;
    LocalDataBase localDataBase;
    ArrayList<Portfolio> portfolios = new ArrayList<Portfolio>();

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);      
        
        image = (ZoomImage)findViewById(R.id.largeimage);
        upload = (Button)findViewById(R.id.upload);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        userid = sharedPreferences.getString("userid", "");
        localDataBase = new LocalDataBase(getApplicationContext());
        
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
    
    private void showUploadButtons(boolean show) {
    	if(show) {
        	((Button)findViewById(R.id.gallery)).setVisibility(View.VISIBLE);
        	((Button)findViewById(R.id.snap)).setVisibility(View.VISIBLE);
    	}else {
    		((Button)findViewById(R.id.gallery)).setVisibility(View.GONE);
    		((Button)findViewById(R.id.snap)).setVisibility(View.GONE);
    	}
	}



	public void takePhotoCamera(View v){
    	 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
    	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    	    }
	}
	
    public void pickFromGallery(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO); 
	}
	
    public void uploadPhoto(View v){
    	path = getRealPathFromURI(uri);
    
    	Dialog dialog = new Dialog(this);
	    dialog.setTitle("Enter caption");
	    View view = getLayoutInflater().inflate(R.layout.dialog_photo_caption, null);
	    final TextView cap = (TextView) view.findViewById(R.id.caption);
	    final Spinner pot = (Spinner) view.findViewById(R.id.portfolio);
	    final Button create = (Button) view.findViewById(R.id.create);
	    
	    portfolios = localDataBase.getPortfolios();
	    localDataBase.close();
	    if(portfolios.size() > 0) {
	    	ArrayAdapter<Portfolio> adapter = new ArrayAdapter<Portfolio>(getApplicationContext(),R.layout.spinner_black_text_item, portfolios);
	    	pot.setAdapter(adapter);
	    	create.setVisibility(View.GONE);
	    	pot.setVisibility(View.VISIBLE);
	    	
	    }else {
	    	pot.setVisibility(View.GONE);
	    	create.setVisibility(View.VISIBLE);
	    }
	    
	   
	    
	    pot.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				portfolio = portfolios.get(position).getPortfolioid();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub	
			}
		});
	    
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	    builder.setTitle("Enter caption of photo");
	    builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.setPositiveButton("Start Upload", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(cap.getText().toString().length() < 3) {
					Utilities.Toaster(getApplicationContext(), "Please enter a valid caption");
				}else if(portfolio == "0"){
					Utilities.Toaster(getApplicationContext(), "Please create a portfolio");
				}else {
					caption = cap.getText().toString();
					startThreadUpload(path);
				}
			}
		});
	     
	    builder.setView(view);
	    dialog = builder.create();
	    dialog.show();
	}
    
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); 

        switch(requestCode) { 
        case SELECT_PHOTO:
            if(resultCode == RESULT_OK){  
                uri = data.getData();
                InputStream imageStream = null;
				try {
					imageStream = getContentResolver().openInputStream(uri);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				bm= BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(bm);          
            }
           break;
        case REQUEST_IMAGE_CAPTURE:
            if(resultCode == RESULT_OK){  
            	   Bundle extras = data.getExtras();
            	   uri = data.getData();
                   bm  = (Bitmap) extras.get("data");
                   image.setImageBitmap(bm);           
              }
        }
        
        if(bm != null) {
        	upload.setVisibility(View.VISIBLE);
        }else {
        	upload.setVisibility(View.GONE);
        }
    }
    
    
 // And to convert the image URI to the direct file system path of the image file
    
    
    public void startThreadUpload(final String filePath){
		dialogupload = ProgressDialog.show(this, "Please wait", "Uploading...",true,false);
    		final Handler handlert = new Handler();
    		new Thread(new Runnable() {
    			public void run() {
    				uploadFile(filePath);
    				handlert.post(new Runnable() {
    					public void run() {
    						if(Utilities.completeStatus) {
        						Utilities.Toaster(getApplicationContext(), "Upload was successful");
        						
        						 ArrayList<Photo> photos = new ArrayList<Photo>();
        						 
        						 try {
                                 photos.add(new Photo(
                                	  Utilities.jsonObject.getString("id"),
                               		  caption, 
                               		  Utilities.jsonObject.getString("url"), 
                               		  portfolio,
                               		  userid,
                               		  "", 
                               		  "0",
                               		  Utilities.jsonObject.getString("created"),
                               		  "0"
                               		  ));
        						 }catch(Exception d) {
        							 
        						 }
                                 
                                 LocalDataBase base = new LocalDataBase(getApplicationContext());
                                 base.saveMyPhoto(photos);
                                 base.close();
        						
        						finish();
    						}else {
        						Utilities.showAlert((Activity) getApplicationContext(), "Not soccessful", "Please try again later. If this persist check your internet connection");
    						}
    						dialogupload.cancel();
    						Thread.interrupted();
    					}
    				});
    			}
    		}).start();
    	}
    	
    	
    private void uploadFile(String file) {
				String fileName = file.substring(file.lastIndexOf("/")+1);
    		    FileInputStream fstrm;
			try {
				fstrm = new FileInputStream(file);
	    	    HttpFileUpload demoHttpFileUpload = new HttpFileUpload(Utilities.URL, fileName, userid, caption, portfolio);
	    		demoHttpFileUpload.Send_Now(fstrm);
			} catch (FileNotFoundException e) {
    			Log.d("uploadFile", e.toString());
			}
    	}
   
    	
    public String getRealPathFromURI(Uri contentUri) {
    	  String[] proj = { MediaStore.Images.Media.DATA };
    	  String result = null;
    	  
    	  CursorLoader cursorLoader = new CursorLoader(
    	    this, 
    	    contentUri, proj, null, null, null);        
    	  Cursor cursor = cursorLoader.loadInBackground();
    	  
    	  if(cursor != null){
    	   int column_index = 
    	     cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	   cursor.moveToFirst();
    	   result = cursor.getString(column_index);
    	  }
    	    
    	  return result;  
    	 }    
}