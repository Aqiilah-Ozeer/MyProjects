<?php

include 'db.php'; 

$main = $_GET['main'] ?? ''; 
$sub = $_GET['sub'] ?? ''; 

$conditions = []; 
$params = [];
$types = "";

if ($sub) {
    $conditions[] = "Breed = ?";
    $params[] = $sub;
    $types .= "s";
} elseif ($main) {
    $conditions[] = "Animal = ?";
    $params[] = $main;
    $types .= "s";
}

$whereClause = $conditions ? "WHERE " . implode(" AND ", $conditions) : "";

$sql = "SELECT PetID, PetName, Animal, Breed, PetStatus FROM Pet $whereClause ORDER BY PetName ASC";
$stmt = $conn->prepare($sql);

if ($types) {
    $stmt->bind_param($types, ...$params);
}

$stmt->execute();
$result = $stmt->get_result();

$imagePath = 'images/';
$extensions = ['jpeg', 'jpg', 'png', 'gif', 'webp']; 

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $petName = strtolower(str_replace(' ', '_', $row['PetName'])); 
        $imageFile = 'default.jpg';
        
        // Image check (jpeg/jpg recognized)
        foreach ($extensions as $ext) {
            $match = glob("$imagePath$petName.$ext");
            if ($match) {
                $imageFile = basename($match[0]);
                break;
            }
        }
        
        $status = htmlspecialchars($row['PetStatus']);
        $statusClass = ($status == 'Adopted') ? 'status-adopted' : 'status-available';

       
        echo "
        <div class='grid-card'>
          <a href='petinfo.php?id={$row['PetID']}'>
            <div class='card-image-wrapper'>
                <img src='images/$imageFile' alt='{$row['PetName']}'>
                <span class='pet-status $statusClass'>$status</span>
            </div>
            <div class='card-details'>
                <h3>" . htmlspecialchars($row['PetName']) . "</h3>
                <p class='species-breed'>**" . htmlspecialchars($row['Breed']) . "**</p>
            </div>
          </a>
        </div>";
    }
} else {
    echo "<p class='no-results'>😔 No pets match your current filter criteria.</p>";
}

$stmt->close();
$conn->close();
?>