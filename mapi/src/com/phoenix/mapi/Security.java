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
		Button tomain = (Button) findViewById(R.id.button1);
		tomain.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.phoenix.mapi.MAINPAGE"));
			}
		});
	}
	
}
