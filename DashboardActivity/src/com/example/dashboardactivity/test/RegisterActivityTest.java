package com.example.dashboardactivity.test;

import com.example.dashboardactivity.DashboardActivity;
import com.example.dashboardactivity.R;
import com.example.dashboardactivity.RegisterActivity;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivityTest extends ActivityInstrumentationTestCase2<RegisterActivity>{

	RegisterActivity activityWeTest;
	Button btnRegister, btnLinkToLogin;
	EditText inputFullName, inputEmail, inputPassword;
	TextView registerErrorMsg;
	
	private Solo solo;
	
	public RegisterActivityTest(){
		super(RegisterActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();	
		solo = new Solo(getInstrumentation(), getActivity());
		
		inputFullName = (EditText) activityWeTest.findViewById(R.id.registerName);
		inputEmail = (EditText) activityWeTest.findViewById(R.id.registerEmail);
		inputPassword = (EditText) activityWeTest.findViewById(R.id.registerPassword);
	}
	
	// passed
	public void testSoloRegisterNewAccount(){
		solo.enterText(inputFullName, "soloTestUserName");
		solo.enterText(inputEmail, "soloInputEmail@test.com");
		solo.enterText(inputPassword, "soloInputPassword");
		solo.clickOnButton("Register");
		solo.assertCurrentActivity("should launch DashboardActivity", DashboardActivity.class);		
	}
	
	 // previsouly passed GUI tests
	public void testRegisterActivityClassNotNull() {
	   assertNotNull(activityWeTest);
	}
	
	public void testButtons(){
		// register button
		btnRegister = (Button) activityWeTest.findViewById(R.id.btnRegister);
		assertNotNull(btnRegister);
		assertEquals("Register", btnRegister.getText());
		
		// back to login button
		btnLinkToLogin = (Button) activityWeTest.findViewById(R.id.btnLinkToLoginScreen);
		assertNotNull(btnLinkToLogin);
		assertEquals("Already registred. Login Me!", btnLinkToLogin.getText());
	}	
	
	public void testEditTexts(){
		inputFullName = (EditText) activityWeTest.findViewById(R.id.registerName);
		assertNotNull(inputFullName);
		
		inputEmail = (EditText) activityWeTest.findViewById(R.id.registerEmail);
		assertNotNull(inputEmail);
		
		inputPassword = (EditText) activityWeTest.findViewById(R.id.registerPassword);
		assertNotNull(inputPassword);
	}
	
	public void testErrorMsgTextView(){
		registerErrorMsg = (TextView) activityWeTest.findViewById(R.id.register_error);
		assertNotNull(registerErrorMsg);
	}	
	
}
