package com.example.dashboardactivity.test;

import java.util.List;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
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
	EditText txtUpdate, txtSearchPeople,txtTweetComment;
	TextView lblUpdate, lblUserName;	
	
	private Solo solo;
	private Twitter twitter;
	private User user;
	private int num_of_status;
	private IDs ids;
	private long gonna_unfollow_this_friend_id;
	
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
		txtTweetComment = (EditText) activityWeTest.findViewById(R.id.txtComment);
		tweetListView = (ListView) activityWeTest.findViewById(R.id.mylist);
		peopleListView = (ListView) activityWeTest.findViewById(R.id.mylist2);
		
	}
	

	// passed
	public void testBlockUser(){
		// login
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		//solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);
		twitter = activityWeTest.getTwitterForTesting();	
		
		
		try {
			user = twitter.showUser(twitter.getId()); // this user is me
			
			solo.clickOnText("People");			
			solo.enterText( (EditText) solo.getView( R.id.txtSearchPeople ), "steve"); 		
			solo.clickOnImageButton(0);
			solo.sleep(7000);
			//Assert.assertNotNull(peopleListView);
			//Assert.assertTrue(solo.searchText("steve"));
			
			solo.clickOnScreen(100, 500);
			solo.clickOnText("Block");
			solo.sleep(5000);
			
			
			ResponseList<User> blockedUsers = twitter.getBlockingUsers();			
			System.out.println("Block[0] is: " +blockedUsers.get(0).getScreenName());// yelvington	
			
			ids = twitter.getBlockingUsersIDs();
			long new_block_id = ids.getIDs()[0];
			
			Assert.assertTrue(twitter.existsBlock(new_block_id));
			
			
		} catch (IllegalStateException e) {		
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
	}
	


	// passed
	public void testSearchAndFollowPeople(){
		long new_friend_id;
		
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		//solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);
		twitter = activityWeTest.getTwitterForTesting();
		try {
			user = twitter.showUser(twitter.getId());
		} catch (IllegalStateException e) {		
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		int num_of_friends =  user.getFriendsCount();
		
		solo.clickOnText("People");
		
		solo.enterText( (EditText) solo.getView( R.id.txtSearchPeople ), "steve"); 		
		solo.clickOnImageButton(0);
		solo.sleep(7000);
		Assert.assertNotNull(peopleListView);
		Assert.assertTrue(solo.searchText("steve"));
		
		solo.clickOnScreen(100, 250);
		solo.clickOnText("Follow");
		solo.sleep(4000);
		
		Assert.assertEquals(user.getFriendsCount(), num_of_friends);
		
		try {
			
			ids = twitter.getFriendsIDs(-1);
			//String new_friend_screenName = twitter.showUser(ids.getIDs()[0]).getScreenName();
			new_friend_id = ids.getIDs()[0];						
			//System.out.println("ids[0] is: " + ids.getIDs()[0]);
			//System.out.println("ids[0]'s screen name is: " +  twitter.showUser(ids.getIDs()[0]).getScreenName());				
			Assert.assertTrue(twitter.showFriendship(twitter.getId(), new_friend_id).isSourceFollowingTarget());
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		solo.clickOnText("Main");
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
	}

	// passed
	public void testSearchAndUnfollowPeople(){
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		//solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);
		twitter = activityWeTest.getTwitterForTesting();
		try {
			user = twitter.showUser(twitter.getId());
			ids = twitter.getFriendsIDs(-1);
			gonna_unfollow_this_friend_id = ids.getIDs()[0];
		} catch (IllegalStateException e) {		
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		int num_of_friends =  user.getFriendsCount();
		
		solo.clickOnText("People");		
		solo.enterText( (EditText) solo.getView( R.id.txtSearchPeople ), "steve"); 		
		solo.clickOnImageButton(0);
		solo.sleep(7000);
		Assert.assertNotNull(peopleListView);
		Assert.assertTrue(solo.searchText("steve"));
		
		solo.clickOnScreen(100, 250);
		solo.clickOnText("Unfollow");
		solo.sleep(4000);
		
		Assert.assertEquals(user.getFriendsCount(), num_of_friends);		
		
			
		try {
			Assert.assertFalse(twitter.showFriendship(twitter.getId(), gonna_unfollow_this_friend_id).isSourceFollowingTarget());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
			
		
		
		solo.clickOnText("Main");
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
	}
	
	
	// passed
	public void testTweetAndTweetEmpty(){
		
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		twitter = activityWeTest.getTwitterForTesting();
		try {
			user = twitter.showUser(twitter.getId());
		} catch (IllegalStateException e) {		
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}		
		
		// test tweet an empty message	
		
		solo.clickOnButton("Tweet");
		Assert.assertEquals("",txtUpdate.getText().toString());
		Assert.assertTrue(solo.waitForText("Please enter status message"));		
		
	 
		// test sending a not empty tweet
		num_of_status = user.getStatusesCount();
		String status = "Robotium test tweet 18! Nov 24";
		
		solo.enterText( (EditText) solo.getView( R.id.txtUpdateStatus ), status);
		solo.clickOnButton("Tweet");	
		
		Assert.assertTrue(solo.waitForText("Updating to twitter..."));
		Assert.assertTrue(solo.waitForText("Status tweeted successfully"));
		solo.sleep(10000);
		
		
		try {
			List<Status> statuses = twitter.getUserTimeline();// was .getHomeTimeline()
			Assert.assertEquals(user.getStatusesCount(), num_of_status);			
			Assert.assertEquals(status, statuses.get(0).getText().toString());
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
		Assert.assertEquals(0, btnLoginTwitter.getVisibility());
	}
	
	
		
	// passed
	public void testGetTweets(){
		// login
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		//solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);
		twitter = activityWeTest.getTwitterForTesting();
		try {
			user = twitter.showUser(twitter.getId());
		} catch (IllegalStateException e) {		
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		// get recent tweets
		solo.clickOnText("Tweets");
		solo.clickOnButton("Get Tweets");
		solo.sleep(6000);
		Assert.assertNotNull(tweetListView);
		
		// log out
		solo.clickOnText("Main");
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
	}

/*	
	// passed, merged with tweet test, not used
	public void testTweetEmptyMsg(){
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		solo.assertCurrentActivity("should launch TwitterActivity", MainActivity.class);		
		
		
		// test tweet an empty message	
		
		solo.clickOnButton("Tweet");
		Assert.assertEquals("",txtUpdate.getText().toString());
		Assert.assertTrue(solo.waitForText("Please enter status message"));				
		
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);
		Assert.assertEquals(0, btnLoginTwitter.getVisibility());
	}
*/	
	
	// previously passed tests	
	
	public void testAllGuiComponents(){
		
		assertNotNull(activityWeTest);
		assertNotNull(btnLoginTwitter);
		assertEquals("Login with Twitter", btnLoginTwitter.getText());
		Assert.assertEquals(0, btnLoginTwitter.getVisibility());		
		
		assertNotNull(btnUpdateStatus);
		assertEquals("Tweet", btnUpdateStatus.getText());
		Assert.assertEquals(8, btnUpdateStatus.getVisibility());		
		
		assertNotNull(btnLogoutTwitter);
		assertEquals("Logout from Twitter", btnLogoutTwitter.getText());
		Assert.assertEquals(8, btnLogoutTwitter.getVisibility());		
		
		assertNotNull(btnProfileImage);
		assertEquals("Show Profile Image", btnProfileImage.getText());
		Assert.assertEquals(8, btnProfileImage.getVisibility());		
		
		assertNotNull(btnGetTweets);
		assertEquals("Get Tweets", btnGetTweets.getText());
		Assert.assertEquals(8, btnGetTweets.getVisibility());		
		
		assertNotNull(btnSearchPeople);
		Assert.assertEquals(8, btnSearchPeople.getVisibility());
		
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
	}
	
}




