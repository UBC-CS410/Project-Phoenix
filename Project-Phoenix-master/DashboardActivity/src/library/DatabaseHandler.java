package library;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "android_api";
 
    // Login table name
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_IMAGE = "photos";//************
    //comment
    private static final String TABLE_COMMENT="comment";
    
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    //************************************************************8
    
    // Image Table Columns names
    private static final String IMAGE_ID = "photo_uid";
    private static final String IMAGE_TBL_EMAIL = "email";
    private static final String IMAGE_TBL_URL = "photo_ref";
    
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //Comment table 
    private static final String CID="cid";
    private static final String IMAGE_URL="photoref";
    private static final String USER_ID="email";
    private static final String COMMENTS="comment";
    
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        //*************************************************
        /*
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
        		+ IMAGE_ID + " INTEGER PRIMARY KEY,"
                + IMAGE_TBL_EMAIL + " TEXT UNIQUE,"
                + IMAGE_TBL_URL + " TEXT," + ")";
        db.execSQL(CREATE_IMAGE_TABLE);
        */
        
      //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //comment table here
        /*String CREATE_COMMENT_TABLE="CREATE TABLE "+TABLE_COMMENT+"("
        		+CID+" INTEGER PRIMARY KEY,"
        		+IMAGE_URL+" TEXT,"
        		+USER_ID+" TEXT,"
        		+COMMENTS+" TEXT,"+")";
        db.execSQL(CREATE_COMMENT_TABLE);
        */
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
 
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }
    
    //*********************************************************************88
  
    public void addImage(String email, String id, String url){
    	SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(IMAGE_TBL_EMAIL, email); // user eamil
    	values.put(IMAGE_TBL_URL, url); // image url
    	
    	db.insert(TABLE_IMAGE, null, values);
        db.close(); // Closing database connection
    }
 
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // function comment here
    
    public void addcomment(String url,String email,String comment)
    {
    	SQLiteDatabase db= this.getWritableDatabase();
    	ContentValues values=new ContentValues();
    	values.put(IMAGE_URL, url);
    	values.put(USER_ID, email);
    	values.put(COMMENTS, comment);
    	db.insert(TABLE_COMMENT, null, values);
    	db.close();
    }
    
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
 
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
 
        // return row count
        return rowCount;
    }
 
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
 
}
