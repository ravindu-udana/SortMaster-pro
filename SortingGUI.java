import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class SortingGUI extends JFrame {

    private JLabel fileLabel;
    private JComboBox<String> columnSelect;
    private JButton runButton;
    private JPanel resultsPanel;
    private File currentFile;
    private JLabel statusLabel;

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            fileLabel.setText(currentFile.getName());
            fileLabel.setForeground(Color.BLACK);
            loadColumns();
        }
    }

    private void loadColumns() {
        columnSelect.removeAllItems();
        try {
            List<String> numericHeaders = CSVParser.getNumericHeaders(currentFile);

            if (numericHeaders.isEmpty()) {
                if (CSVParser.getHeaders(currentFile).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "The file appears to be empty.", "Invalid File",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No numeric columns found in this CSV.", "No Data Found",
                            JOptionPane.WARNING_MESSAGE);
                }
                runButton.setEnabled(false);
                columnSelect.setEnabled(false);
                statusLabel.setText("Invalid file. Please select a CSV with numeric data.");
                return;
            }

            for (String header : numericHeaders) {
                columnSelect.addItem(header);
            }

            columnSelect.setEnabled(true);
            runButton.setEnabled(true);
            statusLabel.setText("File loaded. Select a numeric column to sort.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Read Error",
                    JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error loading file.");
        }
    }
}