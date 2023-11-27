import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.filechooser.FileSystemView;

public class RecursiveLister extends JFrame {
    private JTextArea textArea; // TextArea to display file paths
    private JLabel pathLabel; // Label to display the selected directory path
    private JProgressBar progressBar; // Progress bar to indicate progress
    private int fileCount; // Counter for the number of files listed

    public RecursiveLister() {
        createUI(); // Initialize the UI components
    }

    private void createUI() {
        // Create UI components
        JButton startButton = new JButton("Select Directory"); // Button to open file chooser
        JButton quitButton = new JButton("Quit"); // Button to quit the application
        JButton clearButton = new JButton("Clear"); // Button to clear the text area
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea); // Scroll pane for text area
        pathLabel = new JLabel("No directory selected");
        progressBar = new JProgressBar();
        JLabel titleLabel = new JLabel("Recursive Lister", JLabel.CENTER); // Title label

        // Set layout
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for path label and progress bar
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(pathLabel);
        bottomPanel.add(progressBar);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button panel for start, quit, and clear buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Action listeners for buttons
        startButton.addActionListener(this::startAction);
        quitButton.addActionListener(e -> System.exit(0));
        clearButton.addActionListener(e -> {
            textArea.setText(""); // Clear the text area
            pathLabel.setText("No directory selected");
            progressBar.setValue(0); // Reset progress bar
            fileCount = 0;
        });

        // Set frame settings
        applyCustomLookAndFeel();
        setTitle("Recursive Lister");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Method to apply a custom look and feel
    private void applyCustomLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace(); // Print any look and feel exception
        }
    }

    // Action to start directory selection and file listing
    private void startAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Set to directories only
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fileChooser.getSelectedFile();
            pathLabel.setText("Selected Directory: " + selectedDir.getAbsolutePath());
            progressBar.setValue(0);
            fileCount = 0;
            listFiles(selectedDir); // Start listing files
            JOptionPane.showMessageDialog(this, "Total files/directories listed: " + fileCount);
        }
    }

    // Recursive method to list files
    private void listFiles(File dir) {
        File[] files = dir.listFiles(); // List all files in the directory
        if (files != null) {
            for (File file : files) {
                // Use file icons to differentiate between file types
                Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                textArea.insert(icon + " " + file.getAbsolutePath() + "\n", textArea.getText().length());
                fileCount++;
                progressBar.setValue(Math.min(100, (fileCount * 100) / files.length));
                if (file.isDirectory()) {
                    listFiles(file); // Recurse if it's a directory
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecursiveLister().setVisible(true));
    }
}
