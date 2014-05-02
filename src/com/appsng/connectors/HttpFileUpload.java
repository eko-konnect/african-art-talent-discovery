package com.appsng.connectors;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import com.appsng.models.Photo;
import com.appsng.reusables.Utilities;


import android.util.Log;
	
public class HttpFileUpload implements Runnable{
        URL connectURL;
        String responseString;
        String file_nameA,userid,caption,portfolio;
        byte[ ] dataToServer;
        FileInputStream fileInputStream = null;

        public HttpFileUpload(String url,String filename,String user_id,String caption,String portfolio_id){
                try{
                        connectURL = new URL(url);
                        file_nameA = filename;
                        this.userid = user_id;
                        this.caption = caption;
                        this.portfolio = portfolio_id;
                }catch(Exception ex){
                    Log.i("HttpFileUpload","URL Malformatted");
                }
        }
	
        public void Send_Now(FileInputStream fStream){
            Log.i("Send_Now","I was here");

                fileInputStream = fStream;
                Sending();

        }
	
        void Sending(){
                String iFileName = file_nameA;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                String Tag="fSnd";
                try
                {
                        Log.e(Tag,"Starting Http File Sending to URL");
	
                        // Open a HTTP connection to the URL
                        HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();
	
                        // Allow Inputs
                        conn.setDoInput(true);
	
                        // Allow Outputs
                        conn.setDoOutput(true);
	
                        // Don't use a cached copy.
                        conn.setUseCaches(false);
	
                        // Use a post method.
                        conn.setRequestMethod("POST");
	
                        conn.setRequestProperty("Connection", "Keep-Alive");
	
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
	
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());


                        
		                dos.writeBytes("Content-Disposition: form-data; name=\"filename\""+ lineEnd);
		                dos.writeBytes(lineEnd);
		                dos.writeBytes(file_nameA);
		                dos.writeBytes(lineEnd);
		                dos.writeBytes(twoHyphens + boundary + lineEnd);
	                    
			                dos.writeBytes("Content-Disposition: form-data; name=\"user_id\""+ lineEnd);
			                dos.writeBytes(lineEnd);
			                dos.writeBytes(userid);
			                dos.writeBytes(lineEnd);
			                dos.writeBytes(twoHyphens + boundary + lineEnd);
		                    
			                dos.writeBytes("Content-Disposition: form-data; name=\"portfolio_id\""+ lineEnd);
			                dos.writeBytes(lineEnd);
			                dos.writeBytes(portfolio);
			                dos.writeBytes(lineEnd);
			                dos.writeBytes(twoHyphens + boundary + lineEnd);
		                    
			                dos.writeBytes("Content-Disposition: form-data; name=\"caption\""+ lineEnd);
			                dos.writeBytes(lineEnd);
			                dos.writeBytes(caption);
			                dos.writeBytes(lineEnd);
			                dos.writeBytes(twoHyphens + boundary + lineEnd);


	
                        
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + iFileName +"\"" + lineEnd);
                        dos.writeBytes(lineEnd);
	
                        Log.e(Tag,"Headers are written");
	
                        // create a buffer of maximum size
                        int bytesAvailable = fileInputStream.available();
	                        
                        int maxBufferSize = 1024;
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        byte[ ] buffer = new byte[bufferSize];
	
                        // read file and write it into form...
                        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	
                        while (bytesRead > 0)
                        {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
                        }
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	
                        // close streams
                        fileInputStream.close();
	                        
                        dos.flush();
	                        
                        Log.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));
	                         
                        InputStream is = conn.getInputStream();
	                        
                        // retrieve the response from server
                        int ch;
	
                        StringBuffer b =new StringBuffer();
                        while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
                        String s=b.toString();
                        
                        try{
                        	JSONObject jsonObject = new JSONObject(s);
                        	if(jsonObject.get("status").toString().equals("true")){
                                Utilities.completeStatus = true;
                                Utilities.jsonObject = jsonObject.getJSONObject("photo").getJSONObject("Photo");
                        	}else{
                                Utilities.completeStatus = false;
                        	}
                        }catch(Exception d){
                        		Utilities.completeStatus = false;
                        }
                        
                        
                        
                        Log.i("Response",s);
                        dos.close();
                }
                catch (MalformedURLException ex)
                {
                        Log.e(Tag, "URL error: " + ex.getMessage(), ex);
                }
	
                catch (IOException ioe)
                {
                        Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
                }
        }
	
        @Override
        public void run() {
                // TODO Auto-generated method stub
        }
}