package com.example.dashboardactivity;

import library.AlertDialogManager;
import library.ConnectionDetector;
import library.DatabaseHandler;
import library.UserFunctions;
import library.ExtendedImageDownloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.dashboardactivity.ImageGridActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.example.dashboardactivity.ImageSource.Extra;


import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	// Constants
	/**
	 * Register your here app https://dev.twitter.com/apps/new and get your
	 * consumer key and secret
	 * */
	static String TWITTER_CONSUMER_KEY = "pS8Wj0Oce42kGeP9xLO2VA"; // steve shen's cosumer key
	static String TWITTER_CONSUMER_SECRET = "miKSDaBfLD7ddNxsrvV4dRD5oh4EgIADSCMub6qY"; // steve shen's consumer secret

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "169188831-rfbO6tzDxCr1nrH2zOa0qp4kqKmFoEC6e3NyJiAS"; // my oa_token// was oauth_token
	static final String PREF_KEY_OAUTH_SECRET = "68SonBIUn0TCAkGSas8CVZlXSN2xeZXXTHoI7rSBXA"; // my o_t_s// was oauth_token_secret
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	//*****************************************************
	// JSON Response node names
    private static String IMAGE_SUCCESS = "success";
    private static String IMAGE_ERROR = "error";
    private static String IMAGE_ERROR_MSG = "error_msg";
    private static String IMAGE_URL = "photo_ref";
    private static String IMAGE_EMAIL = "email";
    //private static String KEY_CREATED_AT = "created_at";
	
    private ArrayList<String> imageList = new ArrayList<String>();
    private ArrayList<String> tweetList = new ArrayList<String>();
    private ArrayList<String> peopleList = new ArrayList<String>();
    private ArrayList<Long> peopleIdList = new ArrayList<Long>();
    
    
    private TabHost tabHost;

	// Login button
	Button btnLoginTwitter;
	// Update status button
	Button btnUpdateStatus;
	// Logout button
	Button btnLogoutTwitter;
	
	// Show profile image button   @#@#
	Button btnProfileImage;
	//Button btnBackToCenter;
	Button btnGetTweets;
	Button btnSearchPeople;
	
	ListView tweetListView;  // @#@#
	ListView peopleListView;
	
	// EditText for update
	EditText txtUpdate;
	EditText txtSearchPeople;
	// lbl update
	TextView lblUpdate;
	TextView lblUserName;

	// Progress dialog
	ProgressDialog pDialog;

	// Twitter
	private static Twitter twitter;
	private static RequestToken requestToken;
	
	
	//@#@#
	IDs ids;
	IDs ids2;
	ArrayList<IDs> followerIds = new ArrayList<IDs>();
	
	// Shared Preferences
	private static SharedPreferences mSharedPreferences;
	
	// Internet Connection detector
	private ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		
		// Check if twitter keys are set
		if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
			// stop executing code by return
			return;
		}

		// All UI elements
		//****************************************************************************************************
		// Create steve shen's tabs
		tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();
		//LayoutInflater.from(this).inflate(R.layout.activity_main, tabHost.getTabContentView(), true);
		
		// Add steve shen's tabs
		TabSpec spec1=tabHost.newTabSpec("Tab 1");
		spec1.setContent(R.id.tab1_Main);
		spec1.setIndicator("Main");
		
		TabSpec spec2=tabHost.newTabSpec("Tab 2");
		spec2.setContent(R.id.tab2_Image);
		spec2.setIndicator("Image");
		
		TabSpec spec3=tabHost.newTabSpec("Tab 3");
		spec3.setContent(R.id.tab3_Tweets);
		spec3.setIndicator("Tweets");
		
		TabSpec spec4=tabHost.newTabSpec("Tab 4");
		spec4.setContent(R.id.tab4_Pepple);
		spec4.setIndicator("People");		
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		tabHost.addTab(spec4);
		
		//****************************************************************************************************
		
		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
		btnUpdateStatus = (Button) findViewById(R.id.btnUpdateStatus);
		
		btnProfileImage = (Button) findViewById(R.id.btnProfileImage);  // @#@#
		//btnBackToCenter = (Button) findViewById(R.id.btnBackToCenter);
		btnGetTweets = (Button) findViewById(R.id.btnGetTweets);
		btnSearchPeople = (Button) findViewById(R.id.btnSearchPeople);
		
		tweetListView = (ListView) findViewById(R.id.mylist);  // @#@#
		peopleListView = (ListView) findViewById(R.id.mylist2);
		
		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
		txtUpdate = (EditText) findViewById(R.id.txtUpdateStatus);
		txtSearchPeople = (EditText) findViewById(R.id.txtSearchPeople); //@#@#
		lblUpdate = (TextView) findViewById(R.id.lblUpdate);
		lblUserName = (TextView) findViewById(R.id.lblUserName);		
		

		// Shared Preferences
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		

		/**
		 * Twitter login button click event will call loginToTwitter() function
		 * */
		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

			//@Override
			public void onClick(View arg0) {
				// Call login twitter function
				loginToTwitter();
			}
		});

		/**
		 * Button click event to Update Status, will call updateTwitterStatus()
		 * function
		 * */
		btnUpdateStatus.setOnClickListener(new View.OnClickListener() {

			//@Override
			public void onClick(View v) {
				// Call update status function
				// Get the status from EditText
				String status = txtUpdate.getText().toString();

				// Check for blank text
				if (status.trim().length() > 0) {
					// update status
					new updateTwitterStatus().execute(status);
				} else {
					// EditText is empty
					Toast.makeText(getApplicationContext(),
							"Please enter status message", Toast.LENGTH_SHORT).show();
				}
			}
		});		
		

		/**
		 * Button click event for logout from twitter
		 * */
		btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {

			//@Override
			public void onClick(View arg0) {
				// Call logout twitter function
				logoutFromTwitter();
			}
		});
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
	    .threadPriority(Thread.NORM_PRIORITY - 2)
	    .memoryCacheSize(2 * 1024 * 1024) // 2 Mb
	    .denyCacheImageMultipleSizesInMemory()
	    .discCacheFileNameGenerator(new Md5FileNameGenerator())
	    .imageDownloader(new ExtendedImageDownloader(getApplicationContext()))
	    .tasksProcessingOrder(QueueProcessingType.LIFO)
	    .enableLogging() // Not necessary in common
	    .build();
    
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	  
	  
		//@#@#@
		
		peopleListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				showPopupMenu(view, position);	
			}
			
		});
		
		btnProfileImage.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				//showProfileImage();
				
				startActivity(new Intent(getApplicationContext(), ImageGridActivity.class).putExtra(Extra.IMAGES, showProfileImage()));
				
				//Intent intent = new Intent(getApplicationContext(), ImageGridActivity.class);
			    //dashboardPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    //intent.putExtra(Extra.IMAGES, showProfileImage());
			    //startActivity(intent);
			    //finish();				
			}
			
		});
		
		
		btnGetTweets.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				getRecentTweets();
			}
			
		});
		
		btnSearchPeople.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				String name = txtSearchPeople.getText().toString();
				
				// Check for blank text
				if (name.trim().length() > 0) {
					// update status
					searchPeople(name);
				} else {
					// EditText is empty
					Toast.makeText(getApplicationContext(),
							"Please enter a name", Toast.LENGTH_SHORT).show();
				}				
			}
			
		});
	/*	
		btnBackToCenter.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				Intent dashboardPage = new Intent(getApplicationContext(), DashboardActivity.class);
				dashboardPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(dashboardPage);
				finish();
			}
			
		});*/
		
		
		//@#@#

		/** This if conditions is tested once is
		 * redirected from twitter page. Parse the uri to get oAuth
		 * Verifier
		 * */
		if (!isTwitterLoggedInAlready()) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
				// oAuth verifier
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				try {
					// Get the access token
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);
					//*************************************************
					
					
					//*************************************************
					// Shared Preferences
					Editor e = mSharedPreferences.edit();

					// After getting access token, access token secret
					// store them in application preferences
					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());
					// Store login status - true
					e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
					e.commit(); // save changes

					Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

					// Hide login button
					btnLoginTwitter.setVisibility(View.GONE);
					//btnBackToCenter.setVisibility(View.GONE);  //@#@#

					// Show Update Twitter
					lblUpdate.setVisibility(View.VISIBLE);
					txtUpdate.setVisibility(View.VISIBLE);
					txtSearchPeople.setVisibility(View.VISIBLE);// @#@#
					btnUpdateStatus.setVisibility(View.VISIBLE);
					
					btnProfileImage.setVisibility(View.VISIBLE);
					btnLogoutTwitter.setVisibility(View.VISIBLE);
					btnGetTweets.setVisibility(View.VISIBLE);
					btnSearchPeople.setVisibility(View.VISIBLE);
					
					
					// Getting user details from twitter
					// For now i am getting his name only
					long userID = accessToken.getUserId();
					User user = twitter.showUser(userID);
					String username = user.getName();
					
					// Displaying in xml ui
					lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));
				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}

	}

	/**
	 * Function to login twitter
	 * */
	private void loginToTwitter() {
		// Check if already logged in
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();
			
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter
						.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(requestToken.getAuthenticationURL())));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {
			// user already logged into twitter
			Toast.makeText(getApplicationContext(),
					"Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Function to update status
	 * */
	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
				
				// Access Token 
				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
				
				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
				
				// Update status
				twitter4j.Status response = twitter.updateStatus(status);
				
				Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				//@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
					// Clearing EditText field
					txtUpdate.setText("");
				}
			});
		}

	}

	/**
	 * Function to logout from twitter
	 * It will just clear the application shared preferences
	 * */
	private void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();
		
		//@#@#
		/*Intent twitterPage = new Intent(getApplicationContext(), MainActivity.class);
		twitterPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(twitterPage);
		finish();*/
		//@#@#@
		

		// After this take the appropriate action
		// I am showing the hiding/showing buttons again
		// You might not needed this code
		
		/*
		btnLogoutTwitter.setVisibility(View.GONE);
		btnUpdateStatus.setVisibility(View.GONE);
		txtUpdate.setVisibility(View.GONE);
		txtSearchPeople.setVisibility(View.GONE);
		lblUpdate.setVisibility(View.GONE);
		lblUserName.setText("");
		lblUserName.setVisibility(View.GONE);
		
		btnProfileImage.setVisibility(View.GONE);
		btnGetTweets.setVisibility(View.GONE);
		btnSearchPeople.setVisibility(View.GONE);

		btnLoginTwitter.setVisibility(View.VISIBLE); */
		//btnBackToCenter.setVisibility(View.VISIBLE);
		
		// this line is very important!!, it ends this current activity
		finish();//@#@#
	}

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * */
	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}
	
	private void searchPeople(String name){
		try {
			List<User> listOfUsers = twitter.searchUsers(name, 10);
			
			for (User user : listOfUsers){
				peopleList.add(user.getScreenName());
				peopleIdList.add(user.getId());
			}
			
			String[] simpleArray = new String[peopleList.size()];
			peopleList.toArray(simpleArray);
			
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,simpleArray);
			peopleListView.setAdapter(arrayAdapter);
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	

	private void showPopupMenu(View v, final int position){
		
		PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
	    popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
	    
	    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	    	public boolean onMenuItemClick(MenuItem item) {
				try {
					int id = item.getItemId();
					//System.out.println("This menu item has this order " + id);					
					switch(id)
					{
						case 2131230756: // follow
							twitter.createFriendship(peopleIdList.get(position));
							Toast.makeText(MainActivity.this,item.toString(),Toast.LENGTH_LONG).show();
							break;
						case 2131230757: // unfollow
							twitter.destroyFriendship(peopleIdList.get(position));
							Toast.makeText(MainActivity.this,item.toString(),Toast.LENGTH_LONG).show();
							break;
						case 2131230758: // block
							twitter.createBlock(peopleIdList.get(position));
							Toast.makeText(MainActivity.this,item.toString(),Toast.LENGTH_LONG).show();
							break;
						default:
							Toast.makeText(MainActivity.this,item.toString(),Toast.LENGTH_LONG).show();
					
					}
				} catch (TwitterException e) {					
					e.printStackTrace();
				}				
				
				return true;
			}
	    	
	    });
	    popupMenu.show();
	}
	
	private void getRecentTweets(){
		System.out.println("Get Tweets button is clicked");		
		try {			
			//User user = twitter.verifyCredentials();
			User user = twitter.showUser(twitter.getId());
			List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
            
            for (Status status : statuses) {            	
            	tweetList.add(status.getText());
                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }			
            
            String[] simpleArray = new String[tweetList.size()];
            tweetList.toArray(simpleArray);
            
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,simpleArray);
            tweetListView.setAdapter(arrayAdapter);
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
	}
	
	private String[] showProfileImage(){
		System.out.println("Show image button is clicked");
		try {
			
			//User user = twitter.showUser(twitter.getId());
			//URL url = user.getProfileImageURL();
			//System.out.println(url.toString());			
			
			System.out.println("Listing of  following's ids.");
			// Specify that the image size is original
			twitter4j.ProfileImage.ImageSize imageSize = twitter4j.ProfileImage.ORIGINAL;
			
            //ids = twitter.getFollowersIDs(-1);
            ids2= twitter.getFriendsIDs(-1); // friend = following
                    
            for (long id2 : ids2.getIDs()) {
                    	
            	twitter4j.ProfileImage image = twitter.getProfileImage(Long.toString(id2), imageSize);
            	//System.out.println(image.getURL());
            	imageList.add(image.getURL());
                    	
                //user = twitter.showUser(id2);
                //url = user.getProfileImageURL();
            	
                //System.out.println("The following with this id: "+ id2 + " has his profile pic link below");
                //System.out.println(url.toString());  
                // System.out.println(id);
             }
            
            //Iterator<String> it = imageList.iterator();
            //while(it.hasNext()){
            	//System.out.println("The url is: "+ it.next());
            //}
			
			
			//UserFunctions userFunction = new UserFunctions();
			//JSONObject json = userFunction.getImageUrl("s@s.com", url.toString());
		/*	
			System.out.println("11111");
			try{
				if (json.getString(IMAGE_SUCCESS) != null) {
					System.out.println("22222");
					String res = json.getString(IMAGE_SUCCESS);
					if(Integer.parseInt(res) == 1){
						// add image successful
						// Store image details in SQLite Database
                        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        //JSONObject json_user = json.getJSONObject("user");
					}
					
				}
			}catch (JSONException e) {
                e.printStackTrace();
            }

			*/
			//ids = twitter.getFollowersIDs(-1);
/*			
			twitter4j.ProfileImage.ImageSize imageSize = twitter4j.ProfileImage.ORIGINAL;
			
            ids = twitter.getFollowersIDs(-1);
            ids2= twitter.getFriendsIDs(-1); // friend = following
                    
            for (long id2 : ids2.getIDs()) {
                    	
            	twitter4j.ProfileImage image = twitter.getProfileImage(Long.toString(id2), imageSize);
            	System.out.println(image.getURL());
                //followerIds.add(ids);
                    	
                user = twitter.showUser(id2);
                url = user.getProfileImageURL();
            	
                System.out.println("The following with this id: "+ id2 + " has his profile pic link below");
                System.out.println(url.toString());
            	
                    	
                       // System.out.println(id);
             }
*/		
             /*       
               System.out.println("Listing follower's ids.");   
                
                for (long id : ids.getIDs()) {
                	//followerIds.add(ids);
                	
               	user = twitter.showUser(id);
                url = user.getProfileImageURL();
                System.out.println("The follower with this id: "+ id + "has his profile link below");
                System.out.println(url.toString());
                   // System.out.println(id);
                }*/
			
			//System.out.println(twitter.getFollowersIDs(-1).toString());
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
	   String[] url_arr = new String[imageList.size()];
	    return imageList.toArray(url_arr);
	}

	protected void onResume() {
		super.onResume();
		Log.d("TAG", "-----onResume-----");
	}
	
	protected void onPause(){
		super.onPause();
		Log.d("TAG", "-----onPause-----");
	}
	//******************************************************************************
	
	

}
