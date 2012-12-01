package library;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
import android.content.Context;
 
public class UserFunctions {
 
    private JSONParser jsonParser;
 
    // DB urls
    private static String loginURL = "http://70.79.75.130:3721/";
    private static String registerURL = "http://70.79.75.130:3721/";
 
    //twitter comment function
    private static String tcommentURL="http://70.79.75.130:3721/tcomment.php";
    private static String tgetcommentURL="http://70.79.75.130:3721/tgetcomment.php";
    private static String get_all_friends = "http://70.79.75.130:3721/friend/get_all_friends.php";
    
    private static String storeTweets = "http://70.79.75.130:3721/tstat.php";
    private static String getStoredTweets = "http://70.79.75.130:3721/getTweets.php";    
    private static String updateTweets = "http://70.79.75.130:3721/updateStat.php";    
    private static String getNewestTweets = "http://70.79.75.130:3721/getStatus.php";
        
    private static String login_tag = "login";
    private static String register_tag = "register";
 
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
 
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
       
        return json;
    }
 
    /**
     * function make Login Request
     * @param name user name
     * @param email email address
     * @param password password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
    
    /**
     * function comment on tweet
     * @param twid tweetID
     * @param twuserid user's twitter ID
     * @param comment user's written comment
     * */
    public JSONObject tcomment(long twid,long twuserid,String comment)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("twid", ""+twid));
        params.add(new BasicNameValuePair("twuserid", ""+twuserid));
        params.add(new BasicNameValuePair("comment", comment));
        
    	JSONObject json = jsonParser.getJSONFromUrl(tcommentURL, params);
    	return json;
    }
    
    /**
     * function get comment on tweet
     * @param twid tweetID
     * */
    public JSONObject tgetcomment(long twid)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("twid",""+twid));
    	JSONObject json=jsonParser.getJSONFromUrl(tgetcommentURL,params);
		return json;
    	
    }
    
    /**
     * function store tweet to database
     * @param status status that user writes
     * @param tuser user's twitter ID
     * @param tid tweetID
     * @param lat latitude
     * @param lon lontitude
     * */
    public JSONObject storeTweets(String status, long tuser, long tid, double lat, double lon){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair("stat", status));
        params.add(new BasicNameValuePair("tuser", ""+tuser));
        params.add(new BasicNameValuePair("tid", ""+tid));
        params.add(new BasicNameValuePair("lat", ""+lat));
        params.add(new BasicNameValuePair("lon", ""+lon));
    	JSONObject json = jsonParser.getJSONFromUrl(storeTweets, params);
    	return json;
    }
    
    /**
     * function update the newest tweet for a user in database
     * @param status status that user writes
     * @param tuser user's twitter ID
     * @param tid tweetID
     * @param lat latitude
     * @param lon lontitude
     * @param imageUrl user's profile image url
     * */
    public JSONObject updateTweets(String status, long tuser, long tid, double lat, double lon, String imageUrl){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair("stat", status));
        params.add(new BasicNameValuePair("tuser", ""+tuser));
        params.add(new BasicNameValuePair("tid", ""+tid));
        params.add(new BasicNameValuePair("lat", ""+lat));
        params.add(new BasicNameValuePair("lon", ""+lon));
        params.add(new BasicNameValuePair("imgurl", imageUrl)); // newly added field
        		
    	JSONObject json = jsonParser.getJSONFromUrl(updateTweets, params);
    	return json;
    }
    
    /**
     * function for every Ever Friend user, get his/her newest status
     * */
    public JSONObject getNewestStatus(){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	JSONObject json = jsonParser.getJSONFromUrl(getNewestTweets,params);
    	
    	return json;
    }
    
    
    /**
     * function get all status from database
     * */
    public JSONObject getAllStatus(){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	JSONObject json = jsonParser.getJSONFromUrl(getStoredTweets,params);
		return json;   	
    }
    
    // get friend in our DB

    /**
     * function for a user, get all his/her friends
     * @param currUID current user's twitterID
     * */
    public JSONObject getAllFriends(long currUID){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("twitterID",""+currUID));
    	JSONObject json = jsonParser.getJSONFromUrl(get_all_friends,params);
		return json;   	
    }
    
 
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
 
}
