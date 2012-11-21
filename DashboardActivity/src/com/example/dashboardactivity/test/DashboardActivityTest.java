package com.example.dashboardactivity.test;

import com.example.dashboardactivity.DashboardActivity;
import com.example.dashboardactivity.LoginActivity;
import com.example.dashboardactivity.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.dashboardactivity.R;
import com.jayway.android.robotium.solo.Solo;

public class DashboardActivityTest extends ActivityInstrumentationTestCase2<DashboardActivity>{

	DashboardActivity activityWeTest;
	Button btnLogout, btnConnectToTwitter;
	EditText inputEmail, inputPassword;
	
	private  Solo solo;	
	
	public DashboardActivityTest(){
		super(DashboardActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();
		solo = new Solo(getInstrumentation(), getActivity());		
	}
	
	//passed
	public void testSolo(){
		solo.assertCurrentActivity("check on 1st activity", DashboardActivity.class);
	}
	
	// passed
	public void testSoloGoToTwitter(){
		solo.clickOnButton("Go To Twitter");
		solo.assertCurrentActivity("should go to Twitter", MainActivity.class);
		solo.goBack();
		solo.assertCurrentActivity("should go to DashboardActivity", DashboardActivity.class);
	}
	
	// passed
	public void testSoloLogoutDashboard(){
		solo.clickOnButton("Logout Me");
		solo.assertCurrentActivity("should go to LoginActivity", LoginActivity.class);
		/*
		// user is logged out at this point, need to relogged in
		inputPassword = (EditText) activityWeTest.findViewById(R.id.loginPassword);
		inputEmail = (EditText) activityWeTest.findViewById(R.id.loginEmail);
		solo.goBack();
		solo.enterText(inputEmail, "s@s.com");
		solo.enterText(inputPassword, "12345");
		solo.clickOnButton("Login");
		solo.assertCurrentActivity("should launch DashboardActivity", DashboardActivity.class);
		*/
	}
	
	// previously passed GUI tests
	public void testButtons(){
		btnLogout = (Button) activityWeTest.findViewById(R.id.btnLogout);
		assertNotNull(btnLogout);
		assertEquals("Logout Me", btnLogout.getText());
		
		btnConnectToTwitter = (Button) activityWeTest.findViewById(R.id.btnConnectTwitter);
		assertNotNull(btnConnectToTwitter);
		assertEquals("Go To Twitter", btnConnectToTwitter.getText());		
	}

}
