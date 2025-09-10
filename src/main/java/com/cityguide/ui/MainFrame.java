package com.cityguide.ui;

import com.cityguide.api.GeminiApiClient;
import com.cityguide.auth.LoginDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private JTextField cityTextField;
    private JButton searchButton;
    private JTextArea resultArea;
    private JComboBox<String> topicComboBox;
    private JLabel statusLabel;

    private GeminiApiClient apiClient;
    private boolean isLoggedIn = false;

    public MainFrame() {
        super("City Guide");

        apiClient = new GeminiApiClient();

        // Setup UI components
        setupUI();

        // Setup menu
        setupMenu();

        // Frame setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame
    }

    private void setupUI() {
        // Top panel for input
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("City:"));
        cityTextField = new JTextField(20);
        topPanel.add(cityTextField);

        String[] topics = {"Famous Food", "Famous Tourist Spots", "Famous Restaurants", "Nightlife", "Shopping"};
        topicComboBox = new JComboBox<>(topics);
        topPanel.add(new JLabel("Topic:"));
        topPanel.add(topicComboBox);

        searchButton = new JButton("Search");
        topPanel.add(searchButton);

        // Center panel for results
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Status bar
        statusLabel = new JLabel("Status: Not logged in");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        // Add panels to frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(statusLabel, BorderLayout.SOUTH);

        // Add action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loginItem = new JMenuItem("Login");
        loginItem.addActionListener(e -> performLogin());
        fileMenu.add(loginItem);

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> performLogout());
        fileMenu.add(logoutItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void performSearch() {
        String city = cityTextField.getText();
        if (city.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String topic = (String) topicComboBox.getSelectedItem();
        if ("Culture".equals(topic) && !isLoggedIn) {
            JOptionPane.showMessageDialog(this, "You must be logged in to view information about culture.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        resultArea.setText("Searching for " + topic + " in " + city + "...");

        // Run API call in a background thread to keep the UI responsive
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return apiClient.getCityInfo(city, topic);
            }

            @Override
            protected void done() {
                try {
                    resultArea.setText(get());
                } catch (Exception ex) {
                    resultArea.setText("An error occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void performLogin() {
        LoginDialog loginDlg = new LoginDialog(this);
        loginDlg.setVisible(true);
        // if login successful
        if (loginDlg.isSucceeded()) {
            isLoggedIn = true;
            statusLabel.setText("Status: Logged in as 'admin'");
            updateTopicComboBox();
        }
    }

    private void performLogout() {
        isLoggedIn = false;
        statusLabel.setText("Status: Not logged in");
        updateTopicComboBox();
        JOptionPane.showMessageDialog(this, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTopicComboBox() {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) topicComboBox.getModel();
        model.removeAllElements();
        model.addElement("Famous Food");
        model.addElement("Famous Tourist Spots");
        model.addElement("Famous Restaurants");
        model.addElement("Nightlife");
        model.addElement("Shopping");
        if (isLoggedIn) {
            model.addElement("Culture");
        }
    }

    public static void main(String[] args) {
        // Set a more modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
