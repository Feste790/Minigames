//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package saper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class SaperComp extends JFrame {
    private int ROWS;
    private int COLS;
    private int MINES;
    private int maxDisplayNumber;
    private JButton[][] buttons;
    private boolean[][] mined;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private boolean gameOver = false;
    private boolean firstClick = true;
    private int selectedDifficulty = 0;
    private JLabel timerLabel;
    private JLabel minesLeftLabel;
    private Timer timer;
    private long startTime;
    private int revealedSafeFields = 0;
    private final Map<Integer, Color> numberColors = new HashMap();
    private Color tileBackgroundColor = new Color(180, 180, 180);
    private Color revealedTileColor = new Color(220, 220, 220);
    private final ImageIcon mineIcon = this.loadImage("/icons/bomba2.png");
    private final ImageIcon flagIcon = this.loadImage("/icons/flaga2.png");
    private Runnable onExit;
    private SaperConfig saperConfig;

    public SaperComp(){
        try {
            this.saperConfig.loadSettings();
        } catch (IOException var3) {
            IOException e = var3;
            throw new RuntimeException("Błąd wczytywania konfiguracji: " + e.getMessage(), e);
        }

        this.ROWS = this.saperConfig.getRows(this.saperConfig.getDifficulty());
        this.COLS = this.saperConfig.getCols(this.saperConfig.getDifficulty());
        this.MINES = this.saperConfig.getMines(this.saperConfig.getDifficulty());
        this.maxDisplayNumber = this.saperConfig.getMaxDisplayNumber(this.selectedDifficulty);
        this.buttons = new JButton[this.ROWS][this.COLS];
        this.mined = new boolean[this.ROWS][this.COLS];
        this.revealed = new boolean[this.ROWS][this.COLS];
        this.flagged = new boolean[this.ROWS][this.COLS];
        this.initDefaultColors();
        this.initGUI(this.selectedDifficulty);
        this.startTimer();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                SaperComp.this.closeSaper();
            }
        });

    }

    public SaperComp(Runnable onExit) {
        this.onExit = onExit;
        this.saperConfig = new SaperConfig();

        try {
            this.saperConfig.loadSettings();
        } catch (IOException var3) {
            IOException e = var3;
            throw new RuntimeException("Błąd wczytywania konfiguracji: " + e.getMessage(), e);
        }

        this.ROWS = this.saperConfig.getRows(this.saperConfig.getDifficulty());
        this.COLS = this.saperConfig.getCols(this.saperConfig.getDifficulty());
        this.MINES = this.saperConfig.getMines(this.saperConfig.getDifficulty());
        this.maxDisplayNumber = this.saperConfig.getMaxDisplayNumber(this.selectedDifficulty);
        this.buttons = new JButton[this.ROWS][this.COLS];
        this.mined = new boolean[this.ROWS][this.COLS];
        this.revealed = new boolean[this.ROWS][this.COLS];
        this.flagged = new boolean[this.ROWS][this.COLS];
        this.initDefaultColors();
        this.initGUI(this.selectedDifficulty);
        this.startTimer();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                SaperComp.this.closeSaper();
            }
        });
    }

    private ImageIcon loadImage(String path) {
        URL imageUrl = this.getClass().getResource(path);
        if (imageUrl == null) {
            throw new RuntimeException("Nie można wczytać obrazu: " + path);
        } else {
            return new ImageIcon(imageUrl);
        }
    }

    private void initDefaultColors() {
        this.numberColors.put(1, Color.BLUE);
        this.numberColors.put(2, new Color(0, 128, 0));
        this.numberColors.put(3, Color.RED);
        this.numberColors.put(4, new Color(0, 0, 139));
        this.numberColors.put(5, new Color(139, 69, 19));
        this.numberColors.put(6, new Color(64, 224, 208));
        this.numberColors.put(7, Color.BLACK);
        this.numberColors.put(8, Color.DARK_GRAY);
    }

    public long getStartTime() {
        return this.startTime;
    }

    public Color getTileBackgroundColor() {
        return this.tileBackgroundColor;
    }

    public void setTileBackgroundColor(Color tileBackgroundColor) {
        this.tileBackgroundColor = tileBackgroundColor;
        this.updateAllButtonColors();
    }

    public Color getRevealedTileColor() {
        return this.revealedTileColor;
    }

    public void setRevealedTileColor(Color revealedTileColor) {
        this.revealedTileColor = revealedTileColor;
        this.updateAllButtonColors();
    }

    public void setNumberColor(int number, Color color) {
        this.numberColors.put(number, color);
        this.updateAllButtonColors();
    }

    public Color getNumberColor(int number) {
        return (Color)this.numberColors.get(number);
    }

    public void setMines(int mines) {
        this.MINES = mines;
        this.updateMinesLeftLabel();
    }

    public int getMines() {
        return this.MINES;
    }

    private void updateAllButtonColors() {
        for(int i = 0; i < this.ROWS; ++i) {
            for(int j = 0; j < this.COLS; ++j) {
                JButton btn = this.buttons[i][j];
                if (this.revealed[i][j]) {
                    int count = Math.min(this.countMines(i, j), this.maxDisplayNumber);
                    btn.setBackground(this.revealedTileColor);
                    if (count > 0) {
                        btn.setForeground((Color)this.numberColors.getOrDefault(count, Color.BLACK));
                    }
                } else {
                    btn.setBackground(this.tileBackgroundColor);
                }
            }
        }

        this.repaint();
    }

    private void initGUI(int selectedDifficulty) {
        this.setTitle("Saper");
        this.setDefaultCloseOperation(0);
        this.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(0));
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener((e) -> {
            this.restartCurrentGame();
        });
        leftPanel.add(resetButton);
        topPanel.add(leftPanel, "West");
        this.timerLabel = new JLabel("Czas: 0 s", 0);
        this.timerLabel.setFont(new Font("Arial", 1, 16));
        topPanel.add(this.timerLabel, "Center");
        this.minesLeftLabel = new JLabel("Miny: " + this.MINES, 4);
        this.minesLeftLabel.setFont(new Font("Arial", 1, 16));
        topPanel.add(this.minesLeftLabel, "East");
        this.add(topPanel, "North");
        JPanel gridPanel = new JPanel(new GridLayout(this.ROWS, this.COLS));

        for(int i = 0; i < this.ROWS; ++i) {
            for(int j = 0; j < this.COLS; ++j) {
                final int row = i;
                final int col = j;
                final JButton btn = new JButton();
                btn.setFont(new Font("Arial", 1, 18));
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setFocusPainted(false);
                btn.addActionListener((e) -> {
                    this.handleClick(row, col);
                });
                btn.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (!SaperComp.this.gameOver && !SaperComp.this.revealed[row][col]) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                SaperComp.this.flagged[row][col] = !SaperComp.this.flagged[row][col];
                                btn.setIcon(SaperComp.this.flagged[row][col] ? SaperComp.this.flagIcon : null);
                                btn.setText("");
                                SaperComp.this.updateMinesLeftLabel();
                            }

                        }
                    }
                });
                this.buttons[i][j] = btn;
                gridPanel.add(btn);
            }
        }

        this.add(gridPanel, "Center");
        this.setSize(this.COLS * 40, this.ROWS * 40 + 80);
        this.setLocationRelativeTo((Component)null);
        this.setVisible(true);
    }

    private void restartCurrentGame() {
        int selectedIndex = this.saperConfig.getDifficulty();
        SwingUtilities.invokeLater(() -> {
            new SaperComp(this.onExit);
        });
        this.dispose();
    }

    private void handleClick(int row, int col) {
        if (!this.gameOver && !this.revealed[row][col] && !this.flagged[row][col]) {
            if (this.firstClick) {
                this.placeMinesSafe(row, col);
                this.firstClick = false;
            }

            System.out.println("selected difficulty: " + this.selectedDifficulty);
            this.reveal(row, col);
            this.updateMinesLeftLabel();
            if (this.gameOver && this.mined[row][col]) {
                this.showAllMines();
                if (JOptionPane.showConfirmDialog((Component)null, "Game Over! Restart?", "Restart", 0) == 0) {
                    this.restartGameFromComboBox();
                } else {
                    this.closeSaper();
                }
            } else if (this.checkWin()) {
                this.gameOver = true;
                String time = String.valueOf(this.getElapsedTime());
                this.timer.stop();
                int result = JOptionPane.showConfirmDialog((Component)null, "Gratulacje! Wygrałeś w " + this.getElapsedTime() + " s.\nCzy chcesz zagrać ponownie?", "Wygrana", 0);

                try {
                    PrintStream var10000 = System.out;
                    String var10001 = this.saperConfig.getPlayer();
                    var10000.println("1. player = " + var10001 + "difficulty = " + this.selectedDifficulty + "time = " + time);
                    this.saperConfig.saveScores(this.saperConfig.getPlayer(), this.selectedDifficulty == 0 ? "easy" : (this.selectedDifficulty == 1 ? "medium" : "hard"), time);
                    System.out.println("2. player = " + this.saperConfig.getPlayer() + "difficulty = " + this.selectedDifficulty + "time = " + time);
                } catch (IOException var6) {
                    IOException e = var6;
                    throw new RuntimeException(e);
                }

                if (result == 0) {
                    this.restartCurrentGame();
                } else {
                    this.closeSaper();
                }
            }

        }
    }

    private void closeSaper() {
        System.out.println("Zamykanie SaperComp na wątku: " + Thread.currentThread().getName());
        if (this.onExit != null) {
            System.out.println("Wywołanie onExit z SaperComp");
            this.onExit.run();
            this.dispose();
        } else {
            System.out.println("onExit jest null");
        }

    }

    private void updateMinesLeftLabel() {
        int flaggedCount = 0;

        for(int i = 0; i < this.ROWS; ++i) {
            for(int j = 0; j < this.COLS; ++j) {
                if (this.flagged[i][j]) {
                    ++flaggedCount;
                }
            }
        }

        int var10001 = this.MINES - flaggedCount;
        this.minesLeftLabel.setText("Miny: " + var10001);
    }

    private void startTimer() {
        this.startTime = System.currentTimeMillis();
        this.timer = new Timer(1000, (e) -> {
            this.timerLabel.setText("Czas: " + this.getElapsedTime() + " s");
        });
        this.timer.start();
    }

    private long getElapsedTime() {
        return (System.currentTimeMillis() - this.startTime) / 1000L;
    }

    private void placeMinesSafe(int safeRow, int safeCol) {
        Set<Point> safeZone = new HashSet();

        int j;
        int r;
        int c;
        for(int i = -1; i <= 1; ++i) {
            for(j = -1; j <= 1; ++j) {
                r = safeRow + i;
                c = safeCol + j;
                if (r >= 0 && r < this.ROWS && c >= 0 && c < this.COLS) {
                    safeZone.add(new Point(r, c));
                }
            }
        }

        Random rand = new Random();
        j = 0;

        while(j < this.MINES) {
            r = rand.nextInt(this.ROWS);
            c = rand.nextInt(this.COLS);
            if (!this.mined[r][c] && !safeZone.contains(new Point(r, c))) {
                this.mined[r][c] = true;
                ++j;
            }
        }

    }

    private int countMines(int row, int col) {
        int count = 0;

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < this.ROWS && c >= 0 && c < this.COLS && this.mined[r][c]) {
                    ++count;
                }
            }
        }

        return count;
    }

    private void reveal(int row, int col) {
        if (row >= 0 && row < this.ROWS && col >= 0 && col < this.COLS && !this.revealed[row][col]) {
            this.revealed[row][col] = true;
            this.flagged[row][col] = false;
            JButton btn = this.buttons[row][col];
            btn.setIcon((Icon)null);
            if (this.mined[row][col]) {
                btn.setIcon(this.mineIcon);
                btn.setBackground(Color.RED);
                this.gameOver = true;
                this.timer.stop();
            } else {
                ++this.revealedSafeFields;
                int count = Math.min(this.countMines(row, col), this.maxDisplayNumber);
                if (count > 0) {
                    btn.setText(Integer.toString(count));
                    btn.setForeground(this.getNumberColor(count));
                } else {
                    btn.setText("");
                }

                btn.setBackground(this.revealedTileColor);
                btn.setOpaque(true);
                btn.setBorderPainted(true);
                if (count == 0) {
                    for(int i = -1; i <= 1; ++i) {
                        for(int j = -1; j <= 1; ++j) {
                            if (i != 0 || j != 0) {
                                this.reveal(row + i, col + j);
                            }
                        }
                    }
                }

            }
        }
    }

    private void showAllMines() {
        for(int i = 0; i < this.ROWS; ++i) {
            for(int j = 0; j < this.COLS; ++j) {
                if (this.mined[i][j] && !this.revealed[i][j]) {
                    this.buttons[i][j].setIcon(this.mineIcon);
                    this.buttons[i][j].setText("");
                    this.buttons[i][j].setBackground(Color.LIGHT_GRAY);
                }
            }
        }

    }

    private boolean checkWin() {
        return this.revealedSafeFields == this.ROWS * this.COLS - this.MINES;
    }

    private void restartGameFromComboBox() {
        int index = this.saperConfig.getDifficulty();
        this.saperConfig.setRows(index, index == 0 ? 10 : (index == 1 ? 18 : 24));
        this.saperConfig.setCols(index, index == 0 ? 8 : (index == 1 ? 14 : 20));
        this.saperConfig.setMines(index, index == 0 ? 10 : (index == 1 ? 40 : 99));
        this.saperConfig.setMaxDisplayNumber(index, index == 0 ? 3 : (index == 1 ? 4 : 6));
        SwingUtilities.invokeLater(() -> {
            new SaperComp(this.onExit);
        });
    }
}
