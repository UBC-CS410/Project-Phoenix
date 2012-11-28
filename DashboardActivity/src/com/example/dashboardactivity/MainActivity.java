package com.example.dashboardactivity;

import library.AlertDialogManager;
import library.ConnectionDetector;
import library.JSONParserFriend;
import library.UserFunctions;
import library.ExtendedImageDownloader;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
	
	// ArrayLists
    private ArrayList<String> imageList = new ArrayList<String>();
    private ArrayList<Long> friendList = new ArrayList<Long>();
    
    private ArrayList<String> tweetList = new ArrayList<String>();
    private ArrayList<Long> tweetIdList = new ArrayList<Long>();
    private ArrayList<String> comboTweetList = new ArrayList<String>();
    
    private ArrayList<String> peopleList = new ArrayList<String>();
    private ArrayList<Long> peopleIdList = new ArrayList<Long>();
    
    private ArrayList<String> commentList = new ArrayList<String>();
    private ArrayList<Long> commentAuthorIdList = new ArrayList<Long>();
    private ArrayList<String> comboCommentList = new ArrayList<String>();    
    
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ for map 
    private ArrayList<String> statList = new ArrayList<String>();
    private ArrayList<Double> latList = new ArrayList<Double>();
    private ArrayList<Double> lonList = new ArrayList<Double>();
    private ArrayList<String> imgurlList = new ArrayList<String>();
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ for map
    
    private long yourID;
    private long createFriendID;
    private long deleteFriendID;
    private String friendPicUrl;
    private long currentTweetID; // ????
    
    private JSONParserFriend jasonParsonFriend = new JSONParserFriend();
    private static String url_create_friend = "http://70.79.75.130:3721/test/create_product.php";
    private static final String TAG_SUCCESS = "success";    
    private static String url_delete_friend = "http://70.79.75.130:3721/test/delete_product.php";
    
    private TabHost tabHost;    
    protected ImageLoader imageLoader = ImageLoader.getInstance();
	private String[] imageUrls = {};
	private DisplayImageOptions options;

	Button btnLoginTwitter;
	Button btnUpdateStatus;
	Button btnLogoutTwitter; // no use
	
	// Show profile image button 
	Button btnProfileImage;  // no use
	Button btnGetTweets;     // no use
	Button btnShowMap; // map button // no use
	Button btnSendComment; // no use
	
	ImageButton btnSearchPeople;		
	
	// List Views
	ListView tweetListView;
	ListView peopleListView;
	ListView commentListView;
	
	// Image Grid view
	GridView imageGridView;
	
	// EditText for update
	EditText txtUpdate;
	EditText txtSearchPeople;
	EditText txtTweetComment;
	
	// lbl update
	TextView lblUpdate;
	TextView lblUserName;
	
	// location check box
	CheckBox checkboxLocation;

	// Progress dialog
	ProgressDialog pDialog;

	// Twitter
	private static Twitter twitter;
	private static Twitter twitterForTesting;
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
	gpsTracker gps ;  // gps


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
		// Create tabs
		tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();
		
		// Add  tabs
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
		
