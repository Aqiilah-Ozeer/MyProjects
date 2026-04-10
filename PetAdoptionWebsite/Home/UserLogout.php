<?php
    session_start();
    
    // Destroy the session
    session_destroy();
    
    // Clear the session array
    $_SESSION = array();
    
    // Redirect to login page
    header("Location: UserLogin.php");
    exit();
?>
