package com.example.dashboardactivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 
import library.ConnectionDetector;
import library.UserFunctions;
 
public class DashboardActivity extends Activity {
    UserFunctions userFunctions;
    Button btnLogout;
    Button btnConnectTwitter;
    private ConnectionDetector cd;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(getApplicationContext());
        
     // check internet connection at the very begin        
        if (!cd.isConnectingToInternet()){
        	checkInternetConnection();
        }          
        
        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
       // user already logged in show databoard
            setContentView(R.layout.dashboard);
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnConnectTwitter = (Button) findViewById(R.id.btnConnectTwitter);
            
            
            btnConnectTwitter.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					//TODO
					//startActivity(new Intent(getApplicationContext(), MainActivity.class));
					Intent twitterPage = new Intent(getApplicationContext(), MainActivity.class);
					//twitterPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(twitterPage);
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
    
    
	private void checkInternetConnection() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Error");
		alertDialog.setMessage("Do you want turn on the Internet?");
		alertDialog.setPositiveButton("yes", new OnClickListener() {
			// @Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startActivity(new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		alertDialog.setNegativeButton("cancle", new OnClickListener() {
			// @Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}
}
