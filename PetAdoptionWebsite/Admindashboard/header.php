<div class="top-right">
    <div class="header-content">
        <span class="admin-username">
            <?php 
            session_start();
            if (!isset($_SESSION['adminusername'])) {
                header("Location: AdminLogin.php");
                exit;
            } else {
                echo htmlspecialchars($_SESSION['adminusername']);
            }
            ?>
        </span>
        <a href="AdminLogout.php" class="logout-btn">Logout</a>
    </div>
</div>