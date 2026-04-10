<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "webpadb";
$conn = mysqli_connect($servername, $username, $password, $dbname);
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
if (isset($_GET['pet_id'])) {
    $pet_id = $_GET['pet_id'];
    $sql = "DELETE FROM Pet WHERE PetID = $pet_id";
    if (mysqli_query($conn, $sql)) {
        echo "<script>
            alert('Delete successful!');
            window.location.href = 'dashboard_pet.php';
            </script>";
    } else {
        echo "<script>
        alert('Error can't delete data. Please try again.');
        window.history.back();
        </script>";
    }
}
mysqli_close($conn);
?>