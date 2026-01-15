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

    private void runAnalysis() {
        String selectedCol = (String) columnSelect.getSelectedItem();
        if (selectedCol == null)
            return;

        statusLabel.setText("Reading data...");
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();

        new SwingWorker<List<Result>, Void>() {
            @Override
            protected List<Result> doInBackground() throws Exception {
                double[] originalData = CSVParser.getColumnData(currentFile, selectedCol);

                if (originalData.length < 2) {
                    throw new Exception("Not enough numeric data in this column.");
                }

                List<Result> results = new java.util.ArrayList<>();
                results.add(benchmark("Insertion Sort", originalData, SortingAlgorithms::insertionSort));
                results.add(benchmark("Shell Sort", originalData, SortingAlgorithms::shellSort));
                results.add(benchmark("Merge Sort", originalData, SortingAlgorithms::mergeSort));
                results.add(benchmark("Quick Sort", originalData, SortingAlgorithms::quickSort));
                results.add(benchmark("Heap Sort", originalData, SortingAlgorithms::heapSort));

                results.sort((a, b) -> Double.compare(a.timeMs, b.timeMs));
                return results;
            }

            @Override
            protected void done() {
                try {
                    List<Result> results = get();
                    displayResults(results);
                    statusLabel.setText("Analysis Complete.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(SortingGUI.this, e.getMessage(), "Analysis Error",
                            JOptionPane.ERROR_MESSAGE);
                    statusLabel.setText("Error during analysis.");
                }
            }
        }.execute();
    }

    private static class Result {
        String name;
        double timeMs;

        public Result(String name, double timeMs) {
            this.name = name;
            this.timeMs = timeMs;
        }
    }

    private Result benchmark(String name, double[] original, java.util.function.Consumer<double[]> algo) {
        double[] arr = Arrays.copyOf(original, original.length);
        long start = System.nanoTime();
        algo.accept(arr);
        long end = System.nanoTime();
        return new Result(name, (end - start) / 1_000_000.0);
    }
}