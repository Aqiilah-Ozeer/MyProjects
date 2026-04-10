<!DOCTYPE html>
<html>
<head> 
    <title>Pet Dashboard</title>
    <style>
        body {
            background-color: #e6f2ff; /* soft blue background */
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            color: #0059b3;
            margin-top: 30px;
        }

        .top-right {
            background-color: #3399ff;
            color: white;
            padding: 10px 20px;
            font-weight: bold;
            display: flex;
            justify-content: flex-end;
            align-items: center;
            gap: 15px;
        }

        .header-content {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            gap: 15px;
            width: 100%;
        }

        .admin-username {
            color: white;
            font-weight: bold;
        }

        .logout-btn {
            background-color: #ff6666;
            color: white;
            padding: 8px 16px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .logout-btn:hover {
            background-color: #ff4444;
        }

        .sidebar {
            background-color: #cce0ff;
            width: 200px;
            height: 100vh;
            float: left;
            padding: 20px;
            box-sizing: border-box;
        }

        .sidebar a {
            display: block;
            background-color: #e6f2ff;
            color: #003366;
            text-decoration: none;
            font-weight: bold;
            padding: 10px 15px;
            margin-bottom: 10px;
            border-radius: 5px;
            border: 1px solid #99ccff;
        }

        .sidebar a:hover {
            background-color: #d0e7ff;
            color: #0059b3;
        }

        .main-content {
            margin-left: 220px;
            padding: 30px;
        }

        .filter-links {
            text-align: center;
            margin-bottom: 20px;
        }

        .filter-links a {
            margin: 0 10px;
            color: #0059b3;
            text-decoration: none;
            font-weight: bold;
            padding: 8px 12px;
            border: 1px solid #99ccff;
            border-radius: 5px;
            background-color: #ffffff;
        }

        .filter-links a:hover {
            background-color: #d0e7ff;
        }

        table {
            width: 95%;
            margin: 0 auto;
            border-collapse: collapse;
            background-color: #ffffff;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }

        th, td {
            border: 1px solid #cce0ff;
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #cce0ff;
            color: #003366;
        }

        img {
            border-radius: 5px;
            border: 1px solid #99ccff;
        }

        a {
            color: #0059b3;
            text-decoration: none;
            font-weight: bold;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <?php include 'header.php'; ?>
    <div class="sidebar"><?php include 'sidebar.php'; ?></div>
    
    <div class="main-content">
        <h2>Pet Dashboard</h2>
        <div class="filter-links">
            <a href="dashboard_pet.php">View All</a>
            <a href="dashboard_pet.php?filter=Adopted">View Adopted Pets</a>
            <a href="dashboard_pet.php?filter=Available">View Available Pets</a>
            <br><br>
            <a href="dashboard_pet_add.php">Add New Pet</a>
        </div>
        <?php
        $servername = "localhost";
        $username = "root";
        $password = "";
        $dbname = "webpadb";

        $conn = mysqli_connect($servername, $username, $password, $dbname);

        if (!$conn) {
            die("Connection failed: " . mysqli_connect_error());
        }

        if (isset($_GET['filter']) && $_GET['filter'] == 'Adopted') {
            $sql = "SELECT * FROM Pet WHERE PetStatus ='Adopted'"; 
            echo "<h2>Adopted Pets</h2>";
        } elseif (isset($_GET['filter']) && $_GET['filter'] == 'Available') {
            $sql = "SELECT * FROM Pet WHERE PetStatus ='Available'"; 
            echo "<h2>Available Pets</h2>";
        } else {
            $sql = "SELECT * FROM Pet"; 
            echo "<h2>All Pets</h2>";
        }
        $result = mysqli_query($conn, $sql);
        if (mysqli_num_rows($result) > 0) {
            echo "<table>
                    <tr>
                        <th>Image</th>
                        <th>Pet ID</th>
                        <th>Pet Name</th>
                        <th>Animal</th>
                        <th>Breed</th>
                        <th>Age</th>
                        <th>Status</th>
                        <th>Adoption Fee</th>
                    </tr>";
            while($row = mysqli_fetch_assoc($result)) {
                echo '<tr>';
                echo '<td><img src="images/' . $row['ImagePath'] . '" alt="' . $row['PetName'] . '" style="max-width:120px;"></td>';
                echo '<td>' . $row['PetID'] . '</td>';
                echo '<td>' . $row['PetName'] . '</td>';
                echo '<td>' . $row['Animal'] . '</td>';
                echo '<td>' . $row['Breed'] . '</td>';
                echo '<td>' . $row['Age'] . '</td>';
                echo '<td>' . $row['PetStatus'] . '</td>';
                echo '<td>' . $row['AdoptionFee'] . '</td>';
                echo '<td> <a href="dashboard_pet_edit.php?pet_id=' . $row['PetID'] . '">Edit</a> </td>';
                echo '<td> <a href="dashboard_pet_delete.php?pet_id=' . $row['PetID'] . '" onclick="return confirm(\'Delete this pet?\');">Delete</a> </td>';
                echo '</tr>';
            }
            echo "</table>";
        } else {
            echo "No pets found.";
        }
        mysqli_close($conn);
        ?>
    </div>
</body>
</html>