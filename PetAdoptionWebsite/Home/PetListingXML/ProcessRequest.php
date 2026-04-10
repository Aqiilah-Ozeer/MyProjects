<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "webpadb";

$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn) {
  die("Connection failed: " . mysqli_connect_error());
}

$sql = "INSERT INTO Adoption (PetID, Username, ReasonForChoosing,
    OwnsOtherPets, AdoptedBefore, HasSecureOutdoorArea, AgreesToHomeVisit,
    FinanciallyPrepared, AppointmentDate) 
    VALUES ('" . $_POST['petid'] . "',
            '" . $_POST['username'] . "', '" . $_POST['reason'] . "',
            '" . $_POST['ownership'] . "', '" . $_POST['adoptedbefore'] . "',
            '" . $_POST['secureoutdoor'] . "', '" . $_POST['homevisit'] . "',
            '" . $_POST['financialprep'] . "', '" . $_POST['appointment'] . "')";

if (mysqli_query($conn, $sql)) {
    echo "<script>
        alert('Adoption request submitted successfully!');
        window.location.href = 'Homepage.html'; /* Redirect to home page */
    </script>";
} else {
    echo "<script>
        alert('Error submitting request. Please try again.');
        window.history.back(); /* Go back to the form */
    </script>";
}

mysqli_close($conn);
?>