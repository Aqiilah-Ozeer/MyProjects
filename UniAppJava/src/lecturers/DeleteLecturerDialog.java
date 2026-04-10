package lecturers;

import javax.swing.*;
import java.awt.*;

public class DeleteLecturerDialog extends JDialog {
    private LecturerDAO lecturerDAO;
    private LecturerListFrame parentFrame;
    private String lecturerId;
    private String lecturerName;

    public DeleteLecturerDialog(LecturerListFrame parent, String lecturerId, String lecturerName) {
        super(parent, "Delete Lecturer", true);
        this.parentFrame = parent;
        this.lecturerId = lecturerId;
        this.lecturerName = lecturerName;
        lecturerDAO = new LecturerDAO();

        setSize(400, 150);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel message = new JLabel("<html><div style='text-align:center;'>" +
                "Are you sure you want to delete Lecturer: <b>" + lecturerId + " - " + lecturerName + "</b>?" +
                "</div></html>");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        yesButton.addActionListener(e -> {
            lecturerDAO.deleteLecturer(lecturerId);
            JOptionPane.showMessageDialog(this, "Lecturer " + lecturerId + " deleted successfully!");
            parentFrame.loadLecturers();
            dispose();
        });

        noButton.addActionListener(e -> dispose());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
