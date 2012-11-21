package com.example.dashboardactivity.test;

import junit.framework.Assert;

import com.example.dashboardactivity.DashboardActivity;
import com.example.dashboardactivity.MainActivity;
import com.example.dashboardactivity.R;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

	MainActivity activityWeTest;
	Button btnLoginTwitter, btnUpdateStatus, btnLogoutTwitter,btnProfileImage,btnGetTweets;
	ImageButton btnSearchPeople;
	ListView tweetListView, peopleListView;
	EditText txtUpdate, txtSearchPeople;
	TextView lblUpdate, lblUserName;
	
	private Solo solo;
	
	public MainActivityTest(){
		super(MainActivity.class);
		Log.i("tester 1", "within the constructor");
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		Log.i("tester 2", "within setUp()");
		activityWeTest = this.getActivity();	
		solo = new Solo(getInstrumentation(), getActivity());
		
		btnLoginTwitter = (Button) activityWeTest.findViewById(R.id.btnLoginTwitter);
		btnUpdateStatus = (Button) activityWeTest.findViewById(R.id.btnUpdateStatus);
		btnLogoutTwitter = (Button) activityWeTest.findViewById(R.id.btnLogoutTwitter);
		btnProfileImage = (Button) activityWeTest.findViewById(R.id.btnProfileImage);
		btnGetTweets = (Button) activityWeTest.findViewById(R.id.btnGetTweets);
		btnSearchPeople = (ImageButton) activityWeTest.findViewById(R.id.btnSearchPeople);
		
		txtUpdate = (EditText) activityWeTest.findViewById(R.id.txtUpdateStatus);
		txtSearchPeople = (EditText) activityWeTest.findViewById(R.id.txtSearchPeople);
		tweetListView = (ListView) activityWeTest.findViewById(R.id.mylist);
		peopleListView = (ListView) activityWeTest.findViewById(R.id.mylist2);
		
	}
	
	// passed
		public void testGetTweets(){
			solo.clickOnButton("Login with Twitter");
			solo.sleep(8000);
			solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);
			
			solo.clickOnText("Tweets");
			solo.clickOnButton("Get Tweets");
			solo.sleep(6000);
			Assert.assertNotNull(tweetListView);
			
			solo.clickOnText("Main");
			solo.clickOnButton("Logout from Twitter");
			solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
		}
	
	
	// passed
	public void testSearchPeople(){
		solo.clickOnButton("Login with Twitter");
		solo.sleep(8000);
		solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);		
		
		solo.clickOnText("People");
		
		solo.enterText( (EditText) solo.getView( R.id.txtSearchPeople ), "steve"); 		
		solo.clickOnImageButton(0);
		solo.sleep(7000);
		Assert.assertNotNull(peopleListView);
		Assert.assertTrue(solo.searchText("steve"));
		
		solo.clickOnText("Main");
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
	}
	
	
	// passed
	public void testTweet(){
		solo.clickOnButton("Login with Twitter");
		solo.sleep(8000);
		solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);
				
		// test send a tweet		
		solo.enterText( (EditText) solo.getView( R.id.txtUpdateStatus ), "Robotium test tweet 2 !");
		solo.clickOnButton("Tweet");	
		Assert.assertTrue(solo.waitForText("Updating to twitter..."));
		Assert.assertTrue(solo.waitForText("Status tweeted successfully"));
				
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
		Assert.assertEquals(0, btnLoginTwitter.getVisibility());
	}
		
	// passed
	public void testTweetEmptyMsg(){
		solo.clickOnButton("Login with Twitter");
		solo.sleep(8000);
		solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);		
		
		
		// test tweet an empty message		
		solo.clickOnButton("Tweet");
		Assert.assertTrue(solo.waitForText("Please enter status message"));				
		
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
		Assert.assertEquals(0, btnLoginTwitter.getVisibility());
	}
	
	
	// previously passed tests	
	public void testAMainActivityNotNull(){
		assertNotNull(activityWeTest);
	}
	
	public void testButtons(){
		//btnLoginTwitter = (Button) activityWeTest.findViewById(R.id.btnLoginTwitter);
		assertNotNull(btnLoginTwitter);
		assertEquals("Login with Twitter", btnLoginTwitter.getText());
		Assert.assertEquals(0, btnLoginTwitter.getVisibility());
		
		//btnUpdateStatus = (Button) activityWeTest.findViewById(R.id.btnUpdateStatus);
		assertNotNull(btnUpdateStatus);
		assertEquals("Tweet", btnUpdateStatus.getText());
		Assert.assertEquals(8, btnUpdateStatus.getVisibility());
		
		//btnLogoutTwitter = (Button) activityWeTest.findViewById(R.id.btnLogoutTwitter);
		assertNotNull(btnLogoutTwitter);
		assertEquals("Logout from Twitter", btnLogoutTwitter.getText());
		Assert.assertEquals(8, btnLogoutTwitter.getVisibility());
		
		//btnProfileImage = (Button) activityWeTest.findViewById(R.id.btnProfileImage);
		assertNotNull(btnProfileImage);
		assertEquals("Show Profile Image", btnProfileImage.getText());
		Assert.assertEquals(8, btnProfileImage.getVisibility());
		
		//btnGetTweets = (Button) activityWeTest.findViewById(R.id.btnGetTweets);
		assertNotNull(btnGetTweets);
		assertEquals("Get Tweets", btnGetTweets.getText());
		Assert.assertEquals(8, btnGetTweets.getVisibility());
		
		//btnSearchPeople = (ImageButton) activityWeTest.findViewById(R.id.btnSearchPeople);
		assertNotNull(btnSearchPeople);
		Assert.assertEquals(8, btnSearchPeople.getVisibility());
	}
		
	public void testCListViews(){
		tweetListView = (ListView) activityWeTest.findViewById(R.id.mylist);
		assertNotNull(tweetListView);
		
		peopleListView = (ListView) activityWeTest.findViewById(R.id.mylist2);
		assertNotNull(peopleListView);
	}
	
	public void testCEditTexts(){
		txtUpdate = (EditText) activityWeTest.findViewById(R.id.txtUpdateStatus);
		assertNotNull(txtUpdate);
		
		txtSearchPeople = (EditText) activityWeTest.findViewById(R.id.txtSearchPeople);
		assertNotNull(txtSearchPeople);				
	}

	public void testCTextViews(){
		lblUpdate = (TextView) activityWeTest.findViewById(R.id.lblUpdate);
		assertNotNull(lblUpdate);
		assertEquals("Update Status", lblUpdate.getText());	
		
		lblUserName = (TextView) activityWeTest.findViewById(R.id.lblUserName);
		assertNotNull(lblUserName);
	}
	
}
