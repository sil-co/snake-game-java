package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

// It extends JPanel (so we can draw graphics in the window)
// It implements ActionListener (so we can update the game every tick using a Timer)
// It implements KeyListener (so we can capture keyboard input to move the snake)
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // Size (in pixels) of each "tile" (snake body part and food)
    private final int TILE_SIZE = 25; // Each square size
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    // Maximum number of tiles that can fit in the game area.
    // This also represents the maximum possible length of the snake
    // (if it fills the whole screen).
    private final int ALL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);

    // Array holding the X positions of the snake's body segments.
    // x[0] = head of the snake, x[1] = first body part, etc.
    private final int[] x = new int[ALL_TILES]; // Snake X positions

    // Array holding the Y positions of the snake's body segments.
    // y[0] = head, y[1] = body, aligned with x[].
    private final int[] y = new int[ALL_TILES]; // Snake Y positions

    private int bodyParts = 3; // Initial snake length
    private int foodEaten; // Score

    // Current X position of the food (randomly generated).
    private int foodX; // Food X
    // Current Y position of the food (randomly generated).
    private int foodY; // Food Y

    private char direction = 'R'; // U D L R
    private boolean running = false;

    // Swing Timer object that triggers game updates at a fixed interval
    // (for movement, collision checks, repainting).
    private Timer timer;
    private Random random;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        random = new Random();
        startGame();
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(200, this); // Speed (100 ms per move)
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw food
            g.setColor(Color.red);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green); // Head
                } else {
                    g.setColor(new Color(45, 180, 0)); // Body
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            // Draw score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + foodEaten, 10, 20);
        } else {
            gameOver(g);
        }
    }

    public void newFood() {
        foodX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] = y[0] - TILE_SIZE; break;
            case 'D': y[0] = y[0] + TILE_SIZE; break;
            case 'L': x[0] = x[0] - TILE_SIZE; break;
            case 'R': x[0] = x[0] + TILE_SIZE; break;
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollision() {
        // Head hits body
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // Head hits walls
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - fm.stringWidth("Game Over")) / 2, HEIGHT / 2);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Final Score: " + foodEaten, WIDTH / 2 - 70, HEIGHT / 2 + 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
