import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main extends JFrame {
    private JTextArea originalArea, filteredArea;
    private JTextField searchField;
    private Path currentFile;
    private List<String> allLines = new ArrayList<>();

    public Main() {
        setTitle("Data Stream Search");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalArea = new JTextArea();
        filteredArea = new JTextArea();
        originalArea.setEditable(false);
        filteredArea.setEditable(false);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(originalArea), new JScrollPane(filteredArea));
        split.setDividerLocation(350);
        add(split, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        searchField = new JTextField(15);
        JButton loadBtn = new JButton("Load File");
        JButton searchBtn = new JButton("Search");
        JButton quitBtn = new JButton("Quit");

        bottom.add(new JLabel("Search:"));
        bottom.add(searchField);
        bottom.add(loadBtn);
        bottom.add(searchBtn);
        bottom.add(quitBtn);
        add(bottom, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> loadFile());
        searchBtn.addActionListener(e -> searchFile());
        quitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void loadFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile().toPath();
            try {
                allLines = Files.readAllLines(currentFile);
                originalArea.setText(String.join("\n", allLines));
                filteredArea.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            }
        }
    }

    private void searchFile() {
        if (allLines.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load a file first!");
            return;
        }
        String term = searchField.getText().trim().toLowerCase();
        if (term.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a search term.");
            return;
        }

        List<String> filtered = allLines.stream()
                .filter(l -> l.toLowerCase().contains(term))
                .collect(Collectors.toList());
        filteredArea.setText(String.join("\n", filtered));
    }

    public static void main(String[] args) {
        new Main();
    }
}