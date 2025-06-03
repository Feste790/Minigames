package Sneake_Game;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class SnakeConfig {
    private static final Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".snake", "snakeConfig.txt");
    private int difficulty = 1; // 0: Easy, 1: Medium, 2: Hard
    private String player = "SnakePlayer";
    private int easyWidth = 600;
    private int easyHeight = 600;
    private int easyDotSize = 20;
    private int easyDelay = 200;
    private int mediumWidth = 600;
    private int mediumHeight = 600;
    private int mediumDotSize = 20;
    private int mediumDelay = 140;
    private int hardWidth = 600;
    private int hardHeight = 600;
    private int hardDotSize = 20;
    private int hardDelay = 100;
    private Color snakeColor = new Color(0, 255, 0);
    private Color appleColor = new Color(255, 0, 0);
    private Color backgroundColor = new Color(0, 0, 0);
    private Color obstacleColor = new Color(128, 128, 128);

    public void loadSettings() throws IOException {
        Map<String, String> settings = new HashMap<>();
        File configFile = CONFIG_PATH.toFile();
        if (configFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                readSettings(reader, settings);
            } catch (IOException e) {
                System.err.println("Błąd wczytywania zewnętrznego pliku konfiguracyjnego: " + e.getMessage());
            }
        } else {
            try (InputStream inputStream = getClass().getResourceAsStream("/snakeConfig.txt")) {
                if (inputStream == null) {
                    throw new IOException("Nie znaleziono pliku: /snakeConfig.txt");
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
        String value = settings.get("player");
        if (value != null) this.player = value;

        value = settings.get("difficulty");
        if (value != null) {
            try {
                this.difficulty = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid difficulty value: " + value);
            }
        }

        value = settings.get("easyWidth");
        if (value != null) {
            try {
                this.easyWidth = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid easyWidth value: " + value);
            }
        }

        value = settings.get("mediumWidth");
        if (value != null) {
            try {
                this.mediumWidth = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid mediumWidth value: " + value);
            }
        }

        value = settings.get("hardWidth");
        if (value != null) {
            try {
                this.hardWidth = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid hardWidth value: " + value);
            }
        }

        value = settings.get("easyHeight");
        if (value != null) {
            try {
                this.easyHeight = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid easyHeight value: " + value);
            }
        }

        value = settings.get("mediumHeight");
        if (value != null) {
            try {
                this.mediumHeight = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid mediumHeight value: " + value);
            }
        }

        value = settings.get("hardHeight");
        if (value != null) {
            try {
                this.hardHeight = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid hardHeight value: " + value);
            }
        }

        value = settings.get("easyDotSize");
        if (value != null) {
            try {
                this.easyDotSize = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid easyDotSize value: " + value);
            }
        }

        value = settings.get("mediumDotSize");
        if (value != null) {
            try {
                this.mediumDotSize = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid mediumDotSize value: " + value);
            }
        }

        value = settings.get("hardDotSize");
        if (value != null) {
            try {
                this.hardDotSize = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid hardDotSize value: " + value);
            }
        }

        value = settings.get("easyDelay");
        if (value != null) {
            try {
                this.easyDelay = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid easyDelay value: " + value);
            }
        }

        value = settings.get("mediumDelay");
        if (value != null) {
            try {
                this.mediumDelay = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid mediumDelay value: " + value);
            }
        }

        value = settings.get("hardDelay");
        if (value != null) {
            try {
                this.hardDelay = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid hardDelay value: " + value);
            }
        }

        value = settings.get("snakeColor");
        if (value != null) {
            try {
                String[] rgb = value.split(",");
                this.snakeColor = new Color(
                        Integer.parseInt(rgb[0].trim()),
                        Integer.parseInt(rgb[1].trim()),
                        Integer.parseInt(rgb[2].trim())
                );
            } catch (Exception e) {
                System.err.println("Invalid snakeColor value: " + value);
            }
        }

        value = settings.get("appleColor");
        if (value != null) {
            try {
                String[] rgb = value.split(",");
                this.appleColor = new Color(
                        Integer.parseInt(rgb[0].trim()),
                        Integer.parseInt(rgb[1].trim()),
                        Integer.parseInt(rgb[2].trim())
                );
            } catch (Exception e) {
                System.err.println("Invalid appleColor value: " + value);
            }
        }

        value = settings.get("backgroundColor");
        if (value != null) {
            try {
                String[] rgb = value.split(",");
                this.backgroundColor = new Color(
                        Integer.parseInt(rgb[0].trim()),
                        Integer.parseInt(rgb[1].trim()),
                        Integer.parseInt(rgb[2].trim())
                );
            } catch (Exception e) {
                System.err.println("Invalid backgroundColor value: " + value);
            }
        }

        value = settings.get("obstacleColor");
        if (value != null) {
            try {
                String[] rgb = value.split(",");
                this.obstacleColor = new Color(
                        Integer.parseInt(rgb[0].trim()),
                        Integer.parseInt(rgb[1].trim()),
                        Integer.parseInt(rgb[2].trim())
                );
            } catch (Exception e) {
                System.err.println("Invalid obstacleColor value: " + value);
            }
        }
    }

    public void saveSettings() throws IOException {
        File configDir = CONFIG_PATH.getParent().toFile();
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH)) {
            writer.write("player=" + player);
            writer.newLine();
            writer.write("difficulty=" + difficulty);
            writer.newLine();
            writer.write("easyWidth=" + easyWidth);
            writer.newLine();
            writer.write("mediumWidth=" + mediumWidth);
            writer.newLine();
            writer.write("hardWidth=" + hardWidth);
            writer.newLine();
            writer.write("easyHeight=" + easyHeight);
            writer.newLine();
            writer.write("mediumHeight=" + mediumHeight);
            writer.newLine();
            writer.write("hardHeight=" + hardHeight);
            writer.newLine();
            writer.write("easyDotSize=" + easyDotSize);
            writer.newLine();
            writer.write("mediumDotSize=" + mediumDotSize);
            writer.newLine();
            writer.write("hardDotSize=" + hardDotSize);
            writer.newLine();
            writer.write("easyDelay=" + easyDelay);
            writer.newLine();
            writer.write("mediumDelay=" + mediumDelay);
            writer.newLine();
            writer.write("hardDelay=" + hardDelay);
            writer.newLine();
            writer.write("snakeColor=" + snakeColor.getRed() + "," + snakeColor.getGreen() + "," + snakeColor.getBlue());
            writer.newLine();
            writer.write("appleColor=" + appleColor.getRed() + "," + appleColor.getGreen() + "," + appleColor.getBlue());
            writer.newLine();
            writer.write("backgroundColor=" + backgroundColor.getRed() + "," + backgroundColor.getGreen() + "," + backgroundColor.getBlue());
            writer.newLine();
            writer.write("obstacleColor=" + obstacleColor.getRed() + "," + obstacleColor.getGreen() + "," + obstacleColor.getBlue());
            writer.newLine();
        } catch (IOException e) {
            throw new IOException("Błąd zapisywania pliku konfiguracyjnego: " + e.getMessage(), e);
        }
    }

    // Gettery i Settery
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie difficulty: " + e.getMessage());
        }
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie player: " + e.getMessage());
        }
    }

    public int getWidth(int difficulty) {
        return switch (difficulty) {
            case 0 -> easyWidth;
            case 1 -> mediumWidth;
            case 2 -> hardWidth;
            default -> mediumWidth;
        };
    }

    public void setWidth(int difficulty, int width) {
        switch (difficulty) {
            case 0 -> easyWidth = width;
            case 1 -> mediumWidth = width;
            case 2 -> hardWidth = width;
        }
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie width: " + e.getMessage());
        }
    }

    public int getHeight(int difficulty) {
        return switch (difficulty) {
            case 0 -> easyHeight;
            case 1 -> mediumHeight;
            case 2 -> hardHeight;
            default -> mediumHeight;
        };
    }

    public void setHeight(int difficulty, int height) {
        switch (difficulty) {
            case 0 -> easyHeight = height;
            case 1 -> mediumHeight = height;
            case 2 -> hardHeight = height;
        }
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie height: " + e.getMessage());
        }
    }

    public int getDotSize(int difficulty) {
        return switch (difficulty) {
            case 0 -> easyDotSize;
            case 1 -> mediumDotSize;
            case 2 -> hardDotSize;
            default -> mediumDotSize;
        };
    }

    public void setDotSize(int difficulty, int dotSize) {
        switch (difficulty) {
            case 0 -> easyDotSize = dotSize;
            case 1 -> mediumDotSize = dotSize;
            case 2 -> hardDotSize = dotSize;
        }
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie dotSize: " + e.getMessage());
        }
    }

    public int getDelay(int difficulty) {
        return switch (difficulty) {
            case 0 -> easyDelay;
            case 1 -> mediumDelay;
            case 2 -> hardDelay;
            default -> mediumDelay;
        };
    }

    public void setDelay(int difficulty, int delay) {
        switch (difficulty) {
            case 0 -> easyDelay = delay;
            case 1 -> mediumDelay = delay;
            case 2 -> hardDelay = delay;
        }
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie delay: " + e.getMessage());
        }
    }

    public Color getSnakeColor() {
        return snakeColor;
    }

    public void setSnakeColor(Color snakeColor) {
        this.snakeColor = snakeColor;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie snakeColor: " + e.getMessage());
        }
    }

    public Color getAppleColor() {
        return appleColor;
    }

    public void setAppleColor(Color appleColor) {
        this.appleColor = appleColor;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie appleColor: " + e.getMessage());
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie backgroundColor: " + e.getMessage());
        }
    }

    public Color getObstacleColor() {
        return obstacleColor;
    }

    public void setObstacleColor(Color obstacleColor) {
        this.obstacleColor = obstacleColor;
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Błąd zapisywania ustawień po zmianie obstacleColor: " + e.getMessage());
        }
    }
}