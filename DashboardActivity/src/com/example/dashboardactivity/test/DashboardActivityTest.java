package com.example.dashboardactivity.test;

import com.example.dashboardactivity.DashboardActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;

import com.example.dashboardactivity.R;

public class DashboardActivityTest extends ActivityInstrumentationTestCase2<DashboardActivity>{

	DashboardActivity activityWeTest;
	Button btnLogout, btnConnectToTwitter;
	
	
	public DashboardActivityTest(){
		super(DashboardActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	/*
	public DashboardActivityTest(Class<DashboardActivity> activityClass) {
		super(activityClass);
		Log.i("tester 1", "within the constructor");
	}*/
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();
		
	}
	
	public void testDashboardActivityClassNotNull() {
	   assertNotNull(activityWeTest);
	  }
	
	public void testLogoutButton(){
		btnLogout = (Button) activityWeTest.findViewById(R.id.btnLogout);
		assertNotNull(btnLogout);
		assertEquals("Logout Me", btnLogout.getText());
	}
	
	public void testConntectToTwitterButton(){
		btnConnectToTwitter = (Button) activityWeTest.findViewById(R.id.btnConnectTwitter);
		assertNotNull(btnConnectToTwitter);
		assertEquals("Go To Twitter", btnConnectToTwitter.getText());
		
	}
	

}
