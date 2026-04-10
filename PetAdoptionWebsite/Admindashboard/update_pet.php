<?php

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "webpadb";
$conn = mysqli_connect($servername, $username, $password, $dbname);
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$pet_id = $_POST['petid'];
$pet_name = $_POST['pet_name'];
$animal = $_POST['animal'];
$breed = $_POST['breed'];
$age = $_POST['age'];
$petstatus = $_POST['petstatus'];
$adoption_fee = $_POST['adoption_fee'];
$imagepath = $_POST['imagepath'];

$sql = "UPDATE Pet SET 
        PetName='$pet_name', 
        Animal='$animal', 
        Breed='$breed', 
        Age=$age, 
        PetStatus='$petstatus', 
        AdoptionFee=$adoption_fee, 
        ImagePath='$imagepath' 
        WHERE PetID=$pet_id";

if (mysqli_query($conn, $sql)) {
    echo "<script>
        alert('Update successful!');
        window.location.href = 'dashboard_pet.php';
        </script>";
} else {
    echo "<script>
        alert('Error can't update data. Please try again.');
        window.history.back();
        </script>";
}
mysqli_close($conn);
?>