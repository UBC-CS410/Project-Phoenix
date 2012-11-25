package com.example.dashboardactivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.Util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



public class FbActivity extends Activity  {
	private static String APP_ID="441750162554944";
	private Facebook facebook;
	//private AsyncFacebookRunner mAsyncRunner;
	String FILENAME="AndroidSSO_data";
	private SharedPreferences mPrefs;
	TextView w;
	ImageView pic;
	
	//comment
	private static String txtlist="";
	
	//comment
	Button submit;
	EditText input;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fblayout);
		
		//new Loadcomment().execute();
		//##################################################
		//getcomment
		UserFunctions user=new UserFunctions();
		JSONObject json=user.getcomment("www.com");
		try{
			//get item
			int success =json.getInt("success");
			
			if(success==1)
			{
				JSONArray comments=json.getJSONArray("comments");
				for(int i=0;i<comments.length();i++)
				{
					JSONObject c=comments.getJSONObject(i);
					String id=c.get("email").toString();
					String comment=c.get("comment").toString();
					txtlist=txtlist+comment+"\n\t\t"+id+"\n";
				}
			}
			else
			{}
		}
		catch(JSONException e)
		{e.printStackTrace();}
		TextView txtbox=(TextView)findViewById(R.id.txtbox);
    	txtbox.setText(txtlist);
    	//end
		//#######################################
		
		
		facebook = new Facebook(APP_ID);
       // mAsyncRunner = new AsyncFacebookRunner(facebook);
        mPrefs=getPreferences(MODE_PRIVATE);
        
        loginToFacebook();
        w =(TextView) findViewById(R.id.userID);
        pic=(ImageView) findViewById(R.id.profilpic);

        //comment
        input=(EditText)findViewById(R.id.commenttxt);
        submit=(Button)findViewById(R.id.submit);
        
        String access=mPrefs.getString("access_token", null);
        long expire=mPrefs.getLong("access_expires",0);
        if(access!=null)
        {facebook.setAccessToken(access);}
        if(expire!=0)
        {facebook.setAccessExpires(expire);}
        
    	JSONObject obj=null;
    	URL img=null;
    	
    	try{
    	String jUser=facebook.request("me");
    	obj=Util.parseJson(jUser);
    	String id=obj.optString("id");
    	String name=obj.optString("name");
    	w.setText("Hi"+name);
    	img=new URL("http://graph.facebook.com/"+id+"/picture?type=normal");
    	Bitmap bmp=BitmapFactory.decodeStream(img.openConnection().getInputStream());
    	pic.setImageBitmap(bmp);
    	}
    	catch(FacebookError e)
    	{e.printStackTrace();}
    	catch(JSONException e)
    	{e.printStackTrace();}
    	catch(MalformedURLException e)
    	{e.printStackTrace();}
    	catch (IOException e) 
    	{e.printStackTrace();}
    	
    	submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String comment=input.getText().toString();
				String email="email";
				String url="www.com";
				
				UserFunctions user=new UserFunctions();
				JSONObject json=user.comment_Image(url, email, comment);
				try {
					input.setText(json.get("message").toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    	
    	
	}
/*
	//################################
	//load comment function
	// Progress Dialog
    private ProgressDialog pDialog;
	class Loadcomment extends AsyncTask<String,String,String>
	{
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FbActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			UserFunctions user=new UserFunctions();
			JSONObject json=user.getcomment("www.com");
			try{
				//get item
				int success =json.getInt("success");
				
				if(success==1)
				{
					JSONArray comments=json.getJSONArray("comments");
					for(int i=0;i<comments.length();i++)
					{
						JSONObject c=comments.getJSONObject(i);
						String id=c.getString("email");
						String comment=c.getString("comment");
						txtlist=txtlist+comment+"\n\t\t"+id+"\n";
					}
				}
				else
				{}
			}
			catch(JSONException e)
			{e.printStackTrace();}
			
			return null;
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                	TextView txtbox=(TextView)findViewById(R.id.txtbox);
                	txtbox.setText(txtlist);
                }
            });
 
        }
	}
	
	*/
	
	
	
	//fb login func
    public void loginToFacebook() {
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
     
        if (access_token != null) {
            facebook.setAccessToken(access_token);
        }
     
        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }
     
        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[] { "email", "publish_stream" },
                    new DialogListener() {
     
                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                        }
     
                        @Override
                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();
                        }
     
                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error
     
                        }
     
                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors
     
                        }
     
                    });
        }
    }
}
