<?php

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "webpadb";
$conn = mysqli_connect($servername, $username, $password, $dbname);
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$pet_name = $_POST['pet_name'];
$animal = $_POST['animal'];
$breed = $_POST['breed'];
$age = $_POST['age'];
$petstatus = $_POST['petstatus'];
$adoption_fee = $_POST['adoption_fee'];
$imagepath = $_POST['imagepath'];

$sql = "INSERT INTO Pet (PetName, Animal, Breed, Age, PetStatus, AdoptionFee, ImagePath) VALUES
        ('$pet_name', '$animal', '$breed', $age, '$petstatus', $adoption_fee, '$imagepath')";

if (mysqli_query($conn, $sql)) {
    echo "<script>
        alert('Addition successful!');
        window.location.href = 'dashboard_pet.php';
        </script>";
} else {
    echo "<script>
        alert('Error can't add data. Please try again.');
        window.history.back();
        </script>";
}
mysqli_close($conn);
?>