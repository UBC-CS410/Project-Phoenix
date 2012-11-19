package com.example.dashboardactivity.test;

import com.example.dashboardactivity.LoginActivity;
import com.example.dashboardactivity.R;

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
	
	public LoginActivityTest(){
		super(LoginActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();		
		//btnLogin = (Button) activityWeTest.findViewById(R.id.btnLogin);
		//btnLinkToRegister = (Button) activityWeTest.findViewById(R.id.btnLinkToRegisterScreen);
		//inputPassword = (EditText) activityWeTest.findViewById(R.id.loginPassword);
		//inputEmail = (EditText) activityWeTest.findViewById(R.id.loginEmail);
		//loginErrorMsg = (TextView) activityWeTest.findViewById(R.id.login_error);
	}
	
	public void testLoginActivityClassNotNull() {
	   assertNotNull(activityWeTest);
	}
	
	public void testLoginButton(){
		btnLogin = (Button) activityWeTest.findViewById(R.id.btnLogin);
		assertNotNull(btnLogin);
		assertEquals("Login", btnLogin.getText());
	}
	
	public void testRegisterButton(){
		btnLinkToRegister = (Button) activityWeTest.findViewById(R.id.btnLinkToRegisterScreen);
		assertNotNull(btnLinkToRegister);
		assertEquals("I don't have account. Register Me!", btnLinkToRegister.getText());
	}
	
	public void testEmailEditTextNotNull(){
		inputEmail = (EditText) activityWeTest.findViewById(R.id.loginEmail);
		assertNotNull(inputEmail);
	}
	
	public void testPasswordEditTextNotNull(){
		inputPassword = (EditText) activityWeTest.findViewById(R.id.loginPassword);
		assertNotNull(inputPassword);
	}
	
	public void testErrorMsgTextViewNotNull(){
		loginErrorMsg = (TextView) activityWeTest.findViewById(R.id.login_error);
		assertNotNull(loginErrorMsg);
	}	
	
	
}
