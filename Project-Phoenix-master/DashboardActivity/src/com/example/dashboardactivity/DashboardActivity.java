package com.example.dashboardactivity;


import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 
import library.UserFunctions;
 
public class DashboardActivity extends Activity {
	
	
    UserFunctions userFunctions;
    //facebook here
    Button facebookbtn;
    
    Button btnLogout;
    Button btnConnectTwitter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
       // user already logged in show databoard
            setContentView(R.layout.dashboard);
       //fb here
            
            facebookbtn=(Button)findViewById(R.id.facebookbtn);
            
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnConnectTwitter = (Button) findViewById(R.id.btnConnectTwitter);  //@#@#
            
            //fb here once click goto the fb layout
            facebookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent fb=new Intent(getApplicationContext(),FbActivity.class);
                        startActivity(fb);
                    }
            });
            
            btnConnectTwitter.setOnClickListener(new View.OnClickListener() { //@#@#

				public void onClick(View v) {
					Intent twitterPage = new Intent(getApplicationContext(), MainActivity.class);
					twitterPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(twitterPage);
					finish();
				}
            	
            });
 
            btnLogout.setOnClickListener(new View.OnClickListener() {
 
                public void onClick(View arg0) {
                    userFunctions.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Closing dashboard screen
                    finish();
                }
            });
 
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }
    }
    
    
    
}
