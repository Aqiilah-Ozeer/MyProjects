<?php
/**
 * adoption_service.php
 * SOAP Server using PHP's native SOAP extension
 */

// Enable error reporting for debugging
ini_set('display_errors', 1);
error_reporting(E_ALL);

/**
 * Get adoptions as XML string for a specific user
 */
function getAdoptionsXMLByUser($username) {
    $servername = "localhost";
    $dbusername = "root";
    $dbpassword = "";
    $dbname = "webpadb";
    
    $conn = mysqli_connect($servername, $dbusername, $dbpassword, $dbname);
    
    if (!$conn) {
        return '<?xml version="1.0" encoding="UTF-8"?><adoptions><error>Database connection failed</error></adoptions>';
    }
    
    $sql = "SELECT a.AdoptionID, p.PetName, p.Animal, p.Breed, 
                   a.RequestDate, a.AdoptionStatus as status, 
                   a.AppointmentDate, a.ReasonForChoosing
            FROM Adoption a
            JOIN Pet p ON a.PetID = p.PetID
            WHERE a.Username = ?
            ORDER BY a.RequestDate DESC";
    
    $stmt = mysqli_prepare($conn, $sql);
    mysqli_stmt_bind_param($stmt, "s", $username);
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);
    
    // Create XML dynamically from the result set
    $xml = new DOMDocument('1.0', 'UTF-8');
    $xml->formatOutput = true;
    $root = $xml->createElement('adoptions');
    $xml->appendChild($root);
    
    while ($row = mysqli_fetch_assoc($result)) {
        $adoption = $xml->createElement('adoption');
        $adoption->appendChild($xml->createElement('adoptionID', $row['AdoptionID']));
        $adoption->appendChild($xml->createElement('petName', htmlspecialchars($row['PetName'])));
        $adoption->appendChild($xml->createElement('animal', htmlspecialchars($row['Animal'])));
        $adoption->appendChild($xml->createElement('breed', htmlspecialchars($row['Breed'])));
        $adoption->appendChild($xml->createElement('requestDate', date('Y-m-d', strtotime($row['RequestDate']))));
        $adoption->appendChild($xml->createElement('status', $row['status']));
        $adoption->appendChild($xml->createElement('appointmentDate', 
            $row['AppointmentDate'] ? date('Y-m-d', strtotime($row['AppointmentDate'])) : ''));
        $adoption->appendChild($xml->createElement('reasonForChoosing', htmlspecialchars($row['ReasonForChoosing'])));
        $root->appendChild($adoption);
    }
    
    mysqli_stmt_close($stmt);
    mysqli_close($conn);
    
    return $xml->saveXML();
}

// Check if SOAP extension is loaded
if (!extension_loaded('soap')) {
    die("SOAP extension is not loaded. Please enable it in php.ini");
}

// Create SOAP Server in non-WSDL mode
$server = new SoapServer(null, array('uri' => 'http://localhost/WebAssignment/Home/AdoptionList/'));

// Register the function
$server->addFunction('getAdoptionsXMLByUser');

// Handle SOAP request
$server->handle();
?>