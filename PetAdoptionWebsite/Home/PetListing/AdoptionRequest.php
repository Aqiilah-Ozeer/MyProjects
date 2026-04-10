<!DOCTYPE html>
<html>
<head>
    <title>Adoption Request</title>
    <style>
        body {
            background-color: #f0f8ff; /* soft blue */
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
            color: #0059b3;
            margin-top: 30px;
        }

        form {
            background-color: #ffffff;
            width: 60%;
            margin: 30px auto;
            padding: 20px 30px; /* extra right padding */
            border: 2px solid #cce0ff;
            border-radius: 10px;
            box-sizing: border-box;
        }

        label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="date"],
        textarea {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #99ccff;
            border-radius: 5px;
            box-sizing: border-box;
        }

        input[type="radio"] {
            margin-right: 5px;
        }

        input[type="submit"] {
            background-color: #3399ff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            margin-top: 20px;
            display: block;
            margin-left: auto; /* aligns to right inside form */
        }

        input[type="submit"]:hover {
            background-color: #2675c7;
        }
    </style>
</head>
<body>
    <h1>Adoption Request Form</h1>
    <form action="ProcessRequest.php" method="post">

        <label for="petid">ID of Pet Interested In:</label>
        <input type="text" name="petid" required>

        <label for="username">User Name:</label>
        <input type="text" name="username" required>

        <label for="reason">Reason for Choosing and Adopting this Pet:</label>
        <textarea name="reason" rows="4" cols="50" required></textarea>

        <label for="ownership">Do you own other pets?</label>
        <input type="radio" name="ownership" value="Yes" required> Yes
        <input type="radio" name="ownership" value="No" required> No

        <label for="adoptedbefore">Have you adopted pets before?</label>
        <input type="radio" name="adoptedbefore" value="Yes" required> Yes
        <input type="radio" name="adoptedbefore" value="No" required> No

        <label for="secureoutdoor">Do you have a secure outdoor area for the pet?</label>
        <input type="radio" name="secureoutdoor" value="Yes" required> Yes
        <input type="radio" name="secureoutdoor" value="No" required> No

        <label for="homevisit">Do you agree to a home visit?</label>
        <input type="radio" name="homevisit" value="Yes" required> Yes
        <input type="radio" name="homevisit" value="No" required> No

        <label for="financialprep">Are you financially prepared for pet ownership?</label>
        <input type="radio" name="financialprep" value="Yes" required> Yes
        <input type="radio" name="financialprep" value="No" required> No

        <label for="appointment">Choose appointment date for in-person adoption process:</label>
        <input type="date" name="appointment" required>

        <input type="submit" value="Submit Request">
    </form>
</body>
</html>