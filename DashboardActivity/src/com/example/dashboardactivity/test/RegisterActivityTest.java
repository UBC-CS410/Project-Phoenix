package com.example.dashboardactivity.test;

import com.example.dashboardactivity.R;
import com.example.dashboardactivity.RegisterActivity;
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
	
	public RegisterActivityTest(){
		super(RegisterActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();		
	}
	
	public void testRegisterActivityClassNotNull() {
	   assertNotNull(activityWeTest);
	}
	
	public void testLoginButton(){
		btnRegister = (Button) activityWeTest.findViewById(R.id.btnRegister);
		assertNotNull(btnRegister);
		assertEquals("Register", btnRegister.getText());
	}
	
	public void testLinkToLoginButton(){
		btnLinkToLogin = (Button) activityWeTest.findViewById(R.id.btnLinkToLoginScreen);
		assertNotNull(btnLinkToLogin);
		assertEquals("Already registred. Login Me!", btnLinkToLogin.getText());
	}
	
	
	
	public void testNameEditTextNotNull(){
		inputFullName = (EditText) activityWeTest.findViewById(R.id.registerName);
		assertNotNull(inputFullName);
	}
	
	
	public void testEmailEditTextNotNull(){
		inputEmail = (EditText) activityWeTest.findViewById(R.id.registerEmail);
		assertNotNull(inputEmail);
	}
	
	public void testPasswordEditTextNotNull(){
		inputPassword = (EditText) activityWeTest.findViewById(R.id.registerPassword);
		assertNotNull(inputPassword);
	}	
	
	
	public void testErrorMsgTextViewNotNull(){
		registerErrorMsg = (TextView) activityWeTest.findViewById(R.id.register_error);
		assertNotNull(registerErrorMsg);
	}	
	
}
