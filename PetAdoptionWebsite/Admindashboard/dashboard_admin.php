<!DOCTYPE html>
<html>
<head> 
    <title>Admin Dashboard</title>
    <style>
        body {
            background-color: #e6f2ff;
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
            background-color: #ffffff;
            border-left: 2px solid #cce0ff;
            min-height: 100vh;
            box-sizing: border-box;
        }

        a {
            display: inline-block;
            margin: 10px 15px;
            padding: 8px 12px;
            background-color: #3399ff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }

        a:hover {
            background-color: #2675c7;
        }

        table {
            width: 90%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: #f9fcff;
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

        td a {
            background-color: #ff6666;
            color: white;
            padding: 6px 10px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
        }

        td a:hover {
            background-color: #cc0000;
        }
    </style>
</head>
<body>
    <?php include 'header.php'; ?>
    <div class="sidebar"><?php include 'sidebar.php'; ?></div>
    
    <div class="main-content">
        <h2>Admin Dashboard</h2>
        <a href="dashboard_admin_add.php">Add New Admin</a>
        <?php
        $user = $_SESSION['adminusername'];
        echo '<a href="dashboard_admin_edit.php?admin=' . $user . '">Edit</a>';

        $servername = "localhost";
        $username = "root";
        $password = "";
        $dbname = "webpadb";

        $conn = mysqli_connect($servername, $username, $password, $dbname);

        if (!$conn) {
            die("Connection failed: " . mysqli_connect_error());
        }

        $sql = "SELECT * FROM AdminAccount";
        $result = mysqli_query($conn, $sql);
        if (mysqli_num_rows($result) > 0) {
            echo "<table>
                    <tr>
                        <th>User Name</th>
                        <th>Password</th>
                    </tr>";
            while($row = mysqli_fetch_assoc($result)) {
                echo "<tr>
                        <td>" . $row["Adminusername"] . "</td>
                        <td>" . $row["Password"] . "</td>
                        <td><a href='dashboard_admin_delete.php?admin=" . $row["Adminusername"] . "' onclick=\"return confirm('Delete this admin?');\">Delete</a></td>
                    </tr>";
            }
            echo "</table>";
        } else {
            echo "no admin found";
        }
        mysqli_close($conn);
        ?>
    </div>
</body>
</html>