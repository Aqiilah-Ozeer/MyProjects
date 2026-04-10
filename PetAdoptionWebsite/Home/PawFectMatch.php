<?php
    session_start();
    if (!isset($_SESSION['username'])) {
        header("Location: UserLogin.php");
        exit();
    }
    $username = $_SESSION['username'];
?>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title> PawFect Match-Pet Adoption Centre </title>
        <link rel="stylesheet" href="Homepage.css">
    </head>
    <body>
        <!--  Navigation bar  -->
        <header class="navbar">
          <div class="logo">
            PawFect Match - Pet Adoption Centre 
          </div>
            <nav>
                <ul class="nav-links">
                    <li> <a href="PawFectMatch.php" class="active"> Home </a></li>
                    <li><a href="AdoptionList/MyAdoptions.php">My Adoptions</a></li>
                    <li> <a href="#"> About us </a></li>
                    <li> <a href="#"> Contact Us </a></li>
                <li>
                    <div class="profile-dropdown">
                        <button class="profile-btn"><?php echo htmlspecialchars($username); ?></button>
                        <div class="dropdown-menu">
                            <a href="UserLogout.php">Logout</a>
                        </div>
                    </div>
                </li>
                </ul>
            </nav>

        </header>

        <!--  Main Section  -->
          <main>
            <section class="hero">
                <div class="hero-text">
                    <h1> Find Your New Best Friend! </h1>
                    <p> Adopt a loving companion today and give them a forever home.</p>
                    <a href="PetListing/main.php" class="browse-btn"> Browse Pets </a>
                    <a href="PetListingXML/main.php" class="browse-btn"> Browse Pets (XML) </a>
                </div>
            </section>

            <br><br>
            <!-- Categories Section -->
            <section class="categories">
               <h2> Our categories of adoptable pets </h2>
               <div class="category-cards">
                    <div class="card">
                        <img src="images/dog-icon.png" alt="Dog">
                        <h3>Dogs</h3>
                        <p>Loyal, playful, and always excited to see you!</p>
                    </div>

                    <div class="card">
                        <img src="images/cat-icon.png" alt="Cat">
                        <h3>Cats</h3>
                        <p>Independent, cuddly, and full of personality.</p>
                    </div>

                    <div class="card">
                        <img src="images/rabbit-icon.png" alt="Rabbit">
                        <h3>Rabbits</h3>
                        <p>Soft, gentle, and perfect for quiet homes.</p>
                    </div>

                    <div class="card">
                        <img src="images/bird-icon.png" alt="Bird">
                        <h3>Birds</h3>
                        <p>Colorful companions that bring joy to your home.</p>
                    </div>
                </div>
            </section>


            <!-- Adoption steps -->
            <!-- Adoption steps -->
            <section class="how-to-adopt">
                <h2>How to Adopt ?</h2>
            
                <div class="adopt-container">
                    
                    <div class="adopt-steps">
                        <div class="step-card">
                            <h3>Step 1: Find Your Perfect Match</h3>
                           
                             <p> Browse through our categories and pick the pet that feels right for you. </p>
                  
            
                            <h3>Step 2: Fill the Adoption Form</h3>
                            <p> Complete a quick form with your details so we can match you perfectly with your new friend. </p>
                                 
                           
            
                            <h3>Step 3: Fill in the Adoption Form</h3>
                            <p>
                                Once you’ve found your new companion, fill out a quick and easy adoption form. 
                                This helps us understand you better and ensure the pet you choose will be a perfect fit for your home.
                            </p>      
                            
            
                            <h3>Step 4: Meet and Take Them Home With Love</h3>
                            <p>Visit our center to meet your chosen pet and bring your new family member home!</p>
                                
                            
                        </div>
                    </div>
            
                    <!-- Image Column -->
                    <div class="adopt-image">
                        <img src="images/family.jpg" alt="Happy pets ready for adoption">
                    </div>
                </div>
            </section>

            <br><br>
            <!--   Why adopt with us -->
            <section class="info">
                <h2> Why Adopt With Us? </h2>
                <div class="info-cards">
                    <div class="card">
                        <img src="images/care.jpg" alt="Care">
                        <h3> We care </h3>
                        <p> All our animals are vaccinated and well-cared for before adoption.</p>
                    </div>
                    <div class="card">
                        <img src="images/adopt.jpg" alt="Adopt">
                        <h3> Easy Adoption </h3>
                        <p> Our adoption process is simple,transparent, and quick.</p>
                    </div>
                    
                    <div class="card">
                        <img src="images/community.jpg" alt="Community">
                        <h3> Community Love </h3>
                        <p>We work with volunteers and pet welfare organisations to make a difference.</p>
                    </div> 
                </div>
            </section>

           
        </main>
        <!-- Footer -->
        <footer>
            <div class="footer-container">
                <!-- Logo & About -->
                <div class="footer-section logo-section">
                    <img src="images/logo.png" alt="Pet Adoption Centre Logo" class="footer-logo">
                    <p>© 2025 Pawfect Match - Pet Adoption Centre | All Rights Reserved</p>
                </div>
        
                <!-- Quick Links -->
                <div class="footer-section">
                    <h4>Quick Links</h4>
                    <ul>
                        <li><a href="PawFectMatch.php">Home</a></li>
                        <li><a href="#">My Adoptions</a></li>
                        <li> <a href="#"> About Us </a></li>
                        <li><a href="#">How to Adopt</a></li>
                        <li><a href="#">Contact us</a></li>
                    </ul>
                </div>
        
                <!-- Location -->
                <div class="footer-section">
                    <h4>Location</h4>
                    <p>Royal Road <br>Port Louis, Mauritius</p>
                </div>
        
                <!-- Opening Hours -->
                <div class="footer-section">
                    <h4>Opening Hours</h4>
                    <p>Mon - Fri: 9am - 6pm<br>Sat: 10am - 4pm<br>Sun: Closed</p>
                </div>
        
                <!-- Social Media -->
                <div class="footer-section social-section">
                    <h4>Follow Us</h4>
                    <a href="#">Facebook</a> | 
                    <a href="#">Instagram</a> | 
                    <a href="#">Twitter</a>
                </div>
            </div>
        </footer>
        
    </body>

</html>



  