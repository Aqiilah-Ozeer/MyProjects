<!DOCTYPE html>
<html>
<head> 
    <title>Admin Dashboard</title>
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
            margin-top: 20px;
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

        .main-content {
            margin-left: 220px;
            padding: 20px;
        }

        .request-link {
            display: block;
            background-color: #ffffff;
            border: 1px solid #99ccff;
            border-radius: 5px;
            padding: 10px;
            margin: 10px 0;
            text-decoration: none;
            color: #003366;
        }

        .request-link:hover {
            background-color: #e0f0ff;
        }

        .filter-links {
            text-align: center;
            margin: 20px 0;
        }

        .filter-links a {
            margin: 0 10px;
            color: #0059b3;
            text-decoration: none;
            font-weight: bold;
        }

        .filter-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <?php include 'header.php'; ?>
    <div class="sidebar">
        <?php include 'sidebar.php'; ?>
    </div>
    
    <div class="main-content">
        <h2>Request Dashboard</h2>
        <div class="filter-links">
            <a href="AdminDashboard.php?filter=all">View All</a>
            <a href="AdminDashboard.php">View Pending</a>
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

        if (isset($_GET['filter']) && $_GET['filter'] == 'all') {
            $sql = "SELECT a.AdoptionID, p.PetName, p.Animal, a.AdoptionStatus 
                    FROM Adoption a, Pet p 
                    WHERE a.PetID = p.PetID";
            echo "<h2>All Adoption Requests</h2>";
        } else {
            $sql = "SELECT a.AdoptionID, p.PetName, p.Animal, a.AdoptionStatus 
                    FROM Adoption a, Pet p 
                    WHERE AdoptionStatus ='Pending' 
                    AND a.PetID = p.PetID";
            echo "<h2>Pending Adoption Requests</h2>";
        }
        
        $result = mysqli_query($conn, $sql);
        if (mysqli_num_rows($result) > 0) {
            while($row = mysqli_fetch_assoc($result)) {
                echo "<a class='request-link' href='dashboard_adoption_view.php?id=". $row["AdoptionID"]."'>" .
                "ID: " .$row["AdoptionID"]. " | Pet: " .$row["PetName"]. 
                " (" .$row["Animal"]. ") | Status: " .$row["AdoptionStatus"]."</a>";
            }
        } else {
            echo "No pending adoption requests.";
        }

        mysqli_close($conn);
        ?>
    </div>
</body>
</html>
