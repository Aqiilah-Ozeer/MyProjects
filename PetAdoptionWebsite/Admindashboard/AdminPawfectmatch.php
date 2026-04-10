<!DOCTYPE html>
<html>
<head>
    <title>Admin Login Pawfect Match</title>
    <style>
        body {
            background-color: #e6f2ff; /* soft blue background */
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
            color: #0059b3;
            margin-top: 40px;
        }

        form {
            background-color: #ffffff;
            width: 40%;
            margin: 30px auto;
            padding: 25px 30px;
            border: 2px solid #cce0ff;
            border-radius: 10px;
            box-shadow: 2px 2px 10px rgba(0,0,0,0.1);
            box-sizing: border-box;
        }

        label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
            margin-bottom: 5px;
            color: #003366;
        }

        input[type="text"],
        input[type="password"] {
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
            margin-left: auto;
        }

        input[type="submit"]:hover {
            background-color: #2675c7;
        }

        .error-message {
            text-align: center;
            color: red;
            font-weight: bold;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h1>Admin Login Pawfect Match</h1>
    <form method="POST" action="ProcessAdminLogin.php">

        <label for="adminusername">Username:</label>
        <input type="text" name="adminusername" required>

        <label for="password">Password:</label>
        <input type="password" name="password" required>

        <input type="submit" value="Login">
    </form>

    <?php 
    if (isset($_GET["error"])) { 
        if ($_GET["error"] == "yes") { 
            echo '<div class="error-message">Invalid Username or Password !!</div>'; 
        } 
    } 
    ?>
</body>
</html>