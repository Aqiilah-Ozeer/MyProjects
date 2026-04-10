<?php
$servername = "localhost";
$dbUsername = "root";
$dbPassword = "";
$dbname = "webPAdb";

$conn = new mysqli($servername, $dbUsername, $dbPassword, $dbname);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $fullname = trim($_POST['fullname']);
    $email = trim($_POST['email']);
    $username = trim($_POST['username']);
    $password = $_POST['password'];
    $confirm_password = $_POST['confirm_password'];
    $phone = trim($_POST['phone']);
    $address = trim($_POST['address']);

    $errors = array(); 

    // Validate inputs
    if (empty($fullname) || empty($email) || empty($username) || empty($password) || empty($confirm_password) || empty($phone) || empty($address)) {
        $errors[] = "All fields are required.";
    }

    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $errors[] = "Invalid email format.";
    }

    if ($password !== $confirm_password) {
        $errors[] = "Passwords do not match.";
    }

    
    // Password validation: 8–16 characters, at least one letter, one digit
    if (strlen($password) < 8 || strlen($password) > 16) {
        $errors[] = "Password must be 8 to 16 characters long.";
    } elseif (!preg_match('/[a-zA-Z]/', $password)) {
        $errors[] = "Password must contain at least one letter.";
    } elseif (!preg_match('/\d/', $password)) {
        $errors[] = "Password must contain at least one number.";
    }
    

    // Username already exists check
    $stmt = $conn->prepare("SELECT * FROM UserAccount WHERE Username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows > 0) {
        $errors[] = "Username already exists. Please choose another.";
    }
    $stmt->close();

    // password hashing and Insert into database if no errors
    if (count($errors) === 0) {
        //$hashed_password = password_hash($password, PASSWORD_DEFAULT);

        $stmt = $conn->prepare("INSERT INTO UserAccount (Username, Password, FullName, PhoneNumber, EmailAddress, FullAddress) VALUES (?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssss", $username, $password, $fullname, $phone, $email, $address);

        if ($stmt->execute()) {
            
            header("Location: UserLogin.php");
            exit();
        } else {
            $errors[] = "Database error: " . $stmt->error;
        }

        $stmt->close();
    }

    // Display errors
    if (count($errors) > 0) {
        echo "<div class='error' style='color:red; font-weight:bold;'>";
        foreach ($errors as $err) {
            echo $err . "<br>";
        }
        echo "</div>";
    }
}

$conn->close();
?>


<!DOCTYPE html>
<html> 
    <head> 
        <title> Sign Up </title>
        <link rel="stylesheet" href="SignUp.css">

    </head>

    <body> 
        <div class="card">
            <h2> Sign Up! </h2>
            <p> Create your account </p>
            
            <form action="SignUp.php" method="POST">
                
                <label>Full Name </label>
                <input type="text" name="fullname" required>

                <label> Username </label>
                <input type="text" name="username" required>

                <label> Password </label>
                <input type="password" name="password" required>

                <label> Confirm Password </label>
                <input type="password" name="confirm_password" required>

                <label> Phone Number </label>
                <input type="text" name="phone" required>

                <label> Email Address </label>
                <input type="email" name="email" required>

                <label> Full Address </label>
                <textarea name="address" rows="2" required> </textarea>

                <input type="submit" value="Sign Up">
            </form>
            
            <div class="login">
                Already have an account? <a href="UserLogin.php"> Login </a>
            </div>
        </div>

        <script src="SignUp.js"> </script>

    </body>

</html>

