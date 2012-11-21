package com.example.dashboardactivity.test;

import junit.framework.Assert;

import com.example.dashboardactivity.DashboardActivity;
import com.example.dashboardactivity.LoginActivity;
import com.example.dashboardactivity.R;
import com.example.dashboardactivity.RegisterActivity;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{

	LoginActivity activityWeTest;
	Button btnLogin, btnLinkToRegister;
	EditText inputEmail, inputPassword;
	TextView loginErrorMsg;
	
	private  Solo solo;

	
	public LoginActivityTest(){
		super(LoginActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();		
		solo = new Solo(getInstrumentation(), getActivity());
		
		//btnLogin = (Button) activityWeTest.findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) activityWeTest.findViewById(R.id.btnLinkToRegisterScreen);
		inputPassword = (EditText) activityWeTest.findViewById(R.id.loginPassword);
		inputEmail = (EditText) activityWeTest.findViewById(R.id.loginEmail);
		//loginErrorMsg = (TextView) activityWeTest.findViewById(R.id.login_error);
	}
	
	// passed
	public void testSoloLogin(){
		//Assert.assertTrue(solo.searchButton("Login")); // this works!
		solo.enterText(inputEmail, "s@s.com");
		solo.enterText(inputPassword, "12345");
		solo.clickOnButton("Login");
		solo.assertCurrentActivity("should launch DashboardActivity", DashboardActivity.class);
	}
	
	// passed
	public void testSoloIRegisterBackToLogin(){
		// way 1
		solo.clickOnButton("I don't have account. Register Me!");
		solo.assertCurrentActivity("should launch RegisterActivity", RegisterActivity.class);		
		solo.goBack(); // remove virtual keboard
		solo.goBack();
		solo.assertCurrentActivity("should go back to LoginActivity", LoginActivity.class);
		
		// way 2
		solo.clickOnButton("I don't have account. Register Me!");
		solo.assertCurrentActivity("should launch RegisterActivity", RegisterActivity.class);
		solo.goBack(); // remove virtual keboard
		solo.clickOnButton("Already registred. Login Me!");
		solo.assertCurrentActivity("should go back to LoginActivity", LoginActivity.class);
	}
	
	
	// previsouly passed GUI tests
	public void testLoginActivityClassNotNull() {
	   assertNotNull(activityWeTest);
	}
	
	public void testButtons(){		
		// login button
		btnLogin = (Button) activityWeTest.findViewById(R.id.btnLogin);
		assertNotNull(btnLogin);
		assertEquals("Login", btnLogin.getText());
		
		// register button
		btnLinkToRegister = (Button) activityWeTest.findViewById(R.id.btnLinkToRegisterScreen);
		assertNotNull(btnLinkToRegister);
		assertEquals("I don't have account. Register Me!", btnLinkToRegister.getText());
	}
	
	
	public void testEditText(){
		// input email 
		inputEmail = (EditText) activityWeTest.findViewById(R.id.loginEmail);
		assertNotNull(inputEmail);
		
		// input password
		inputPassword = (EditText) activityWeTest.findViewById(R.id.loginPassword);
		assertNotNull(inputPassword);
	}
	
	public void testErrorMsgTextViewNotNull(){
		loginErrorMsg = (TextView) activityWeTest.findViewById(R.id.login_error);
		assertNotNull(loginErrorMsg);
	}	
}
