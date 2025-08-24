package org.example;

import javax.swing.*;
        import java.awt.*;

public class SimpleGame {
    private int score = 0;

    public SimpleGame() {
        // Frame (window)
        JFrame frame = new JFrame("Click the Button Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Score label
        JLabel scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Button
        JButton button = new JButton("Click me!");
        button.setFont(new Font("Arial", Font.BOLD, 18));

        // Action listener for button
        button.addActionListener(e -> {
            score++;
            scoreLabel.setText("Score: " + score);
        });

        // Add to frame
        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.add(button, BorderLayout.CENTER);

        // Show window
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run game on UI thread
        SwingUtilities.invokeLater((SimpleGame::new));
    }
}

