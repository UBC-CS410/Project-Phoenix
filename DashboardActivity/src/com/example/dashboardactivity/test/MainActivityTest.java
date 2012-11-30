package com.example.dashboardactivity.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import library.JSONParserFriend;
import library.UserFunctions;

import com.example.dashboardactivity.DashboardActivity;
import com.example.dashboardactivity.MainActivity;
import com.example.dashboardactivity.R;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

	MainActivity activityWeTest;
	Button btnLoginTwitter, btnUpdateStatus, btnLogoutTwitter,btnProfileImage,btnGetTweets;
	ImageButton btnSearchPeople;
	ListView tweetListView, peopleListView;
	GridView imageGridView;
	EditText txtUpdate, txtSearchPeople,txtTweetComment;
	TextView lblUpdate, lblUserName;	
	CheckBox checkboxLocation;
	
	private Solo solo;
	private Twitter twitter;
	private User user;
	private int num_of_status;
	private IDs ids;
	private long gonna_unfollow_this_friend_id;
	private long userID = 169188831;
	private JSONArray oldFriendsList,newFriendsList;
	private JSONParserFriend jasonParsonFriend = new JSONParserFriend();
	private ArrayList<Long> usersFriendListOld = new ArrayList<Long>();
	private ArrayList<Long> usersFriendListNew = new ArrayList<Long>();
	private ArrayList<String> imageList = new ArrayList<String>();
	private long deleThisfriendId;
	
	
	
	private static String url_delete_friend = "http://70.79.75.130:3721/friend/delete_friend.php";
	
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
	
	// passed
	public void testGetStatusAndCommentAndReply(){
		// login
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		twitter = activityWeTest.getTwitterForTesting();
		long firstTweetID = 0;
			
		// get recent tweets from db user
		solo.clickOnText("Status");
		solo.sleep(3000);
		
		UserFunctions user = new UserFunctions();
		JSONObject json = user.getAllStatus();
		
		int success;
		try{
			success = json.getInt("success");
			if(success==1){
				JSONArray statuses = json.getJSONArray("status");
				JSONObject c = statuses.getJSONObject(0);
				firstTweetID = Long.valueOf( c.get("tid").toString() );	
				
				
				if(statuses.length()>0){
					Assert.assertNotNull(tweetListView);					
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		//---------------------------------
		// Send an empty comment
		solo.enterText( (EditText) solo.getView( R.id.txtComment ), "");
		solo.clickOnScreen(100, 250);
		solo.clickOnText("Send Comment");
		Assert.assertTrue(solo.waitForText("Your comment can not be empty"));	
		
		//---------------------------------
		// send a non empty comment
		String comment = "Keep going2";
		user.tcomment(firstTweetID, userID, comment);		
		
		// check the db		
		JSONObject json2 = user.tgetcomment(firstTweetID);		
		int success2;
		try{
			success2 = json2.getInt("success");
			if(success2==1){
				JSONArray comments = json2.getJSONArray("comments");				
				JSONObject c = comments.getJSONObject(comments.length()-1);
				String newComment=c.get("comment").toString();
				Assert.assertEquals(comment, newComment);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}		
		
		//---------------------------------
		// Reply
		String reply = "I will";
		user.tcomment(firstTweetID, userID, reply);
		
		// check the db		
		JSONObject json3 = user.tgetcomment(firstTweetID);		
		int success3;
		try{
			success3 = json3.getInt("success");
			if(success3==1){
				JSONArray comments = json3.getJSONArray("comments");				
				JSONObject c = comments.getJSONObject(comments.length()-1);
				String newReply=c.get("comment").toString();
				Assert.assertEquals(reply, newReply);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		// log out
		solo.clickOnMenuItem("Log out");
	}
	
	
	
	// passed
	public void testGetEverFriendProfileImages(){
		
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
				
		try {
			twitter = activityWeTest.getTwitterForTesting();	
			userID = twitter.getId();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
		
		
		// check friend list BEFORE deleting a friend
		UserFunctions user = new UserFunctions();
		JSONObject jsonOld = user.getAllFriends(userID);
			
		int success;
		try {
			success = jsonOld.getInt("success");
			if(success==1){
				oldFriendsList = jsonOld.getJSONArray("friends");	
				System.out.println("This user has friend: " + oldFriendsList.length() );	
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		solo.clickOnText("Friend");	
		solo.sleep(3000);		
		
		if(oldFriendsList.length()>0){
			Assert.assertNotNull(imageGridView);
		}else{
			Assert.assertNull(imageGridView);
		}
		
		solo.clickOnMenuItem("Log out");
	}
	
	
	
    // passed
	public void testAddEverFriend(){
		solo.clickOnButton("Login with Twitter");
		solo.sleep(10000);
		
		// setting up
		twitter = activityWeTest.getTwitterForTesting();		
		userID = activityWeTest.getYourID();			
		user = activityWeTest.getCurrentUser();
		//****************************************
		// begin searching friend
		solo.clickOnText("Search");		
		solo.enterText( (EditText) solo.getView( R.id.txtSearchPeople ), "Paul"); 	
		solo.clickOnImageButton(0);
		solo.sleep(7000);
		Assert.assertNotNull(peopleListView);
		
		// check friend list BEFORE adding a friend
		UserFunctions user = new UserFunctions();
		JSONObject jsonOld = user.getAllFriends(userID);
		
		int success;
		try {
			success = jsonOld.getInt("success");
			if(success==1){
				oldFriendsList = jsonOld.getJSONArray("friends");
				System.out.println("friends has size : " + oldFriendsList.length());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// adding a friend		
		solo.clickOnScreen(100, 250);
		solo.clickOnText("Add to Ever Friend");
		solo.sleep(6000);
		
		// check friend list AFTER adding a friend
		JSONObject jsonNew = user.getAllFriends(userID);
		
		int success1;
		try {
			success1 = jsonNew.getInt("success");
			if(success1==1){
				newFriendsList = jsonNew.getJSONArray("friends");
				System.out.println("friends has size : " + newFriendsList.length());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		Assert.assertEquals(newFriendsList.length(), oldFriendsList.length()+1);

		solo.clickOnText("Main");
		solo.clickOnButton("Logout from Twitter");
		solo.assertCurrentActivity("should go to Twitter Login", MainActivity.class);		
	}
	
	
	public void testDeleteEverFriend(){ // passed
		
		// check friend list BEFORE deleting a friend
		UserFunctions user = new UserFunctions();
		JSONObject jsonOld = user.getAllFriends(userID);
			
		int success;
		try {
			success = jsonOld.getInt("success");
			if(success==1){
				oldFriendsList = jsonOld.getJSONArray("friends");	
				System.out.println("All oldfl is: " + oldFriendsList.length() );
				
				JSONObject c = oldFriendsList.getJSONObject(0);
				deleThisfriendId = Long.valueOf( c.get("twitterFriend").toString() );
					
				if (oldFriendsList.length()==0){
					return;
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//-----------------------------------------------------------------
		long deleteID = deleThisfriendId;
						
			// delete this first friend of this user			
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("twitterID", String.valueOf(userID)));
		params.add(new BasicNameValuePair("twitterFriend",String.valueOf(deleteID)));			
		JSONObject json = jasonParsonFriend.makeHttpRequest(url_delete_friend,"POST", params);
		
		//---------------------------------------------------------------------------------------	
			
		// check friend list AFTER delete a friend
		JSONObject jsonNew = user.getAllFriends(userID);		
		int success1;
		try {
			success1 = jsonNew.getInt("success");
			if(success1==1){
				newFriendsList = jsonNew.getJSONArray("friends");
				System.out.println("All newfl is: " + newFriendsList.length() );	
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(newFriendsList.length()>0){
			Assert.assertEquals(oldFriendsList.length()-1, newFriendsList.length());
		}else
			return;
		
		
//		//----------------------------------------------------------
//		
//		JSONObject friendship = user.isEverFriend(userID,deleteID);
//		int success2;
//		try {
//			success2 = friendship.getInt("success");
//			if (success2==1){
//				JSONArray friend = friendship.getJSONArray("friends");
//				System.out.println(friend);
//				//Assert.assertNull(friend);
//				System.out.println("Passed isEverFriend method");	
//			}			
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}		
		
	}
	
}




