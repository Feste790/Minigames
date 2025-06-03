package tictactoe;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TicTacToeConfig {
    private static final String SCORES_FILE_NAME = "ticTacToeScores.txt";
    private static final Path SCORES_PATH = Paths.get(System.getProperty("user.home"), ".tictactoe", "ticTacToeScores.txt");
    private static final String CONFIG_FILE_NAME = "ticTacToeConfig.txt";
    private static final Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".tictactoe", "ticTacToeConfig.txt");
    private static final Path ICONS_DIR = Paths.get(System.getProperty("user.home"), ".tictactoe", "icons");
    private int difficulty = 0; // 0: EasyAI, 1: HardAI, 2: PVP
    private String player1 = "Player1";
    private String player2 = "Player2";
    private String gameMode = "PVP";
    private String circlePNG = "icons/circle.png";
    private String crossPNG = "icons/cross.png";
    private List<ScoreEntry> scores = new ArrayList<>();
    private String textColor = "#000000";

    public TicTacToeConfig() {
        try {
            Files.createDirectories(ICONS_DIR);
            loadScores();
            loadSettings();
        } catch (IOException e) {
            throw new RuntimeException("Błąd wczytywania konfiguracji: " + e.getMessage(), e);
        }
    }

    public void saveScores(String player, String gameMode) throws IOException {
        Path configDir = SCORES_PATH.getParent();
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        int difficultyValue;
        switch (gameMode.toLowerCase()) {
            case "easyai":
                difficultyValue = 0;
                break;
            case "hardai":
                difficultyValue = 1;
                break;
            case "pvp":
                difficultyValue = 2;
                break;
            default:
                throw new IllegalArgumentException("Nieprawidłowy tryb gry: " + gameMode);
        }

        loadScores();
        boolean found = false;
        for (ScoreEntry entry : scores) {
            if (entry.player.equals(player) && entry.difficulty == difficultyValue) {
                entry.scores++;
                found = true;
                break;
            }
        }
        if (!found) {
            scores.add(new ScoreEntry(player, difficultyValue, 1));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(SCORES_PATH, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (ScoreEntry entry : scores) {
                writer.write(entry.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Błąd zapisywania wyników: " + e.getMessage(), e);
        }
    }

    public void loadScores() throws IOException {
        scores.clear();
        File scoresFile = SCORES_PATH.toFile();
        if (scoresFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(scoresFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(",")) {
                        String[] parts = line.split(",", 3);
                        if (parts.length == 3) {
                            try {
                                String player = parts[0].trim();
                                int difficulty = Integer.parseInt(parts[1].trim());
                                int score = Integer.parseInt(parts[2].trim());
                                scores.add(new ScoreEntry(player, difficulty, score));
                            } catch (NumberFormatException e) {
                                System.err.println("Nieprawidłowy format wyniku w linii: " + line);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Błąd wczytywania pliku wyników: " + e.getMessage());
            }
        }
    }

    public void loadSettings() throws IOException {
        Map<String, String> settings = new HashMap<>();
        File configFile = CONFIG_PATH.toFile();
        if (configFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                readSettings(reader, settings);
            } catch (IOException e) {
                System.err.println("Błąd wczytywania pliku konfiguracyjnego: " + e.getMessage());
            }
        } else {
            try (InputStream inputStream = getClass().getResourceAsStream("/ticTacToeConfig.txt")) {
                if (inputStream == null) {
                    throw new IOException("Nie znaleziono pliku: /ticTacToeConfig.txt");
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    readSettings(reader, settings);
                }
            }
        }
        updateFields(settings);
    }

    private void readSettings(BufferedReader reader, Map<String, String> settings) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("=")) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }

    private void updateFields(Map<String, String> settings) {
        String value = settings.get("difficulty");
        if (value != null) {
            try {
                this.difficulty = Integer.parseInt(value);
                this.gameMode = (difficulty == 2) ? "PVP" : (difficulty == 0 ? "EasyAI" : "HardAI");
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa wartość trudności: " + value);
            }
        }

        value = settings.get("player1");
        if (value != null) this.player1 = value;

        value = settings.get("player2");
        if (value != null) this.player2 = value;

        value = settings.get("gameMode");
        if (value != null) {
            this.gameMode = value;
            this.difficulty = switch (value.toLowerCase()) {
                case "easyai" -> 0;
                case "hardai" -> 1;
                case "pvp" -> 2;
                default -> 0;
            };
        }

        value = settings.get("circlePNG");
        if (value != null) this.circlePNG = value;

        value = settings.get("crossPNG");
        if (value != null) this.crossPNG = value;

        value = settings.get("textColor");
        if (value != null) this.textColor = value;

    }

    public void saveSettings() throws IOException {
        File configDir = CONFIG_PATH.getParent().toFile();
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("difficulty=" + difficulty);
            writer.newLine();
            writer.write("player1=" + player1);
            writer.newLine();
            writer.write("player2=" + player2);
            writer.newLine();
            writer.write("gameMode=" + gameMode);
            writer.newLine();
            writer.write("circlePNG=" + circlePNG);
            writer.newLine();
            writer.write("crossPNG=" + crossPNG);
            writer.newLine();
            writer.write("textColor= " + textColor);
            writer.newLine();
        } catch (IOException e) {
            throw new IOException("Błąd zapisywania pliku konfiguracyjnego: " + e.getMessage(), e);
        }
    }

    public void copyImageToIconsDir(File sourceFile, String targetFileName) throws IOException {
        Path targetPath = ICONS_DIR.resolve(targetFileName);
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public Path getIconsDir() {
        return ICONS_DIR;
    }

    public String getCirclePNG() {
        return circlePNG;
    }

    public void setCirclePNG(String circlePNG) {
        this.circlePNG = circlePNG;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie circlePNG: " + e.getMessage());
        }
    }

    public String getCrossPNG() {
        return crossPNG;
    }

    public void setCrossPNG(String crossPNG) {
        this.crossPNG = crossPNG;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie crossPNG: " + e.getMessage());
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        this.gameMode = (difficulty == 2) ? "PVP" : (difficulty == 0 ? "EasyAI" : "HardAI");
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie difficulty: " + e.getMessage());
        }
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie player1: " + e.getMessage());
        }
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie player2: " + e.getMessage());
        }
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
        this.difficulty = switch (gameMode.toLowerCase()) {
            case "easyai" -> 0;
            case "hardai" -> 1;
            case "pvp" -> 2;
            default -> 0;
        };
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie gameMode: " + e.getMessage());
        }
    }

    private static class ScoreEntry {
        String player;
        int difficulty; // 0: EasyAI, 1: HardAI, 2: PVP
        int scores;

        ScoreEntry(String player, int difficulty, int scores) {
            this.player = player;
            this.difficulty = difficulty;
            this.scores = scores;
        }

        @Override
        public String toString() {
            return String.format("%s,%d,%d", player, difficulty, scores);
        }
    }
}