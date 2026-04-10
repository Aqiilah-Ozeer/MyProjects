<!DOCTYPE html>
<html>
<head> 
    <title>Pet Add</title>
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
            background-color: #ffffff;
            border-left: 2px solid #cce0ff;
            min-height: 100vh;
            box-sizing: border-box;
        }

        form {
            width: 60%;
            margin: 0 auto;
            padding: 20px;
            border: 2px solid #cce0ff;
            border-radius: 10px;
            background-color: #f9fcff;
        }

        label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
            margin-bottom: 5px;
            color: #003366;
        }

        input[type="text"],
        input[type="number"],
        input[type="double"],
        select {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #99ccff;
            border-radius: 5px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #3399ff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            display: block;
            margin: 20px auto 0 auto;
        }

        input[type="submit"]:hover {
            background-color: #2675c7;
        }
    </style>
</head>
<body>
    <?php include 'header.php'; ?>
    <div class="sidebar"><?php include 'sidebar.php'; ?></div>

    <div class="main-content">
        <h2>Add New Pet</h2>
        <form action="add_pet.php" method="POST">

            <label for="pet_name">Pet Name:</label>
            <input type="text" name="pet_name" required><br>

            <label for="animal">Animal:</label>
            <input type="text" name="animal" required><br>

            <label for="breed">Breed:</label>
            <input type="text" name="breed" required><br>

            <label for="age">Age:</label>
            <input type="number" name="age" required><br>

            <label for="petstatus">PetStatus:</label>
            <select name="petstatus">
                <option value="Available">Available</option>
                <option value="Adopted">Adopted</option>
            </select><br>

            <label for="adoption_fee">Adoption Fee:</label>
            <input type="double" name="adoption_fee" required><br>

            <label for="imagepath">Image Path:</label>
            <input type="text" name="imagepath"><br>

            <input type="submit" value="Add Pet">
        </form>
    </div>
</body>
</html>
