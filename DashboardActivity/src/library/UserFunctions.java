package library;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
import android.content.Context;
 
public class UserFunctions {
 
    private JSONParser jsonParser;
 
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://70.79.75.130:3721/";
    private static String registerURL = "http://70.79.75.130:3721/";
    private static String imageURL = "http://70.79.75.130:3721/"; //http://70.79.75.130:3721/storePhoto.php
 
    //twitter comment function
    private static String tcommentURL="http://70.79.75.130:3721/tcomment.php";
    private static String tgetcommentURL="http://70.79.75.130:3721/tgetcomment.php";
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private static String get_all_friends = "http://70.79.75.130:3721/test/get_all_products.php";
    
    //@#@#@#@#@#@
    private static String storeTweets = "http://70.79.75.130:3721/tstat.php";
    private static String getStoredTweets = "http://70.79.75.130:3721/getTweets.php";
    
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String image_tag = "storeImage";
 
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
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
 
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
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
    
    //########################################
    //comment on tweet
    public JSONObject tcomment(long twid,long twuserid,String comment)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("twid", ""+twid));
        params.add(new BasicNameValuePair("twuserid", ""+twuserid));
        params.add(new BasicNameValuePair("comment", comment));
        
    	JSONObject json = jsonParser.getJSONFromUrl(tcommentURL, params);
    	return json;
    }
    
    // get comment on tweet
    public JSONObject tgetcomment(long twid)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("twid",""+twid));
    	JSONObject json=jsonParser.getJSONFromUrl(tgetcommentURL,params);
		return json;
    	
    }
    
    //########################
    //store tweets to database
    public JSONObject storeTweets(String status, long tuser, long tid, float lat, float lon){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.print(status);
    	System.out.print(tuser);
    	System.out.print(tid);
    	System.out.print(lat + " "+ lon);
    	
    	params.add(new BasicNameValuePair("stat", status));
        params.add(new BasicNameValuePair("tuser", ""+tuser));
        params.add(new BasicNameValuePair("tid", ""+tid));
        params.add(new BasicNameValuePair("lat", ""+lat));
        params.add(new BasicNameValuePair("lon", ""+lon));
    	JSONObject json = jsonParser.getJSONFromUrl(storeTweets, params);
    	return json;
    }
    
    //get all status form database
    public JSONObject getAllStatus(){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	JSONObject json = jsonParser.getJSONFromUrl(getStoredTweets,params);
    	System.out.println(json);
		return json;   	
    }
    
    // get friend in our DB
    public JSONObject getAllFriends(long currUID){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("twitterID",""+currUID));
    	JSONObject json = jsonParser.getJSONFromUrl(get_all_friends,params);
		return json;   	
    }
    
    
    
    
    //*************************************************************************************
    public JSONObject getImageUrl(String email, String url){
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", image_tag));
    	params.add(new BasicNameValuePair("email", email));
    	params.add(new BasicNameValuePair("photo_ref", url));
    	
    	
    	JSONObject json = jsonParser.getJSONFromUrl(imageURL, params);
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
