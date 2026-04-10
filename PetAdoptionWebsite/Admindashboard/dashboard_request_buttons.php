<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "webpadb";

$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn) {
  die("Connection failed: " . mysqli_connect_error());
}

if ($_POST['button'] === 'approve') {
    $adoptionID = $_POST['adoption_id'];
    $sql = "UPDATE Adoption SET AdoptionStatus='Approved' WHERE AdoptionID=$adoptionID";
    mysqli_query($conn, $sql);
    header("Location: AdminDashboard.php");
} elseif ($_POST['button'] === 'reject') {
    $adoptionID = $_POST['adoption_id'];
    $sql = "UPDATE Adoption SET AdoptionStatus='Rejected' WHERE AdoptionID=$adoptionID";
    mysqli_query($conn, $sql);
    header("Location: AdminDashboard.php");
}
?>