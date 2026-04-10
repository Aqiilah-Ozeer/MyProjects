<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: ../UserLogin.php");
    exit();
}
$username = $_SESSION['username'];

// petinfo.php
include 'db.php'; 
$petID = $_GET['id'] ?? '';

$sql = "SELECT PetName, Animal, Breed, Age, AdoptionFee FROM Pet WHERE PetID = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $petID);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    echo "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><title>Not Found</title><link rel='stylesheet' href='../Homepage.css'><link rel='stylesheet' href='style.css'></head><body><div class='pet-info-container'><h2>Pet not found.</h2><a href='main.php' class='back-button'>← Back to Listing</a></div></body></html>";
    exit;
}

$pet = $result->fetch_assoc();
$stmt->close();
$conn->close();

$imagePath = 'images/';
$extensions = ['jpeg', 'jpg', 'png', 'gif', 'webp']; 
$imageFile = 'default.jpg';
$petName = strtolower(str_replace(' ', '_', $pet['PetName']));

foreach ($extensions as $ext) {
    $match = glob("$imagePath$petName.$ext");
    if ($match) {
        $imageFile = basename($match[0]);
        break;
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><?= htmlspecialchars($pet['PetName']) ?> - Profile</title>
  <link rel="stylesheet" href="../Homepage.css">
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <!--  Navigation bar  -->
  <header class="navbar">
    <div class="logo">
      PawFect Match - Pet Adoption Centre 
    </div>
    <nav>
      <ul class="nav-links">
        <li> <a href="../PawFectMatch.php"> Home </a></li>
        <li><a href="../AdoptionList/MyAdoptions.php">My Adoptions</a></li>
        <li> <a href="#"> About us </a></li>
        <li> <a href="#"> Contact Us </a></li>
        <li>
          <div class="profile-dropdown">
            <button class="profile-btn"><?php echo htmlspecialchars($username); ?></button>
            <div class="dropdown-menu">
              <a href="../UserLogout.php">Logout</a>
            </div>
          </div>
        </li>
      </ul>
    </nav>
  </header>

  <main class="pet-info-page">
    <div class="pet-info-container">
        <div class="pet-info-image">
            <img src="images/<?= $imageFile ?>" alt="<?= htmlspecialchars($pet['PetName']) ?>">
            <span class="adoption-status">Ready for Adoption!</span>
        </div>
        
        <div class="pet-details">
            <h2>Meet <?= htmlspecialchars($pet['PetName']) ?></h2>
            <div class="detail-group">
                <p><strong>Species:</strong> <span><?= htmlspecialchars($pet['Animal']) ?></span></p>
                <p><strong>Breed/Subtype:</strong> <span><?= htmlspecialchars($pet['Breed']) ?></span></p>
                <p><strong>Age:</strong> <span><?= htmlspecialchars($pet['Age']) ?> year(s) old</span></p>
            </div>
            
            <div class="detail-fee">
                <strong>Adoption Fee:</strong>
                <span class="fee-amount">Rs <?= number_format($pet['AdoptionFee'], 2) ?></span>
            </div>
            
            <a href="main.php" class="back-button">← Back to Pet Listing</a>
            <a href="AdoptionRequest.php" class="adopt-button">Apply to Adopt</a>
        </div>
    </div>
  </main>
  <footer>
    <p>&copy; 2025 Pet Adoption Center.</p>
  </footer>
</body>
</html>