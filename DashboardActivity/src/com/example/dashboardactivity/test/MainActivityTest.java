package com.example.dashboardactivity.test;

import com.example.dashboardactivity.MainActivity;
import com.example.dashboardactivity.R;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

	MainActivity activityWeTest;
	Button btnLoginTwitter, btnUpdateStatus, btnLogoutTwitter,btnProfileImage,btnGetTweets,btnSearchPeople;
	ListView tweetListView, peopleListView;
	EditText txtUpdate, txtSearchPeople;
	TextView lblUpdate, lblUserName;
	
	public MainActivityTest(){
		super(MainActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();		
	}
	
	public void testMainActivityNotNull(){
		assertNotNull(activityWeTest);
	}
	
	public void testLoginTwitterButton(){
		btnLoginTwitter = (Button) activityWeTest.findViewById(R.id.btnLoginTwitter);
		assertNotNull(btnLoginTwitter);
		assertEquals("Login with Twitter", btnLoginTwitter.getText());
	}
	
	public void testUpdateStatusButton(){
		btnUpdateStatus = (Button) activityWeTest.findViewById(R.id.btnUpdateStatus);
		assertNotNull(btnUpdateStatus);
		assertEquals("Tweet", btnUpdateStatus.getText());
	}
	
	public void testLogoutTwitterButton(){
		btnLogoutTwitter = (Button) activityWeTest.findViewById(R.id.btnLogoutTwitter);
		assertNotNull(btnLogoutTwitter);
		assertEquals("Logout from Twitter", btnLogoutTwitter.getText());
	}
	
	public void testProfileImageButton(){
		btnProfileImage = (Button) activityWeTest.findViewById(R.id.btnProfileImage);
		assertNotNull(btnProfileImage);
		assertEquals("Show Profile Image", btnProfileImage.getText());
	}
	
	public void testGetTweetsButton(){
		btnGetTweets = (Button) activityWeTest.findViewById(R.id.btnGetTweets);
		assertNotNull(btnGetTweets);
		assertEquals("Get Tweets", btnGetTweets.getText());
	}
	
	public void testSearchPeopleButton(){
		btnSearchPeople = (Button) activityWeTest.findViewById(R.id.btnSearchPeople);
		assertNotNull(btnSearchPeople);
		assertEquals("Search People", btnSearchPeople.getText());
	}
	
	
	public void testTweetListView(){
		tweetListView = (ListView) activityWeTest.findViewById(R.id.mylist);
		assertNotNull(tweetListView);
	}
	
	public void testPeopleListView(){
		peopleListView = (ListView) activityWeTest.findViewById(R.id.mylist2);
		assertNotNull(peopleListView);
	}
	
	
	
	public void testUpdateEditText(){
		txtUpdate = (EditText) activityWeTest.findViewById(R.id.txtUpdateStatus);
		assertNotNull(txtUpdate);
		
	}
	
	public void testSearchPeopleEditText(){
		txtSearchPeople = (EditText) activityWeTest.findViewById(R.id.txtSearchPeople);
		assertNotNull(txtSearchPeople);		
	}
	
	
	public void testUpdateTextView(){
		lblUpdate = (TextView) activityWeTest.findViewById(R.id.lblUpdate);
		assertNotNull(lblUpdate);
		assertEquals("Update Status", lblUpdate.getText());		
	}
	
	public void testUserNameTextView(){
		lblUserName = (TextView) activityWeTest.findViewById(R.id.lblUserName);
		assertNotNull(lblUserName);
	}
}
