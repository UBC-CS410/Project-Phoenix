package com.phoenix.mapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Security extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//go to mainapp page if signin button clicked
		// TODO check with server to log in if login fail--retrurn to same page
		// TODO page pause when call; resume while login fail; delete when success 
		
		Button tomain = (Button) findViewById(R.id.button1);
		tomain.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent("com.phoenix.mapi.MAINPAGE"));
			}
		});
		Button tosignup = (Button) findViewById(R.id.button2);
		tosignup.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent("com.phoenix.mapi.SIGNUP"));
			}
		});
	}
	
}
