package lecturers;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import models.User;
import java.util.HashSet;
import java.util.Set;

public class ViewCoursesModulesFrame extends JFrame {
    private User lecturer;
    private JTree tree;
    private DefaultMutableTreeNode root;
    private JLabel statusLabel;
    
    public ViewCoursesModulesFrame(User lecturer) {
        this.lecturer = lecturer;
        
        setTitle("Courses and Modules - " + lecturer.getUsername());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel headerLabel = new JLabel("Your Courses and Modules (Read Only)", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Create Tree Panel
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        root = new DefaultMutableTreeNode("Courses You Teach");
        
        tree = new JTree(root);
        tree.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tree.setRowHeight(25);
        tree.setEditable(false);
        
        // Customize tree appearance
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setFont(new Font("SansSerif", Font.PLAIN, 14));
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        renderer.setLeafIcon(null);
        
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        treePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(treePanel, BorderLayout.CENTER);
        
        // Status Panel
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setLayout(new BorderLayout());
        
        statusLabel = new JLabel(" Loading data... ");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        add(statusPanel, BorderLayout.NORTH);
        
        // Button Panel - Close Button
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load the data after all UI components are created
        loadCoursesAndModules();
        
        // Expand all nodes after tree is loaded
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tree.getRowCount(); i++) {
                tree.expandRow(i);
            }
        });
    }
    
    private void loadCoursesAndModules() {
        root.removeAllChildren();
        
        // SQL query to get courses and modules for this lecturer
        String sql = "SELECT DISTINCT " +
                    "c.id AS course_id, " +
                    "c.name AS course_name, " +
                    "c.description AS course_description, " +
                    "c.credits AS course_credits, " +
                    "m.id AS module_id, " +
                    "m.name AS module_name, " +
                    "m.schedule AS module_schedule, " +
                    "m.credits AS module_credits " +
                    "FROM Courses c " +
                    "INNER JOIN Modules m ON c.id = m.course_id " +
                    "WHERE m.lecturer_id = ? " +
                    "ORDER BY c.name, m.name";
        
        Connection conn = DBConnection.getInstance().getConnection();
        int totalCourses = 0;
        int totalModules = 0;
        
        // Use Set to track added modules to avoid duplicates
        Set<String> addedModules = new HashSet<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Use username as the lecturer ID
            String lecturerId = lecturer.getUsername();
            System.out.println("Loading courses for lecturer ID: " + lecturerId);
            
            stmt.setString(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            
            int currentCourseId = -1;
            DefaultMutableTreeNode courseNode = null;
            
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                int courseCredits = rs.getInt("course_credits");
                String courseDesc = rs.getString("course_description");
                int moduleId = rs.getInt("module_id");
                String moduleName = rs.getString("module_name");
                String schedule = rs.getString("module_schedule");
                int moduleCredits = rs.getInt("module_credits");
                
                // Debug print to verify module ID is being fetched
                System.out.println("Found module: ID=" + moduleId + ", Name=" + moduleName);
                
                // Create a unique key for the module to avoid duplicates
                String moduleKey = courseId + "|" + moduleId;
                
                // Check if it's a new course
                if (currentCourseId != courseId) {
                    currentCourseId = courseId;
                    totalCourses++;
                    
                    // Create course node with details
                    String courseDisplay = courseName + " [" + courseCredits + " credits]";
                    courseNode = new DefaultMutableTreeNode(courseDisplay);
                    
                    // Add course description as child node
                    if (courseDesc != null && !courseDesc.isEmpty()) {
                        DefaultMutableTreeNode descNode = new DefaultMutableTreeNode("Description: " + courseDesc);
                        courseNode.add(descNode);
                    }
                    
                    root.add(courseNode);
                    addedModules.clear(); // Clear modules set for new course
                }
                
                // Add module node under the course with module ID displayed BEFORE module name
                if (moduleName != null && !addedModules.contains(moduleKey)) {
                    addedModules.add(moduleKey);
                    totalModules++;
                    String scheduleText = (schedule != null && !schedule.isEmpty()) ? schedule : "TBA";
                    
                    // Display module ID right before the module name
                    String moduleDisplay = "[ID: " + moduleId + "] " + moduleName + " | Schedule: " + scheduleText + " | Credits: " + moduleCredits;
                    
                    DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode(moduleDisplay);
                    if (courseNode != null) {
                        courseNode.add(moduleNode);
                    }
                }
            }
            
            rs.close();
            
            // Update status
            if (statusLabel != null) {
                if (totalCourses == 0) {
                    statusLabel.setText(" No courses assigned to you ");
                } else {
                    statusLabel.setText(" Total: " + totalCourses + " Course(s) | " + totalModules + " Module(s) ");
                }
            }
            
            // If no courses found
            if (totalCourses == 0) {
                DefaultMutableTreeNode emptyNode = new DefaultMutableTreeNode("No courses assigned to you");
                root.add(emptyNode);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            if (statusLabel != null) {
                statusLabel.setText(" Error loading data: " + e.getMessage());
            }
            DefaultMutableTreeNode errorNode = new DefaultMutableTreeNode("Error loading courses: " + e.getMessage());
            root.add(errorNode);
        }
        
        // Refresh tree
        tree.revalidate();
        tree.repaint();
    }
}