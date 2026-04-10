<?php
    session_start();
    
    // Destroy the session
    session_destroy();
    
    // Clear the session array
    $_SESSION = array();
    
    // Redirect to admin login page
    header("Location: AdminPawfectmatch.php");
    exit();
?>
