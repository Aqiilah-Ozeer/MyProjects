<?php
$user = $_POST["adminusername"];
$pwd = $_POST["password"];

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "webpadb";

$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn) {
  die("Connection failed: " . mysqli_connect_error());
}

$sql = "SELECT * FROM AdminAccount WHERE Adminusername='$user' AND Password='$pwd'";
$result = mysqli_query($conn, $sql);

session_start();

if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        if ($user == $row['Adminusername'] && $pwd == $row['Password']) {
            
            $_SESSION['adminusername'] = $row['Adminusername'];
            header("Location: AdminDashboard.php");
            exit();
        }
    }
} else {
    header("Location: AdminPawfectmatch.php?error=yes");
}

mysqli_close($conn);
?>