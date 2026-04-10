<?php
session_start(); // Start session 

$error = "";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    
    $conn = new mysqli("localhost", "root", "", "webPAdb");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    
    $username = $conn->real_escape_string($_POST['username']);
    $password = $_POST['password'];


    $sql = "SELECT * FROM UserAccount WHERE Username='$username'";
    $result = $conn->query($sql);

    if ($result->num_rows == 1) {
        $row = $result->fetch_assoc();

        //password_verify($password, $row['Password'])
        if ($password === $row['Password']) {
          
            $_SESSION['username'] = $row['Username'];
            $_SESSION['fullname'] = $row['FullName'];

            header("Location: PawFectMatch.php");
            exit();
        } else {
            $error = "Incorrect password!";
        }
    } else {
        $error = "User not found!";
    }

    $conn->close();
}
?>



<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title> User Login </title>
        <link rel="stylesheet" href="UserLogin.css">
        
    </head>

    <body>
        <div class="card">
            <h2> Welcome Back </h2> 
            <p> Login to your account </p>

            <form action="UserLogin.php" method="POST"> 
                <label> Username </label>
                <input type="text" name="username" required>

                <label> Password </label>
                <input type="password" name="password" required>

                <input type="submit" value="Login">
            </form>  

            <!-- error message -->
            <?php if (!empty($error)): ?>
                <div style="color:red; font-weight:bold; margin-top:10px;">
                    <?php echo $error; ?>
                </div>
            <?php endif; ?>
            

            <div class="signup">
                Don’t have an account? <a href="SignUp.php"> Sign Up </a>
            </div>
        </div>
    </body>
</html>
 







































































<!--<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title> User Login </title>
        <link rel="stylesheet" href="UserLogin.css">
        <script src="UserLogin.js" defer> </script>
    </head>
    <body>

    <div class="card">
    <span class="close-btn" onclick="window.location.href='index.html'"> &times;</span>
    <h2>Welcome Back</h2>
    <p>Login to your account</p>

    <form action="login.php" method="POST">
        <label>Username</label>
        <input type="text" name="username" required>

        <label>Password</label>
        <input type="password" name="password" required>

        <input type="submit" value="Login">
    </form>

    <div class="signup">
        Don’t have an account? <a href="register.html">Sign up</a>
    </div>
</div>

     </body>
     
</html> -->



