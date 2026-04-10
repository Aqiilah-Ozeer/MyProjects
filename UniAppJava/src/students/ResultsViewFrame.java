package students;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import results.*;
import java.util.ArrayList;

public class ResultsViewFrame extends JFrame {
    JTable myresultsTable;
    ResultDAO resultDAO;

    public ResultsViewFrame(int studentId) {
        setTitle("My Results");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        resultDAO = new ResultDAO();
        
        String[] columnNames = new String[] {"Module Name", "Marks", "Grade" };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        myresultsTable = new JTable(model);
        add(new JScrollPane(myresultsTable));

        loadResults(studentId);
    }

    private void loadResults(int studentId) {
        DefaultTableModel model = (DefaultTableModel) myresultsTable.getModel();
        model.setRowCount(0);
        ArrayList<Result> results = resultDAO.getStudentResult(studentId);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No results found for this student.", 
            "No Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Result r : results) {
                model.addRow(new Object[] { r.getModuleName(), r.getMarks(), r.getGrade() });
            }
        }
    }
}
