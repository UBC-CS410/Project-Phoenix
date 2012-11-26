package com.example.dashboardactivity;



import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
 
import library.UserFunctions;
import static library.CommonUT.SENDER_ID;

public class DashboardActivity extends Activity {
    UserFunctions userFunctions;
    private String TAG = "** pushAndroidActivity **";
    private TextView display;
    
    Button btnLogout;
    Button btnConnectTwitter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dashboard);
        
        
        
        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
       // user already logged in show databoard
            setContentView(R.layout.dashboard);
            
            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);
            
            display=(TextView)findViewById(R.id.DISPLAY);
            
            
            final String regId = GCMRegistrar.getRegistrationId(this);
            //Log.i(TAG, "registration id =====  "+regId);

            if (regId.equals("")) {
            GCMRegistrar.register(this, SENDER_ID);
            display.setText("hi"+regId);
            } else {
            //Log.v(TAG, "Already registered");
            display.setText("Already registered"+ regId);
            }

            
            
            
            
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnConnectTwitter = (Button) findViewById(R.id.btnConnectTwitter);  //@#@#
            
            
            btnConnectTwitter.setOnClickListener(new View.OnClickListener() { //@#@#

				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					
					//Intent twitterPage = new Intent(getApplicationContext(), MainActivity.class);
					//twitterPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//startActivity(twitterPage);
					//finish();
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
    @Override
    protected void onPause() {
    super.onPause();
    GCMRegistrar.unregister(this);
    } 
}
