<?php
    session_start();
    if (!isset($_SESSION['username'])) {
        header("Location: ../UserLogin.php");
        exit();
    }
    $username = $_SESSION['username'];
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>🐾 Pet Adoption</title>
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

  <header>
    <div class="header-content">
        <h1>🐾 Available Pets for Adoption</h1>
        <p>Find your new best friend among our wonderful animals.</p>
    </div>
  </header>

  <main>
    <section class="filter-controls">
      <div class="main-category-buttons">
        <h2>Filter by Species:</h2>
        <button class="main-filter active" data-main="">All Pets</button>
        <button class="main-filter" data-main="Cat">Cats</button>
        <button class="main-filter" data-main="Bird">Birds</button>
        <button class="main-filter" data-main="Dog">Dogs</button>
        <button class="main-filter" data-main="Rabbit">Rabbits</button>
      </div>

      <div class="sub-category-buttons" id="sub-category-buttons-container">
        <h2>Filter by Subtype/Breed:</h2>
        <p class="initial-sub-text">Select a species to view available breeds/subtypes.</p>
      </div>
    </section>

    <section id="pet-grid" class="grid-container">
      <div class="loading-message">
        <p>Loading available pets...</p>
      </div>
    </section>
  </main>

  <footer>
    <p>&copy; 2025 Pet Adoption Center. All rights reserved.</p>
  </footer>

  <script src="script.js"></script>
</body>
</html>