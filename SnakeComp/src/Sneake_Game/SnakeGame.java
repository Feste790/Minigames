package Sneake_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private int width;
    private int height;
    private int dotSize;
    private int delay;
    private Color snakeColor;
    private Color appleColor;
    private Color backgroundColor;
    private Color obstacleColor;
    private LinkedList<Point> snake;
    private Point apple;
    private LinkedList<Point> obstacles;
    private boolean left = false, right = true, up = false, down = false;
    private boolean inGame = false;
    private boolean showStartScreen = true;
    private int selectedDifficulty = 1; // 0: Easy, 1: Medium, 2: Hard
    private int score = 0;
    private Timer timer;
    private Runnable onExit;
    private SnakeConfig snakeConfig;
    private JLabel scoreLabel;
    private static final Path SCORES_PATH = Paths.get(System.getProperty("user.home"), ".snake", "snakeScores.txt");
    private JPanel gamePanel; // Dodano zmienną do przechowywania referencji do panelu gry

    public SnakeGame(){
        this.snakeConfig = new SnakeConfig();
        try {
            this.snakeConfig.loadSettings();
        } catch (IOException e) {
            throw new RuntimeException("Błąd wczytywania konfiguracji: " + e.getMessage(), e);
        }

        this.selectedDifficulty = snakeConfig.getDifficulty();
        this.width = snakeConfig.getWidth(selectedDifficulty);
        this.height = snakeConfig.getHeight(selectedDifficulty);
        this.dotSize = snakeConfig.getDotSize(selectedDifficulty);
        this.delay = snakeConfig.getDelay(selectedDifficulty);
        this.snakeColor = snakeConfig.getSnakeColor();
        this.appleColor = snakeConfig.getAppleColor();
        this.backgroundColor = snakeConfig.getBackgroundColor();
        this.obstacleColor = snakeConfig.getObstacleColor();

        this.initGUI();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeSnake();
            }
        });
    }

    public SnakeGame(Runnable onExit) {
        this.onExit = onExit;
        this.snakeConfig = new SnakeConfig();
        try {
            this.snakeConfig.loadSettings();
        } catch (IOException e) {
            throw new RuntimeException("Błąd wczytywania konfiguracji: " + e.getMessage(), e);
        }

        this.selectedDifficulty = snakeConfig.getDifficulty();
        this.width = snakeConfig.getWidth(selectedDifficulty);
        this.height = snakeConfig.getHeight(selectedDifficulty);
        this.dotSize = snakeConfig.getDotSize(selectedDifficulty);
        this.delay = snakeConfig.getDelay(selectedDifficulty);
        this.snakeColor = snakeConfig.getSnakeColor();
        this.appleColor = snakeConfig.getAppleColor();
        this.backgroundColor = snakeConfig.getBackgroundColor();
        this.obstacleColor = snakeConfig.getObstacleColor();

        this.initGUI();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeSnake();
            }
        });
    }

    private void initGUI() {
        setTitle("Snake Game");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(leftPanel, BorderLayout.WEST);

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        JLabel difficultyLabel = new JLabel("Difficulty: " + getDifficultyString(), SwingConstants.RIGHT);
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(difficultyLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (showStartScreen) {
                    drawStartScreen(g);
                } else if (inGame) {
                    drawGame(g);
                } else {
                    drawGameOverScreen(g);
                }
            }
        };
        gamePanel.setPreferredSize(new Dimension(width, height));
        gamePanel.setBackground(backgroundColor);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
        add(gamePanel, BorderLayout.CENTER);

        setSize(width, height + 80);
        setLocationRelativeTo(null);
        setVisible(true);

        // Ustawienie fokusu na panel gry
        gamePanel.requestFocusInWindow();
        // Dodanie słuchacza fokusu, aby przywrócić fokus po kliknięciu
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gamePanel.requestFocusInWindow();
            }
        });
    }

    private String getDifficultyString() {
        return switch (selectedDifficulty) {
            case 0 -> "Easy";
            case 1 -> "Medium";
            case 2 -> "Hard";
            default -> "Medium";
        };
    }

    private void initializeGame() {
        snake = new LinkedList<>();
        snake.add(new Point(100, 100));
        spawnApple();
        obstacles = new LinkedList<>();
        if (selectedDifficulty == 2) { // Hard
            spawnObstacles();
        }
        score = 0;
        scoreLabel.setText("Score: 0");

        left = false;
        right = true;
        up = false;
        down = false;

        inGame = true;
        showStartScreen = false;

        timer = new Timer(delay, this);
        timer.start();

        repaint();
        gamePanel.requestFocusInWindow(); // Ponowne zapewnienie fokusu po inicjalizacji gry
    }

    private void drawStartScreen(Graphics g) {
        String message = "Snake Game";
        String startMessage = "Press ENTER to Start";

        g.setColor(snakeColor);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        g.drawString(message, (width - metrics.stringWidth(message)) / 2, height / 2 - 40);

        g.setFont(new Font("Helvetica", Font.PLAIN, 20));
        metrics = g.getFontMetrics(g.getFont());
        g.drawString(startMessage, (width - metrics.stringWidth(startMessage)) / 2, height / 2 + 20);
    }

    private void drawGame(Graphics g) {
        g.setColor(snakeColor);
        for (Point p : snake) {
            g.fillRect(p.x, p.y, dotSize, dotSize);
        }

        g.setColor(appleColor);
        g.fillRect(apple.x, apple.y, dotSize, dotSize);

        if (selectedDifficulty == 2) {
            g.setColor(obstacleColor);
            for (Point p : obstacles) {
                g.fillRect(p.x, p.y, dotSize, dotSize);
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawGameOverScreen(Graphics g) {
        String gameOverMessage = "Game Over";
        String scoreMessage = "Final Score: " + score;
        String restartMessage = "Press ENTER to Play Again";

        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        g.drawString(gameOverMessage, (width - metrics.stringWidth(gameOverMessage)) / 2, height / 2 - 40);

        g.setFont(new Font("Helvetica", Font.PLAIN, 20));
        metrics = g.getFontMetrics(g.getFont());
        g.drawString(scoreMessage, (width - metrics.stringWidth(scoreMessage)) / 2, height / 2);
        g.drawString(restartMessage, (width - metrics.stringWidth(restartMessage)) / 2, height / 2 + 40);
    }

    private void spawnApple() {
        Random rand = new Random();
        int randPos = width / dotSize;
        int x = rand.nextInt(randPos) * dotSize;
        int y = rand.nextInt(randPos) * dotSize;
        apple = new Point(x, y);
    }

    private void spawnObstacles() {
        Random rand = new Random();
        obstacles.clear();
        int randPos = width / dotSize;
        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(randPos) * dotSize;
            int y = rand.nextInt(randPos) * dotSize;
            obstacles.add(new Point(x, y));
        }
    }

    private void move() {
        if (snake.isEmpty()) return;

        Point head = snake.getFirst();
        Point newHead = null;

        if (left) newHead = new Point(head.x - dotSize, head.y);
        else if (right) newHead = new Point(head.x + dotSize, head.y);
        else if (up) newHead = new Point(head.x, head.y - dotSize);
        else if (down) newHead = new Point(head.x, head.y + dotSize);

        if (selectedDifficulty == 0) { // Easy
            if (newHead.x < 0) newHead.x = width - dotSize;
            if (newHead.x >= width) newHead.x = 0;
            if (newHead.y < 0) newHead.y = height - dotSize;
            if (newHead.y >= height) newHead.y = 0;
        }

        snake.addFirst(newHead);
        if (newHead.equals(apple)) {
            score++;
            scoreLabel.setText("Score: " + score);
            spawnApple();
        } else {
            snake.removeLast();
        }
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        if (selectedDifficulty != 0) { // Not Easy
            if (head.x < 0 || head.x >= width || head.y < 0 || head.y >= height) inGame = false;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) inGame = false;
        }

        if (selectedDifficulty == 2) { // Hard
            for (Point obstacle : obstacles) {
                if (head.equals(obstacle)) inGame = false;
            }
        }

        if (!inGame && timer != null) {
            timer.stop();
            saveGameLog();
            int result = JOptionPane.showConfirmDialog(null, "Game Over! Final Score: " + score + "\nRestart?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                restartCurrentGame();
            } else {
                closeSnake();
            }
        }
    }

    private void saveGameLog() {
        try {
            Path configDir = SCORES_PATH.getParent();
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            String logEntry = String.format("%s,%s,%d", snakeConfig.getPlayer(), getDifficultyString(), score);
            Files.write(SCORES_PATH, (logEntry + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.err.println("Błąd zapisu logu: " + ex.getMessage());
        }
    }

    private void restartCurrentGame() {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame(this.onExit);
            dispose();
        });
    }

    private void closeSnake() {
        //System.out.println("Zamykanie SnakeGame na wątku: " + Thread.currentThread().getName());
        if (onExit != null) {
            //System.out.println("Wywołanie onExit z SnakeGame");
            onExit.run();
        }
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ENTER) {
            if (showStartScreen) {
                initializeGame();
            } else if (!inGame) {
                restartCurrentGame();
            }
        }

        if (inGame) {
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            } else if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
        }
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}