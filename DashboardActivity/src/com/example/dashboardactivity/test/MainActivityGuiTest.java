package com.example.dashboardactivity.test;

import java.util.ArrayList;

import junit.framework.Assert;
import library.JSONParserFriend;

import org.json.JSONArray;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dashboardactivity.MainActivity;
import com.example.dashboardactivity.R;
import com.jayway.android.robotium.solo.Solo;

public class MainActivityGuiTest extends ActivityInstrumentationTestCase2<MainActivity>{

	MainActivity activityWeTest;
	Button btnLoginTwitter, btnUpdateStatus, btnLogoutTwitter,btnProfileImage,btnGetTweets;
	ImageButton btnSearchPeople;
	ListView tweetListView, peopleListView;
	GridView imageGridView;
	EditText txtUpdate, txtSearchPeople,txtTweetComment;
	TextView lblUpdate, lblUserName;	
	CheckBox checkboxLocation;
	
	Solo solo;
	
	public MainActivityGuiTest(){
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
		
		//btnLogoutTwitter = (Button) activityWeTest.findViewById(R.id.btnLogoutTwitter);
		//btnProfileImage = (Button) activityWeTest.findViewById(R.id.btnProfileImage);
		//btnGetTweets = (Button) activityWeTest.findViewById(R.id.btnGetTweets);
		btnSearchPeople = (ImageButton) activityWeTest.findViewById(R.id.btnSearchPeople);
		
		txtUpdate = (EditText) activityWeTest.findViewById(R.id.txtUpdateStatus);
		txtSearchPeople = (EditText) activityWeTest.findViewById(R.id.txtSearchPeople);
		txtTweetComment = (EditText) activityWeTest.findViewById(R.id.txtComment);
		tweetListView = (ListView) activityWeTest.findViewById(R.id.mylist);
		peopleListView = (ListView) activityWeTest.findViewById(R.id.mylist2);
		imageGridView = (GridView) activityWeTest.findViewById(R.id.gridview_test);
		
		checkboxLocation = (CheckBox) activityWeTest.findViewById(R.id.checkboxlocation);
	}
	
	 //previously passed tests		
	public void testAllGuiComponents(){
		
		assertNotNull(activityWeTest);
		assertNotNull(btnLoginTwitter);
		assertEquals("Login with Twitter", btnLoginTwitter.getText());
		Assert.assertEquals(0, btnLoginTwitter.getVisibility());		
		
		assertNotNull(btnUpdateStatus);
		assertEquals("Tweet", btnUpdateStatus.getText());
		Assert.assertEquals(8, btnUpdateStatus.getVisibility());		
		
//		assertNotNull(btnLogoutTwitter);
//		assertEquals("Logout from Twitter", btnLogoutTwitter.getText());
//		Assert.assertEquals(8, btnLogoutTwitter.getVisibility());		
//		
//		assertNotNull(btnProfileImage);
//		assertEquals("Show Profile Image", btnProfileImage.getText());
//		Assert.assertEquals(8, btnProfileImage.getVisibility());		
//		
//		assertNotNull(btnGetTweets);
//		assertEquals("Get Tweets", btnGetTweets.getText());
//		Assert.assertEquals(8, btnGetTweets.getVisibility());		
//		
//		assertNotNull(btnSearchPeople);
//		Assert.assertEquals(8, btnSearchPeople.getVisibility());
		
		tweetListView = (ListView) activityWeTest.findViewById(R.id.mylist);
		assertNotNull(tweetListView);
		
		peopleListView = (ListView) activityWeTest.findViewById(R.id.mylist2);
		assertNotNull(peopleListView);
		
		txtUpdate = (EditText) activityWeTest.findViewById(R.id.txtUpdateStatus);
		assertNotNull(txtUpdate);
		
		txtSearchPeople = (EditText) activityWeTest.findViewById(R.id.txtSearchPeople);
		assertNotNull(txtSearchPeople);			
		
		txtTweetComment = (EditText) activityWeTest.findViewById(R.id.txtComment);
		assertNotNull(txtTweetComment);	
		
		lblUpdate = (TextView) activityWeTest.findViewById(R.id.lblUpdate);
		assertNotNull(lblUpdate);
		assertEquals("Update Status", lblUpdate.getText());	
		
		lblUserName = (TextView) activityWeTest.findViewById(R.id.lblUserName);
		assertNotNull(lblUserName);
	
		checkboxLocation = (CheckBox) activityWeTest.findViewById(R.id.checkboxlocation);
		assertNotNull(checkboxLocation);
	
	}
}
