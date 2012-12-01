<?php
 
class DB_Functions {
 
    private $db;
 
    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new DB_Connect();
        $db->connect();
    }
 
    // destructor
    function __destruct() {
 
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        if ($result) {
            // get user details
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM users WHERE uid = $uid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
 
   
    


    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
        if(get_magic_quotes_gpc())
        {
            $email=stripslashes($email);
        }
        $email=mysql_real_escape_string($email);

        $result = mysql_query("SELECT * FROM users WHERE email = '$email'") or die(mysql_error());

            $no_of_rows = mysql_num_rows($result);
            if ($no_of_rows > 0) {
                $result = mysql_fetch_array($result);
                $salt = $result['salt'];
                $encrypted_password = $result['encrypted_password'];
                $hash = $this->checkhashSSHA($salt, $password);
                // check for password equality
                if ($encrypted_password == $hash) {
                    // user authentication details are correct
                    return $result;
                }
            } 
            else {
                // user not found
                return false;
            }
         
        
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
    
    public function storePhoto($email, $photo_ref){
        $response = array();
 
        // check for required fields
        if (isset($_POST['email']) && isset($_POST['phoot_ref'])) {
         // include db connect class
            require_once __DIR__ . '/db_connect.php';
 
         // connecting to db
            $db = new DB_CONNECT();
 
        // mysql inserting a new row
            $result = mysql_query("INSERT INTO photos(email, photo_ref) VALUES('$email', '$photo_ref')");
         // check if row inserted or not
            if ($result) {
                // successfully inserted into database
                $response["success"] = 1;
                $response["message"] = "Product successfully created.";
                // echoing JSON response
                echo json_encode($response);
            } else {
                // failed to insert row
                $response["success"] = 0;
                $response["message"] = "Oops! An error occurred.";
                // echoing JSON response
                echo json_encode($response);
            }
        } else {
            // required field is missing
            $response["success"] = 0;
            $response["message"] = "Required field(s) is missing";
 
            // echoing JSON response
            echo json_encode($response);
        }
    }

}
 
?>