package com.phoenix.mapi;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.view.Menu;

public class Splash extends Activity {

    @Override
    // display logo
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread Timer = new Thread()
        {
        	public void run(){
        		try{
        			//set up timer
        			int Timer=0;
        			while(Timer<3000){
        				sleep(100);//make it pause.1sec
        				Timer+=100;
        			}
        			startActivity(new Intent("com.phoenix.mapi.CLEARSCREEN"));
        		}
        	 catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		finally{
        			finish();//shut down
        		}
        	}
        };
        Timer.start();
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

    /**@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
}