//		TabSpec spec5=tabHost.newTabSpec("Tab 5");
//		spec5.setContent(R.id.tab5_Maps);
//		spec5.setIndicator("Map");
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		tabHost.addTab(spec4);
		//tabHost.addTab(spec5);
		
		//****************************************************************************************************
		
		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
		btnUpdateStatus = (Button) findViewById(R.id.btnUpdateStatus);
		
		btnProfileImage = (Button) findViewById(R.id.btnProfileImage);  // no use
		btnGetTweets = (Button) findViewById(R.id.btnGetTweets);  // no use
		btnSearchPeople = (ImageButton) findViewById(R.id.btnSearchPeople);
		
		btnShowMap = (Button) findViewById(R.id.btnShowMap); // no use
		
		tweetListView = (ListView) findViewById(R.id.mylist);
		commentListView = (ListView) findViewById(R.id.mylist3);
		peopleListView = (ListView) findViewById(R.id.mylist2);
		imageGridView = (GridView) findViewById(R.id.gridview_test);
		
		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);// no use
		txtUpdate = (EditText) findViewById(R.id.txtUpdateStatus);
		txtSearchPeople = (EditText) findViewById(R.id.txtSearchPeople);
		
		lblUpdate = (TextView) findViewById(R.id.lblUpdate);
		lblUserName = (TextView) findViewById(R.id.lblUserName);		
		
		
		txtTweetComment = (EditText) findViewById(R.id.txtComment); //!!
		
		checkboxLocation = (CheckBox) findViewById(R.id.checkboxlocation);

		// Shared Preferences
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		

		/**
		 * Twitter login button click event will call loginToTwitter() function
		 */
		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

			//@Override
			public void onClick(View arg0) {
				// call this function to log in twitter
				loginToTwitter();
			}
		});

		/**
		 * Button click event to Update Status, will call updateTwitterStatus()
		 * function
		 */
		btnUpdateStatus.setOnClickListener(new View.OnClickListener() {

			//@Override
			public void onClick(View v) {
				// Call update status function
				String status = txtUpdate.getText().toString();

				// Check for blank text
				if (status.trim().length() > 0) {
					// set up gps
					gps = new gpsTracker(MainActivity.this);
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
		 *  set up image loader
		 */
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
	  
	  
		//@#@#@ List view listeners
		
		peopleListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				showPopupMenu(view, position);	
			}
			
		});
		
		tweetListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				showTweetMenu(view, position);	
			}
			
		});
		
		commentListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				showCommentMenu(view, position);
			}
			
		});
	
		
		btnSearchPeople.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				String name = txtSearchPeople.getText().toString();
				
				// Check for blank text
				if (name.trim().length() > 0) {
					// search people by user name
					searchPeople(name);
				} else {
					// EditText is empty
					Toast.makeText(getApplicationContext(),
							"Please enter a name", Toast.LENGTH_SHORT).show();
				}				
			}
			
		});		
		

		/** This if conditions is tested once is
		 * redirected from twitter page. Parse the uri to get oAuth
		 * Verifier
		 */
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
					txtTweetComment.setVisibility(View.VISIBLE); //text field for enter comment for a tweet
					txtSearchPeople.setVisibility(View.VISIBLE);
					btnUpdateStatus.setVisibility(View.VISIBLE);
					
					//btnProfileImage.setVisibility(View.VISIBLE);
					//btnLogoutTwitter.setVisibility(View.VISIBLE);
					//btnGetTweets.setVisibility(View.VISIBLE);
					//btnShowMap.setVisibility(View.VISIBLE);
					btnSearchPeople.setVisibility(View.VISIBLE);
					checkboxLocation.setVisibility(View.VISIBLE);
					
					
					// Getting user details from twitter
					// For now i am getting his name only
					long userID = accessToken.getUserId();
					User user = twitter.showUser(userID);
					String username = user.getName();
					
					yourID = twitter.getId(); // @@@@@@@@@@@@
					
					// Displaying in xml ui
					lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));
				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
		if(isTwitterLoggedInAlready()==true){
			 MenuInflater menuInflater = getMenuInflater();
		        menuInflater.inflate(R.menu.tweetmenu, menu);
		        return true;
		}else{
			return false;
		}
    }
	
	
	/**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {// TODO
        switch (item.getItemId())
        {
        case R.id.menu_showprofileimage:
        	try {
				imageUrls = getAllFriend(twitter.getId());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (TwitterException e) {
				e.printStackTrace();
			}

			options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.stub_image)
				.showImageForEmptyUri(R.drawable.image_for_empty_url)
				.cacheInMemory()
				.cacheOnDisc()
				.build();

			imageGridView.setAdapter(new ImageAdapter());
			imageGridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					showFriendMenu(view, position);
				}
			});
            return true;
 
        case R.id.menu_gettweet:
        	getRecentTweetFromOurDB();
            return true;
 
        case R.id.menu_showmap:
        	 UserFunctions user = new UserFunctions();
	    	 JSONObject json = user.getNewestStatus();
	    	  
	    	  int success;
	  		try {
	  			success = json.getInt("success");
	  			if(success==1){
	  				JSONArray newestStatuses = json.getJSONArray("status");
	  				System.out.println("newestStatuses has size : " + newestStatuses.length());
	  				
	  				for(int i=newestStatuses.length()-1; i>= 0; i--){
	  					
	  					JSONObject c = newestStatuses.getJSONObject(i);
	  						
	  						double lat = Double.valueOf( c.get("lat").toString() );	
	  						double lon = Double.valueOf( c.get("lon").toString() );	
	  						String stat = c.get("stat").toString();
	  						String url = c.get("imgurl").toString();
	  						
	  						statList.add(stat);			  						
	  						latList.add(lat);
	  						lonList.add(lon);
	  						imgurlList.add(url);
	  				}
	  			
	  			}
	  		} catch (JSONException e) {
	  			e.printStackTrace();
	  		}
	  		
	  		String[] statArr = new String[statList.size()]; 
	  		String[] urlArr = new String[imgurlList.size()]; 
	    	 
	    	Intent intent = new Intent(getApplicationContext(), AndroidGoogleMapsActivity.class);
	    	intent.putExtra("stat", statList.toArray(statArr));
	    	intent.putExtra("lat", latList);
	    	intent.putExtra("lon", lonList);
	    	intent.putExtra("imgurl", imgurlList.toArray(urlArr));
	    	
	    	startActivity(intent);
            return true; 
 
        case R.id.menu_logout:
            logoutFromTwitter();
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
 


	/**
	 * Function to login twitter
	 */
	private void loginToTwitter() {
		// Check if already logged in
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();
			
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();
			
			// nextline is for testing 
			setTwitterForTesting(twitter);// THIS METHOD IS FOR UNIT TESTING PURPOSE!

			try {
				requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {
			// user already logged into twitter 
			logoutFromTwitter(); // already login so log our for user
			//Toast.makeText(getApplicationContext(),"Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Function to update status
	 */
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
				
				if(checkboxLocation.isChecked()==true){
					//store to database
					long tuser = twitter.getId();
				    long tid = response.getId();
				    double lat = gps.getLatitude();
				    double lon = gps.getLongitude();
				    UserFunctions userFunction = new UserFunctions();
				    //System.out.println(status+" "+tuser+" "+tid);
				    JSONObject res = userFunction.storeTweets(status, tuser, tid, lat, lon);
				    JSONObject res1 = userFunction.updateTweets(status, tuser, tid, lat, lon,twitter.showUser(tuser).getProfileImageURL().toString() );
					
					Log.d("Status", "> " + response.getText());
				}
				
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
					Toast.makeText(getApplicationContext(),"Status tweeted successfully", Toast.LENGTH_SHORT).show();
					// Clearing EditText field
					txtUpdate.setText("");
				}
			});
		}

	}

	/**
	 * Function to logout from twitter
	 * It will just clear the application shared preferences
	 */
	private void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();
		
		// this line is very important!!, it ends this current activity
		finish();//@#@#
	}

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 */
	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}
	
	
	/**
	 * Get the most recent 20 tweets from our DB user
	 */
	private void getRecentTweetFromOurDB(){
		
		// clear tweet array list every time before use
		tweetList.clear();//@@@@
		tweetIdList.clear(); //$$$$
		comboTweetList.clear();
		
		// get the most recent 20 tweets from our db user
		UserFunctions user = new UserFunctions();
		JSONObject json = user.getAllStatus();
		
		int success;
		try {
			success = json.getInt("success");
			if(success==1){
				JSONArray statuses = json.getJSONArray("status");
				System.out.println("friends has size : " + statuses.length());
				int count=0;
				
				// reverse the order, display the most recent tweet first
				for(int i=statuses.length()-1; i>= 0; i--){
					if (count==20){
						break;
					}
					
					JSONObject c = statuses.getJSONObject(i);
						
					long tid = Long.valueOf( c.get("tid").toString() );					
					String stat = c.get("stat").toString();
					
					tweetList.add(stat);						
					tweetIdList.add(tid);
					
					try {
						comboTweetList.add(twitter.showStatus(tid).getUser().getName() + ": " + stat);
					} catch (TwitterException e) {
						e.printStackTrace();
					}
					
					count++;		
				}
			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// clear the comment list view
		String[] emptyArray = {};
		ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,emptyArray);
		commentListView.setAdapter(emptyAdapter);		
		
		// convert an arraylist to an string array
        String[] simpleArray = new String[comboTweetList.size()];
        comboTweetList.toArray(simpleArray);
        
        // set array adapter and display in list view
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,simpleArray);
        tweetListView.setAdapter(arrayAdapter);
	}
	
	
	/**
	 * Send Comment on a tweet
	 */
	private void sendComment(String comment, long userId, long tweetId){		
		UserFunctions user = new UserFunctions();
		JSONObject json = user.tcomment(tweetId, userId, comment);
	}
	
	/**
	 * Get the profile image of all our db friend for the current user
	 * Return a list of profile image urls
	 */
	private String[] getAllFriend(long currUid){
		imageList.clear();
		friendList.clear();
		
		UserFunctions user = new UserFunctions();
		JSONObject json = user.getAllFriends(currUid);
		
		int success;
		try {
			success = json.getInt("success");
			if(success==1){
				JSONArray friends = json.getJSONArray("friends");
				System.out.println("friends has size : " + friends.length());
				
				for(int i=0; i< friends.length(); i++){
					
					JSONObject c = friends.getJSONObject(i);
					
					try {
						if (twitter.getId() == Long.valueOf( c.get("twitterID").toString() )){							
							long thisfriendId = Long.valueOf( c.get("twitterFriend").toString() );					
							String friendImgUrl = c.get("twitterFriendImg").toString();
							
							imageList.add(friendImgUrl);						
							friendList.add(thisfriendId);
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (TwitterException e) {
						e.printStackTrace();
					}					
				}
			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String[] urls = new String[imageList.size()];
		
		return imageList.toArray(urls);
	}
	
	
	/**
	 * Fetch all comments in our db for a status(tweet)
	 * Return false if there is no comment for this status(tweet)
	 */
	private boolean getComment(long tweetId){
		// 
		commentList.clear();
		commentAuthorIdList.clear();
		comboCommentList.clear();
		
		UserFunctions user=new UserFunctions();
		JSONObject json=user.tgetcomment(tweetId);
		try{
			//get item
			int success =json.getInt("success");
			if(success==1)
			{				
				JSONArray comments=json.getJSONArray("comments");
				System.out.println("comments has size : " + comments.length());
				
				if(comments.length()==0){
					return false;
				}
				
				for(int i=0; i< comments.length(); i++)
				{
					JSONObject c = comments.getJSONObject(i);
					
					long twuserid = Long.valueOf( c.get("twuserid").toString() );
					String comment=c.get("comment").toString();
					System.out.println(twuserid+ " " + comment );
					
					try {
						String comboComment = twitter.showUser(twuserid).getName()
								               + ": " +comment;
						
						commentList.add(comment);// add comment to comment list
						commentAuthorIdList.add(twuserid); // add userId to comment author list
						comboCommentList.add(comboComment);
					} catch (TwitterException e) {
						e.printStackTrace();
					}
					
					
				}
				
				// clear the tweet list view
				String[] emptyArray = {};
				ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,emptyArray);
				tweetListView.setAdapter(emptyAdapter);
				
				// convert an arraylist to an string array
	            String[] simpleArray = new String[comboCommentList.size()];
	            comboCommentList.toArray(simpleArray);
	            
	            // set array adapter and display in list view
	            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,simpleArray);
	            commentListView.setAdapter(arrayAdapter2);// was tweetlistview	            
	            
			}
			else{
				return false;
			}
		}
		catch(JSONException e) { e.printStackTrace(); }
		return true;		
	}
	
	
	/**
	 * search other twitter user by user name
	 */
	private void searchPeople(String name){
		try {
			// get searched user
			List<User> listOfUsers = twitter.searchUsers(name, 10);
			
			// clear user_name arraylist and user_id arraylist every time before use
			peopleList.clear(); // @@@@
			peopleIdList.clear(); // @@@@
			
			// add user name and user id to corresponding arraylist for future use
			for (User user : listOfUsers){
				peopleList.add(user.getScreenName()); // let me try getName(), instead of get ScreenName()
				peopleIdList.add(user.getId());
			}
			
			// convert an arraylist to an string array
			String[] simpleArray = new String[peopleList.size()];
			peopleList.toArray(simpleArray);
			
			// set array adapter and display in list view
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,simpleArray);
			peopleListView.setAdapter(arrayAdapter);
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Show a popup menu after click on a searched user
	 */
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
						case R.id.menu0: // show name
							String name = twitter.showUser(peopleIdList.get(position)).getName();
							Toast.makeText(MainActivity.this,"Name is <" + name+">" ,Toast.LENGTH_LONG).show();
							break;
						case R.id.menu1: // follow
							twitter.createFriendship(peopleIdList.get(position));
							Toast.makeText(MainActivity.this,"You have followed " + peopleList.get(position) ,Toast.LENGTH_LONG).show();
							break;
						case R.id.menu2: // unfollow
							twitter.destroyFriendship(peopleIdList.get(position));
							Toast.makeText(MainActivity.this,"You have unfollowed " + peopleList.get(position),Toast.LENGTH_LONG).show();
							break;
						case R.id.menu3: // block
							twitter.createBlock(peopleIdList.get(position));
							Toast.makeText(MainActivity.this,"You have blocked " + peopleList.get(position),Toast.LENGTH_LONG).show();
							break;
						case R.id.menu4: // add as friend							
							yourID = twitter.getId(); //may not be needed 
							createFriendID = peopleIdList.get(position);							
							twitter4j.ProfileImage.ImageSize imageSize = twitter4j.ProfileImage.BIGGER;							
							twitter4j.ProfileImage image = twitter.getProfileImage(Long.toString(createFriendID), imageSize);
							friendPicUrl = image.getURL();
						    //friendPicUrl = twitter.showUser(createFriendID).getProfileImageURL().toString();
						    new CreateNewFriend().execute();
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
	
	/**
	 * Show a popup menu when click on a profile image
	 */
	private void showFriendMenu(View v, final int position){
		PopupMenu friendMenu = new PopupMenu(MainActivity.this, v);
		friendMenu.getMenuInflater().inflate(R.menu.friendimagemenu , friendMenu.getMenu());
		
		friendMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				//TODO
				int id = item.getItemId();
				switch(id){
					case R.id.friendmenu1:  // delete friend from DB
						deleteFriendID = friendList.get(position);
						//System.out.println("id will be deleted is: "+ deleteFriendID);
						new DeleteFriend().execute();
						break;
					case R.id.friendmenu2:  // item 2
						Toast.makeText(MainActivity.this,"Item 2",Toast.LENGTH_LONG).show();
						break;
					case R.id.friendmenu3:  // item 3
						Toast.makeText(MainActivity.this,"Item 3",Toast.LENGTH_LONG).show();
						break;	
					default:
						Toast.makeText(MainActivity.this,"Nothing",Toast.LENGTH_LONG).show();
						
				}				
				return true;
			}
			
		});
		friendMenu.show();			
	}
	
	private void showCommentMenu(View v, final int position){
		System.out.println("show comment menu?");
		PopupMenu commentMenu = new PopupMenu(MainActivity.this, v);
		commentMenu.getMenuInflater().inflate(R.menu.commentlistmenu, commentMenu.getMenu());
		// TODO !!
		commentMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();
				switch(id){
				case R.id.commentmenu1:	// reply a comment						
					String aReply = txtTweetComment.getText().toString();					
						
						if (aReply.trim().length() > 0) {
							sendComment(aReply, yourID, currentTweetID);
							// refresh the comment list
							getComment(currentTweetID);
							Toast.makeText(MainActivity.this,"Reply Sent",Toast.LENGTH_LONG).show();
							txtTweetComment.setText("");
						} else {
							// EditText is empty
							Toast.makeText(getApplicationContext(),
									"Your reply can not be empty", Toast.LENGTH_SHORT).show();
						}
					break;
				default:
					Toast.makeText(MainActivity.this,"Nothing is clicked",Toast.LENGTH_LONG).show();
				}
				return true;
			}
			
		});
		
		commentMenu.show();
	}
	
	/**
	 * Show a popup menu when click on a recent tweet
	 */
	private void showTweetMenu(View v, final int position){
		PopupMenu tweetMenu = new PopupMenu(MainActivity.this, v);
		tweetMenu.getMenuInflater().inflate(R.menu.tweetlistmenu, tweetMenu.getMenu());
		
		tweetMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();					
				currentTweetID = tweetIdList.get(position);
				long userId;
				switch(id)
				{
					case R.id.tweetmenu1: // Write a Comment
						try {
							String comment = txtTweetComment.getText().toString();
							//tweetId = tweetIdList.get(position);
							userId = twitter.getId();
							
							if (comment.trim().length() > 0) {
								sendComment(comment, userId, currentTweetID);
								Toast.makeText(MainActivity.this,"Comment Sent",Toast.LENGTH_LONG).show();
								txtTweetComment.setText("");
							} else {
								// EditText is empty
								Toast.makeText(getApplicationContext(),
										"Your comment can not be empty", Toast.LENGTH_SHORT).show();
							}
							
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (TwitterException e) {
							e.printStackTrace();
						}							
						break;
					case R.id.tweetmenu2: // Show all Comments
						if (getComment(currentTweetID) == true)
							Toast.makeText(MainActivity.this,"Comments all shown",Toast.LENGTH_LONG).show();
						else
							Toast.makeText(MainActivity.this,"No Comments to show",Toast.LENGTH_LONG).show();
						break;
					default:
						Toast.makeText(MainActivity.this,"Nothing is clicked",Toast.LENGTH_LONG).show();
				}				
				return true;
			}
		});
		tweetMenu.show();
	}

	/**
	 * Get the most recent 20 tweets from other real twitter user
	 */
	private void getRecentTweets(){	
		try {			
			// clear tweet array list every time before use
			tweetList.clear();//@@@@
			tweetIdList.clear(); //$$$$
			comboTweetList.clear();
			
			//User user = twitter.verifyCredentials();
			
			User user = twitter.showUser(twitter.getId());
			List<Status> statuses = twitter.getHomeTimeline();
			//List<Status> statuses = twitter.getUserTimeline();
            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
            
            // add these recent status to corresponding arraylist for future use
            for (Status status : statuses) {            	
            	tweetList.add(status.getText());
            	tweetIdList.add(status.getId());
             
            	comboTweetList.add(status.getUser().getName() + ": " + status.getText() ) ;
            	//System.out.println("pre");
            	if(status.getGeoLocation()!= null){
            		//System.out.println(status.getGeoLocation().getLatitude() + status.getGeoLocation().getLongitude());
            	}else{
            		//System.out.println("No geo info");
            	}
            }			
            
            // convert an arraylist to an string array
            String[] simpleArray = new String[comboTweetList.size()];
            comboTweetList.toArray(simpleArray);
            
            // set array adapter and display in list view
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,simpleArray);
            tweetListView.setAdapter(arrayAdapter);
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * collects follower's profile image urls
	 */
	private String[] showProfileImage(){
		System.out.println("Show image button is clicked");
		try {
			
			imageList.clear();
			//User user = twitter.showUser(twitter.getId());
			//URL url = user.getProfileImageURL();
			//System.out.println(url.toString());	
			
			// Specify that the image size is original
			twitter4j.ProfileImage.ImageSize imageSize = twitter4j.ProfileImage.ORIGINAL;
			
            //ids = twitter.getFollowersIDs(-1);
            ids2= twitter.getFriendsIDs(-1); // friend = following
                    
            for (long id2 : ids2.getIDs()) {                    	
            	twitter4j.ProfileImage image = twitter.getProfileImage(Long.toString(id2), imageSize);
            	//System.out.println(image.getURL());
            	
            	// store these follower's profile pic url into an arraylist
            	imageList.add(image.getURL());
                    	
                //user = twitter.showUser(id2);
                //url = user.getProfileImageURL();
            	
                //System.out.println("The following with this id: "+ id2 + " has his profile pic link below");
                //System.out.println(url.toString());  
             }			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
	   String[] url_arr = new String[imageList.size()];
	    return imageList.toArray(url_arr);
	}
	
	/**
	 * Create a new friend and store a new friend entry into our own db 
	 */
	class CreateNewFriend extends AsyncTask<String, String, String> {
		/**
		 * Show progress dialog before adding a new friend
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Adding as friend..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating friend
		 * */
		protected String doInBackground(String... args) {
			String twitterID = String.valueOf(yourID);
			String twitterFriend = String.valueOf(createFriendID);
			String twitterFriendImg = friendPicUrl;

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("twitterID", twitterID));
			params.add(new BasicNameValuePair("twitterFriend", twitterFriend));
			params.add(new BasicNameValuePair("twitterFriendImg", twitterFriendImg));
			
			System.out.println(params.toString());

			// getting JSON Object
			JSONObject json = jasonParsonFriend.makeHttpRequest(url_create_friend,
					"POST", params);
			
			// check log cat for response
			Log.d("Create Friend Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
//					// successfully create friend
//					Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
//					startActivity(i);
//					
//					// closing this screen
//					finish();
					
				} else {
					// failed to create friend
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			Toast.makeText(MainActivity.this,"Friend Added!",Toast.LENGTH_LONG).show();
		}
		
	}
		
		
	/**
	 * Delete a friend  from our own data base
	 */
	class DeleteFriend extends AsyncTask<String, String, String> {
		// TODO
		/**
		 * Show progress dialog before deleting a friend from our db
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Delete friend..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Delete friend from our db
		 */
		protected String doInBackground(String... args) {
			System.out.println("in doingbackground..");
			String twitterID = String.valueOf(yourID);
			String twitterFriend = String.valueOf(deleteFriendID);
			//String twitterFriendImg = friendPicUrl;

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("twitterID", twitterID));
			params.add(new BasicNameValuePair("twitterFriend", twitterFriend));
			//params.add(new BasicNameValuePair("twitterFriendImg", twitterFriendImg));
			
			System.out.println("..."+params.toString());

			// getting JSON Object
			JSONObject json = jasonParsonFriend.makeHttpRequest(url_delete_friend,
						"POST", params);
			System.out.println("Error here?");// get printed
				
			// check log cat for response
			//Log.d("Delete Friend Response", json.toString());

//			// check for success tag
//			try {
//				int success = json.getInt(TAG_SUCCESS);
//				if (success == 1) {
//					System.out.println("or here?");
////					// successfully delete friend
////					Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
////					startActivity(i);
////					
////					// closing this screen
////					finish();
//						
//				} else {
//					// failed to delete friend
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}

			return null;
		}

		/**
		 * Dismiss the progress dialog after deleting a friend from our db
		 */
		protected void onPostExecute(String file_url) {
			// dismiss the dialog
			pDialog.dismiss();
			Toast.makeText(MainActivity.this,"Friend Deleted!",Toast.LENGTH_LONG).show();
		}
	 }
	
	
	private void setTwitterForTesting(Twitter tw){
		twitterForTesting = tw;
	}
	
	public Twitter getTwitterForTesting(){
		return twitterForTesting;
	}	
	
	
	
	/**
	 * This is a class handling displaying profile image as grid view
	 */
	public class ImageAdapter extends BaseAdapter {
		public int getCount() {
			return imageUrls.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			imageLoader.displayImage(imageUrls[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
					imageView.setAnimation(anim);
					anim.start();
				}
			});

			return imageView;
		}
	}

	protected void onResume() {
		super.onResume();
		Log.d("TAG", "-----onResume-----");
	}
	
	protected void onPause(){
		super.onPause();
		Log.d("TAG", "-----onPause-----");
	}
	
}
