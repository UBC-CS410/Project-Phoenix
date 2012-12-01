 <?php
 require_once 'config.php';
 $mysqli = new mysqli(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);


 		//$email='123@123';
         $stmt=$mysqli->prepare('SELECT salt FROM users WHERE email = ?')
         
            //return $result;
            //$result;
            //$stmt->bind_param('s', $email);
            //$stmt->execute();
            //$stmt->bind_result($salt);
            echo "hi";
?>
            