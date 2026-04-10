<?php
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "webpadb";

    $conn = mysqli_connect($servername, $username, $password, $dbname);

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "SELECT * FROM AdminAccount WHERE Adminusername='".$_POST['username']."';";
    $result = mysqli_query($conn, $sql);
    if (mysqli_num_rows($result) > 0) {
        session_start();
        echo "<script>
        alert('Username already exists. Please choose a different username.'); 
        window.location.href='dashboard_admin_add.php';
        </script>";
    } elseif (strlen($_POST['pwd']) >= 8 && strlen($_POST['pwd']) <= 16) {
        $sql = "INSERT INTO AdminAccount (Adminusername, Password) VALUES
                ('" . $_POST['username'] . "', '" . $_POST['pwd'] . "');";

        if (mysqli_query($conn, $sql)) {
            echo "<script>
            alert('Admin updated successfully.'); 
            window.location.href='dashboard_admin.php';
            </script>";
        } else {
            echo "Error updating record: " . mysqli_error($conn);
        }
    } else {
        echo "<script>
        alert('Password must be between 8 and 16 characters.');
        window.location.href='dashboard_admin_add.php';
        </script>";
    }
    mysqli_close($conn);
?>