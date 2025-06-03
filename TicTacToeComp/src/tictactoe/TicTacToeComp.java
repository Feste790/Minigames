package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

public class TicTacToeComp extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton[][] buttons = new JButton[3][3];
    private boolean playerXTurn = true;
    private String gameMode;
    private int difficulty; // 0: EasyAI, 1: HardAI, 2: PVP
    private Random random = new Random();
    private ImageIcon xIcon;
    private ImageIcon oIcon;
    private int player1Wins = 0;
    private int player2Wins = 0;
    private JLabel scoreLabel;
    private String player1Name;
    private String player2Name;
    private TicTacToeConfig config;
    private Runnable onExit;
    private JPanel boardPanel;

    public TicTacToeComp(){
        this.config = new TicTacToeConfig();
        try {
            this.config.loadSettings();
        } catch (IOException e) {
            throw new RuntimeException("Błąd wczytywania konfiguracji: " + e.getMessage(), e);
        }

        this.gameMode = config.getGameMode();
        this.difficulty = config.getDifficulty();
        this.player1Name = config.getPlayer1();
        this.player2Name = gameMode.equals("PVP") ? config.getPlayer2() : "R2D2";

        // Wczytanie ikon
        ImageIcon originalXIcon = new ImageIcon(config.getCrossPNG());
        Image xImage = originalXIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        this.xIcon = new ImageIcon(xImage);
        ImageIcon originalOIcon = new ImageIcon(config.getCirclePNG());
        Image oImage = originalOIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        this.oIcon = new ImageIcon(oImage);

        initGUI();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeGame();
            }
        });
    }

    public TicTacToeComp(Runnable onExit) {

        this.onExit = onExit;
        this.config = new TicTacToeConfig();
        try {
            this.config.loadSettings();
        } catch (IOException e) {
            throw new RuntimeException("Błąd wczytywania konfiguracji: " + e.getMessage(), e);
        }

        this.gameMode = config.getGameMode();
        this.difficulty = config.getDifficulty();
        this.player1Name = config.getPlayer1();
        this.player2Name = gameMode.equals("PVP") ? config.getPlayer2() : "R2D2";

        // Wczytanie ikon
        ImageIcon originalXIcon = new ImageIcon(config.getCrossPNG());
        Image xImage = originalXIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        this.xIcon = new ImageIcon(xImage);
        ImageIcon originalOIcon = new ImageIcon(config.getCirclePNG());
        Image oImage = originalOIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        this.oIcon = new ImageIcon(oImage);

        initGUI();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeGame();
            }
        });
    }

    private void initGUI() {
        setTitle("Kółko i Krzyżyk");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> restartCurrentGame());
        leftPanel.add(resetButton);
        topPanel.add(leftPanel, BorderLayout.WEST);

        scoreLabel = new JLabel(player1Name + " (X): 0   |   " + player2Name + " (O): 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.decode(config.getTextColor()));
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        JLabel modeLabel = new JLabel("Mode: " + (gameMode.equals("PVP") ? "PVP" : (difficulty == 0 ? "EasyAI" : "HardAI")), SwingConstants.RIGHT);
        modeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(modeLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setFocusable(true);
        boardPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartCurrentGame();
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton();
                btn.setMinimumSize(new Dimension(150, 150));
                btn.setFocusPainted(false);
                final int finalI = i;
                final int finalJ = j;
                btn.addActionListener(e -> handleMove(finalI, finalJ));
                buttons[i][j] = btn;
                boardPanel.add(btn);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        boardPanel.requestFocusInWindow();
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardPanel.requestFocusInWindow();
            }
        });
    }

    private void handleMove(int row, int col) {
        if (buttons[row][col].getIcon() == null) {
            String symbol = playerXTurn ? "X" : "O";
            Icon icon = playerXTurn ? xIcon : oIcon;
            buttons[row][col].setIcon(icon);
            buttons[row][col].putClientProperty("symbol", symbol);

            if (checkWin(symbol)) {
                highlightWinningLine(symbol);
                String winner = playerXTurn ? player1Name : player2Name;
                endGame(winner + " wygrywa!");
                try {
                    config.saveScores(winner, gameMode.equals("PVP") ? "PVP" : (difficulty == 0 ? "EasyAI" : "HardAI"));
                } catch (IOException e) {
                    throw new RuntimeException("Błąd zapisu wyników: " + e.getMessage(), e);
                }
            } else if (isBoardFull()) {
                endGame("Remis!");
            } else {
                playerXTurn = !playerXTurn;
                if (!playerXTurn && (gameMode.equals("EasyAI")) || gameMode.equals("HardAI")) {
                    computerMove();
                }
            }
        }
        boardPanel.requestFocusInWindow();
    }

    private void computerMove() {
        if (difficulty == 0) {
            randomMove();
        } else if (difficulty == 1) {
            int[] bestMove = findBestMove();
            buttons[bestMove[0]][bestMove[1]].setIcon(oIcon);
            buttons[bestMove[0]][bestMove[1]].putClientProperty("symbol", "O");
        }

        if (checkWin("O")) {
            highlightWinningLine("O");
            endGame("Komputer (O) wygrał!");
            try {
                config.saveScores(player2Name, "HardAI");
            } catch (IOException e) {
                throw new RuntimeException("Błąd zapisu wyników: " + e.getMessage(), e);
            }
        } else if (isBoardFull()) {
            endGame("Remis!");
        } else {
            playerXTurn = true;
        }
        boardPanel.requestFocusInWindow();
    }

    private void randomMove() {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (buttons[row][col].getIcon() != null);
        buttons[row][col].setIcon(oIcon);
        buttons[row][col].putClientProperty("symbol", "O");
    }

    private boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (getSymbol(i, 0).equals(symbol) && getSymbol(i, 1).equals(symbol) && getSymbol(i, 2).equals(symbol)) {
                return true;
            }
            if (getSymbol(0, i).equals(symbol) && getSymbol(1, i).equals(symbol) && getSymbol(2, i).equals(symbol)) {
                return true;
            }
        }
        return getSymbol(0, 0).equals(symbol) && getSymbol(1, 1).equals(symbol) && getSymbol(2, 2).equals(symbol) ||
                getSymbol(0, 2).equals(symbol) && getSymbol(1, 1).equals(symbol) && getSymbol(2, 0).equals(symbol);
    }

    private String getSymbol(int row, int col) {
        Object symbol = buttons[row][col].getClientProperty("symbol");
        return symbol != null ? symbol.toString() : "";
    }

    private boolean isBoardFull() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                if (btn.getIcon() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void endGame(String message) {
        if (message.contains("X")) {
            player1Wins++;
        } else if (message.contains("O")) {
            player2Wins++;
        }
        scoreLabel.setText(player1Name + " (X): " + player1Wins + "   |   " + player2Name + " (O): " + player2Wins);

        int option = JOptionPane.showConfirmDialog(this, message + "\nChcesz zagrać ponownie?", "Koniec gry", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            closeGame();
        }
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setIcon(null);
                buttons[i][j].putClientProperty("symbol", null);
                buttons[i][j].setBackground(null);
            }
        }
        playerXTurn = true;
        boardPanel.requestFocusInWindow();
    }

    private void restartCurrentGame() {
        SwingUtilities.invokeLater(() -> {
            new TicTacToeComp(onExit);
            dispose();
        });
    }

    private void closeGame() {
        System.out.println("Zamykanie TicTacToeComp na wątku: " + Thread.currentThread().getName());
        if (onExit != null) {
            System.out.println("Wywołanie onExit z TicTacToeComp");
            onExit.run();
        }
        dispose();
    }

    private void highlightWinningLine(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (getSymbol(i, 0).equals(symbol) && getSymbol(i, 1).equals(symbol) && getSymbol(i, 2).equals(symbol)) {
                highlightRow(i);
                return;
            }
            if (getSymbol(0, i).equals(symbol) && getSymbol(1, i).equals(symbol) && getSymbol(2, i).equals(symbol)) {
                highlightColumn(i);
                return;
            }
        }
        if (getSymbol(0, 0).equals(symbol) && getSymbol(1, 1).equals(symbol) && getSymbol(2, 2).equals(symbol)) {
            highlightDiagonal(true);
        } else if (getSymbol(0, 2).equals(symbol) && getSymbol(1, 1).equals(symbol) && getSymbol(2, 0).equals(symbol)) {
            highlightDiagonal(false);
        }
    }

    private void highlightRow(int row) {
        for (int i = 0; i < 3; i++) {
            buttons[row][i].setBackground(Color.GREEN);
        }
    }

    private void highlightColumn(int col) {
        for (int i = 0; i < 3; i++) {
            buttons[i][col].setBackground(Color.GREEN);
        }
    }

    private void highlightDiagonal(boolean leftToRight) {
        if (leftToRight) {
            buttons[0][0].setBackground(Color.GREEN);
            buttons[1][1].setBackground(Color.GREEN);
            buttons[2][2].setBackground(Color.GREEN);
        } else {
            buttons[0][2].setBackground(Color.GREEN);
            buttons[1][1].setBackground(Color.GREEN);
            buttons[2][0].setBackground(Color.GREEN);
        }
    }

    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[]{-1, -1};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (getSymbol(i, j).isEmpty()) {
                    buttons[i][j].setIcon(oIcon);
                    buttons[i][j].putClientProperty("symbol", "O");
                    int score = minimax(0, false);
                    buttons[i][j].setIcon(null);
                    buttons[i][j].putClientProperty("symbol", null);
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkWin("O")) return 10 - depth;
        if (checkWin("X")) return depth - 10;
        if (isBoardFull()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (getSymbol(i, j).isEmpty()) {
                    buttons[i][j].setIcon(isMaximizing ? oIcon : xIcon);
                    buttons[i][j].putClientProperty("symbol", isMaximizing ? "O" : "X");
                    int score = minimax(depth + 1, !isMaximizing);
                    buttons[i][j].setIcon(null);
                    buttons[i][j].putClientProperty("symbol", null);
                    bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }
}