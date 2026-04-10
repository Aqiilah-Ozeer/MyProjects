<?php
if (isset($_GET['id'])) {
    $adoptionID = $_GET['id'];

    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "webpadb";
    $conn = mysqli_connect($servername, $username, $password, $dbname);
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "SELECT a.AdoptionID, p.PetName, p.Animal, p.ImagePath, u.FullName, u.PhoneNumber, u.EmailAddress, 
            u.FullAddress, a.RequestDate, a.ReasonForChoosing, a.OwnsOtherPets, a.AdoptedBefore, 
            a.HasSecureOutdoorArea, a.AgreesToHomeVisit, a.FinanciallyPrepared, a.AppointmentDate
            FROM Adoption a, Pet p, UserAccount u 
            WHERE a.AdoptionID = $adoptionID 
            AND a.PetID = p.PetID 
            AND a.Username = u.Username";
    $result = mysqli_query($conn, $sql);
    if (mysqli_num_rows($result) == 1) {
        $row = mysqli_fetch_assoc($result);
    } 
}
?>
        <!DOCTYPE html>
        <html>
        <head>
            <title>Adoption Request Details</title>
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
                    margin-top: 30px;
                }

                .sidebar {
                    background-color: #cce0ff;
                    width: 200px;
                    height: 100vh;
                    float: left;
                    padding: 20px;
                    box-sizing: border-box;
                }

                .main-content {
                    margin-left: 220px;
                    padding: 30px;
                }

                img {
                    display: block;
                    margin-bottom: 20px;
                    border: 2px solid #99ccff;
                    border-radius: 5px;
                }

                .detail-label {
                    font-weight: bold;
                    color: #003366;
                    display: inline-block;
                    width: 180px;
                }

                .detail-row {
                    margin-bottom: 10px;
                }

                .button-group {
                    margin-top: 30px;
                    overflow: hidden;
                }

                .button-left {
                    float: left;
                }

                .button-right {
                    float: right;
                }

                button {
                    background-color: #3399ff;
                    color: white;
                    padding: 12px 24px;
                    border: none;
                    border-radius: 5px;
                    font-size: 16px;
                    cursor: pointer;
                    margin-right: 15px;
                    margin-bottom: 10px;
                }

                button:hover {
                    background-color: #2675c7;
                }

            </style>
        </head>
        <body>
            <div class="main-content">
                <h1>Adoption Request Details</h1>
                <img src="images/<?php echo $row['ImagePath']; ?>" alt="Pet Image" style="max-width:200px;">

                <div class="detail-row"><span class="detail-label">Adoption ID:</span> <?php echo $row['AdoptionID']; ?></div>
                <div class="detail-row"><span class="detail-label">Pet Name:</span> <?php echo $row['PetName']; ?></div>
                <div class="detail-row"><span class="detail-label">Animal Type:</span> <?php echo $row['Animal']; ?></div>
                <div class="detail-row"><span class="detail-label">Full Name:</span> <?php echo $row['FullName']; ?></div>
                <div class="detail-row"><span class="detail-label">Phone Number:</span> <?php echo $row['PhoneNumber']; ?></div>
                <div class="detail-row"><span class="detail-label">Email Address:</span> <?php echo $row['EmailAddress']; ?></div>
                <div class="detail-row"><span class="detail-label">Full Address:</span> <?php echo $row['FullAddress']; ?></div>
                <div class="detail-row"><span class="detail-label">Request Date:</span> <?php echo $row['RequestDate']; ?></div>
                <div class="detail-row"><span class="detail-label">Reason for Choosing:</span> <?php echo $row['ReasonForChoosing']; ?></div>
                <div class="detail-row"><span class="detail-label">Owns Other Pets:</span> <?php echo $row['OwnsOtherPets']; ?></div>
                <div class="detail-row"><span class="detail-label">Adopted Before:</span> <?php echo $row['AdoptedBefore']; ?></div>
                <div class="detail-row"><span class="detail-label">Has Secure Outdoor Area:</span> <?php echo $row['HasSecureOutdoorArea']; ?></div>
                <div class="detail-row"><span class="detail-label">Agrees to Home Visit:</span> <?php echo $row['AgreesToHomeVisit']; ?></div>
                <div class="detail-row"><span class="detail-label">Financially Prepared:</span> <?php echo $row['FinanciallyPrepared']; ?></div>
                <div class="detail-row"><span class="detail-label">Appointment Date:</span> <?php echo $row['AppointmentDate']; ?></div>

                <form method="POST" action="dashboard_request_buttons.php">
                    <input type="hidden" name="adoption_id" value="<?php echo $row['AdoptionID']; ?>">

                    <div class="button-group">
                        <div class="button-left">
                            <button type="submit" name="button" value="approve">Approve</button>
                            <button type="submit" name="button" value="reject">Reject</button>
                        </div>
                        <div class="button-right">
                            <button type="button" onclick="back();">Back</button>
                        </div>
                    </div>
                </form>
                <script>
                    function back() {
                        window.location.href = 'AdminDashboard.php';
                    }
                </script>
            </div>
        </body>
        </html>
<?php
mysqli_close($conn);
?>