<?php
/**
 * MyAdoptions.php
 * Displays the logged-in user's adoption requests
 * 
 * Showcases:
 * 1. SOAP Web Service consumption using PHP Native SOAP
 * 2. XML processing with DOMDocument
 * 3. XSD validation of XML structure
 * 4. XSLT transformation of XML to HTML
 */

// Start session to check user login status
session_start();
// Redirect to login page if user is not logged in
if (!isset($_SESSION['username'])) {
    header("Location: ../UserLogin.php");
    exit();
}

$username = $_SESSION['username'];
$soap_error = false;
$error_message = "";

// SOAP WEB SERVICE CLIENT

/**
 * Get adoption data via SOAP Web Service using native PHP SOAP
 * param string $username - The username to fetch adoptions for
 * return array - Associative array with 'success' and 'xml' or 'error'
 */
function getAdoptionsViaSOAP($username) {
    try {
        // Check if SOAP extension is loaded
        if (!extension_loaded('soap')) {
            return array('error' => 'SOAP extension is not enabled in PHP');
        }
        
        // Service URL in non-WSDL mode(with WSDL was not working)
        $service_url = "http://" . $_SERVER['HTTP_HOST'] . dirname($_SERVER['SCRIPT_NAME']) . "/adoption_service.php";
        
        // Create SOAP client in non-WSDL mode(with WSDL was not working)
        $client = new SoapClient(null, array(
            'location' => $service_url,
            'uri' => 'http://localhost/WebAssignment/Home/AdoptionList/',
            'trace' => true,
            'exceptions' => true
        ));
        
        // Call the web service
        $xml_string = $client->__soapCall('getAdoptionsXMLByUser', array($username));
        
        return array('success' => true, 'xml' => $xml_string);
        
    } catch (Exception $e) {
        return array('error' => 'SOAP Exception: ' . $e->getMessage());
    }
}

// PROCESS WEB SERVICE RESPONSE

// Call the web service
$result = getAdoptionsViaSOAP($username);

if (isset($result['error'])) {
    // Web service failed - display error message
    $soap_error = true;
    $error_message = $result['error'];
    $output_html = "<div class='error'>"
                 . "Error fetching adoptions: " . htmlspecialchars($error_message)
                 . "</div>";
} else {
    // Web service succeeded - process the XML response
    $xml_string = $result['xml'];
    
    // Load XML from web service response
    $xmlDoc = new DOMDocument();
    $xmlDoc->loadXML($xml_string);
    
    // Validate XML against XSD Schema
    $xsdFile = __DIR__ . '/adoptions.xsd';
    if (file_exists($xsdFile)) {
        // schemaValidate() checks if XML structure matches XSD rules
        if (@$xmlDoc->schemaValidate($xsdFile)) {
            // Transform XML to HTML using XSLT
            $xslFile = __DIR__ . '/adoptions.xsl';
            if (file_exists($xslFile)) {
                // Load XSLT stylesheet
                $xsl_doc = new DOMDocument();
                $xsl_doc->load($xslFile);
                
                // Create XSLT processor and apply transformation
                $proc = new XSLTProcessor();
                $proc->importStylesheet($xsl_doc);
                
                // Transform XML to HTML
                $output_html = $proc->transformToXML($xmlDoc);
            } else {
                $output_html = "<div class='error'>XSLT stylesheet not found.</div>";
            }
        } else {
            $output_html = "<div class='error'>Invalid XML structure. Failed XSD validation.</div>";
        }
    } else {
        $output_html = "<div class='error'>XSD schema file not found.</div>";
    }
    
    
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Adoptions - PawFect Match</title>
    <link rel="stylesheet" href="../Homepage.css">
    <style>
        /* Additional styles for adoption table */
        .adoptions-container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .adoptions-container h2 {
            color: #0059b3;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #cce0ff;
        }
        .adoptions-table {
            width: 100%;
            border-collapse: collapse;
        }
        .adoptions-table th,
        .adoptions-table td {
            border: 1px solid #cce0ff;
            padding: 12px;
            text-align: left;
        }
        .adoptions-table th {
            background-color: #cce0ff;
            color: #003366;
            font-weight: bold;
        }
        .adoptions-table tr:hover {
            background-color: #f0f8ff;
        }
        .status-pending {
            color: #ff9800;
            font-weight: bold;
        }
        .status-approved {
            color: #4caf50;
            font-weight: bold;
        }
        .status-rejected {
            color: #f44336;
            font-weight: bold;
        }
        .no-adoptions {
            text-align: center;
            padding: 40px;
            background: #f9fcff;
            border-radius: 8px;
        }
        .browse-link {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            background-color: #3399ff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .error {
            color: #f44336;
            text-align: center;
            padding: 20px;
            background: #ffebee;
            border-radius: 8px;
            margin: 20px;
        }
    </style>
</head>
<body>
    <!--  Navigation bar  -->
    <header class="navbar">
        <div class="logo">
        PawFect Match - Pet Adoption Centre 
        </div>
        <nav>
            <ul class="nav-links">
                <li> <a href="../PawFectMatch.php" class="active"> Home </a></li>
                <li><a href="MyAdoptions.php">My Adoptions</a></li>
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

    <!-- Main Content - XSLT transformed XML appears here -->
    <main>
        <?php echo $output_html; ?>
    </main>

    <!-- Footer -->
    <footer>
        <div class="footer-container">
            <div class="footer-section logo-section">
                <img src="../images/logo.png" alt="Logo" class="footer-logo">
                <p>© 2025 Pawfect Match - Pet Adoption Centre | All Rights Reserved</p>
            </div>
            <div class="footer-section">
                <h4>Quick Links</h4>
                <ul>
                    <li><a href="../PawFectMatch.html">Home</a></li>
                    <li><a href="MyAdoptions.php">My Adoptions</a></li>
                    <li><a href="#">About Us</a></li>
                    <li><a href="#">Contact Us</a></li>
                </ul>
            </div>
            <div class="footer-section">
                <h4>Location</h4>
                <p>Royal Road <br>Port Louis, Mauritius</p>
            </div>
            <div class="footer-section">
                <h4>Opening Hours</h4>
                <p>Mon - Fri: 9am - 6pm<br>Sat: 10am - 4pm<br>Sun: Closed</p>
            </div>
        </div>
    </footer>
</body>
</html>