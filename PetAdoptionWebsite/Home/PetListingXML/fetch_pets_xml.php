<?php
// Prevent any output before XML header
ob_start();
error_reporting(E_ALL);
ini_set('display_errors', 0); // Don't display errors, output valid XML instead

header('Content-Type: application/xml; charset=utf-8');

include 'db.php';

$main = $_GET['main'] ?? '';
$sub = $_GET['sub'] ?? '';
$search = $_GET['search'] ?? '';

// Create XML document first
$xml = new DOMDocument('1.0', 'UTF-8');
$xml->formatOutput = true;
$root = $xml->createElement('pets');
$xml->appendChild($root);

// Check database connection
if (!$conn) {
    $error = $xml->createElement('error', 'Database connection failed');
    $root->appendChild($error);
    ob_end_clean();
    echo $xml->saveXML();
    exit;
}

$conditions = [];
$params = [];
$types = "";

if ($search) {
    $conditions[] = "(PetName LIKE ? OR Breed LIKE ? OR Animal LIKE ?)";
    $searchTerm = "%$search%";
    $params[] = $searchTerm;
    $params[] = $searchTerm;
    $params[] = $searchTerm;
    $types .= "sss";
} elseif ($sub) {
    $conditions[] = "Breed = ?";
    $params[] = $sub;
    $types .= "s";
} elseif ($main) {
    $conditions[] = "Animal = ?";
    $params[] = $main;
    $types .= "s";
}

$whereClause = $conditions ? "WHERE " . implode(" AND ", $conditions) : "";

$sql = "SELECT PetID, PetName, Animal, Breed, PetStatus, Age, AdoptionFee, ImagePath FROM Pet $whereClause ORDER BY PetName ASC";

try {
    $stmt = $conn->prepare($sql);
    
    if (!$stmt) {
        $error = $xml->createElement('error', 'Prepare failed: ' . $conn->error);
        $root->appendChild($error);
        ob_end_clean();
        echo $xml->saveXML();
        exit;
    }
    
    if ($types) {
        $stmt->bind_param($types, ...$params);
    }
    
    $stmt->execute();
    
    if ($stmt->error) {
        $error = $xml->createElement('error', 'Execute failed: ' . $stmt->error);
        $root->appendChild($error);
        ob_end_clean();
        echo $xml->saveXML();
        exit;
    }
    
    $result = $stmt->get_result();

    $imagePath = 'images/';
    $extensions = ['jpeg', 'jpg', 'png', 'gif', 'webp'];

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $pet = $xml->createElement('pet');

        $pet->appendChild($xml->createElement('id', htmlspecialchars($row['PetID'])));
        $pet->appendChild($xml->createElement('name', htmlspecialchars($row['PetName'])));
        $pet->appendChild($xml->createElement('animal', htmlspecialchars($row['Animal'])));
        $pet->appendChild($xml->createElement('breed', htmlspecialchars($row['Breed'])));
        $pet->appendChild($xml->createElement('status', htmlspecialchars($row['PetStatus'])));
        $pet->appendChild($xml->createElement('age', htmlspecialchars($row['Age'])));
        $pet->appendChild($xml->createElement('fee', htmlspecialchars($row['AdoptionFee'])));

        // Resolve image: use ImagePath if it's a URL, otherwise find local file
        if (!empty($row['ImagePath']) && strpos($row['ImagePath'], 'http') === 0) {
            $imageFile = $row['ImagePath'];
        } else {
            $petName = strtolower(str_replace(' ', '_', $row['PetName']));
            $imageFile = 'images/default.jpg';
            foreach ($extensions as $ext) {
                $match = glob("$imagePath$petName.$ext");
                if ($match) {
                    $imageFile = 'images/' . basename($match[0]);
                    break;
                }
            }
        }
        $pet->appendChild($xml->createElement('image', htmlspecialchars($imageFile)));

        $root->appendChild($pet);
    }
}

    $stmt->close();
    $conn->close();
    
} catch (Exception $e) {
    $error = $xml->createElement('error', 'Exception: ' . $e->getMessage());
    $root->appendChild($error);
}

ob_end_clean();
echo $xml->saveXML();
?>
