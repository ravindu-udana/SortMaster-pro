import java.io.File;

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

    public SortingGUI() {
        setTitle("SortMaster Pro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        initComponents();
    }
    private void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(15, 15)); // Increased gap
        ((JPanel)contentPane).setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(245, 247, 250)); // Light Gray-Blue Background

        // --- Top Panel (File Selection) ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel sectionTitle1 = new JLabel("1. Data Source");
        sectionTitle1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionTitle1.setForeground(new Color(50, 50, 50));
        topPanel.add(sectionTitle1, BorderLayout.NORTH);

        JPanel fileSelectionPanel = new JPanel(new BorderLayout(10, 0));
        fileSelectionPanel.setOpaque(false);
        fileSelectionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton fileBtn = new JButton("Select CSV File");
        fileBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fileBtn.setBackground(new Color(240, 240, 240));
        fileBtn.setFocusPainted(false);
        
        fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        fileLabel.setForeground(Color.GRAY);

        fileBtn.addActionListener(e -> selectFile());

        fileSelectionPanel.add(fileBtn, BorderLayout.WEST);
        fileSelectionPanel.add(fileLabel, BorderLayout.CENTER);
        topPanel.add(fileSelectionPanel, BorderLayout.CENTER);
        
        JPanel topContainer = new JPanel(new GridLayout(2, 1, 0, 15)); // Gap between panels
        topContainer.setOpaque(false);
        topContainer.add(topPanel);


        // --- Controls Panel (Column & Run) ---
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel sectionTitle2 = new JLabel("2. Configuration");
        sectionTitle2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionTitle2.setForeground(new Color(50, 50, 50));
        controlsPanel.add(sectionTitle2, BorderLayout.NORTH);

        JPanel controlsInner = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlsInner.setOpaque(false);

        JLabel colLabel = new JLabel("Target Column:");
        colLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        controlsInner.add(colLabel);

        columnSelect = new JComboBox<>();
        columnSelect.setPreferredSize(new Dimension(220, 30));
        columnSelect.setBackground(Color.WHITE);
        columnSelect.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        columnSelect.setEnabled(false);
        controlsInner.add(columnSelect);

        runButton = new JButton("Run Analysis");
        runButton.setEnabled(false);
        runButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        runButton.setBackground(Color.BLACK);
        runButton.setForeground(Color.WHITE);
        runButton.setOpaque(true);
        runButton.setContentAreaFilled(true);
        runButton.setBorderPainted(false);
        runButton.setFocusPainted(false);
        runButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        runButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        runButton.setToolTipText("Click to start sorting performance validation");

        runButton.addActionListener(e -> runAnalysis());

        controlsInner.add(runButton);
        controlsPanel.add(controlsInner, BorderLayout.CENTER);
        topContainer.add(controlsPanel);

        contentPane.add(topContainer, BorderLayout.NORTH);

    

}