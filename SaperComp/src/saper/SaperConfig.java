//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package saper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class SaperConfig {
    private static final String SCORES_FILE_NAME = "saperScores.txt";
    private static final Path SCORES_PATH = Paths.get(System.getProperty("user.home"), ".saper", "saperScores.txt");
    private static final String CONFIG_FILE_NAME = "saperConfig.txt";
    private static final Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".saper", "saperConfig.txt");
    private int difficulty = 0;
    private String player = "SaperPlayer";
    private int easyMines = 10;
    private int easyRows = 10;
    private int easyCols = 8;
    private int easyMaxNumber = 3;
    private int mediumMines = 40;
    private int mediumRows = 18;
    private int mediumCols = 14;
    private int mediumMaxNumber = 4;
    private int hardMines = 99;
    private int hardRows = 24;
    private int hardCols = 20;
    private int hardMaxNumber = 6;

    public SaperConfig() {
    }

    public void saveScores(String user, String difficulty, String time) throws IOException {
        Path configDir = SCORES_PATH.getParent();
        if (!Files.exists(configDir, new LinkOption[0])) {
            Files.createDirectories(configDir);
        }

        try {
            BufferedWriter writer = Files.newBufferedWriter(SCORES_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            try {
                writer.write(String.format("%s,%s,%s", user, difficulty, time));
                writer.newLine();
            } catch (Throwable var9) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (writer != null) {
                writer.close();
            }

        } catch (IOException var10) {
            IOException e = var10;
            throw new IOException("ERROR saving scores file: " + e.getMessage(), e);
        }
    }

    public void loadSettings() throws IOException {
        Map<String, String> settings = new HashMap();
        InputStream inputStream = null;
        File configFile = CONFIG_PATH.toFile();
        BufferedReader reader;
        if (configFile.exists()) {
            try {
                reader = new BufferedReader(new FileReader(configFile));

                try {
                    this.readSettings(reader, settings);
                } catch (Throwable var18) {
                    try {
                        reader.close();
                    } catch (Throwable var15) {
                        var18.addSuppressed(var15);
                    }

                    throw var18;
                }

                reader.close();
            } catch (IOException var19) {
                IOException e = var19;
                System.err.println("Błąd wczytywania zewnętrznego pliku konfiguracyjnego: " + e.getMessage());
            }
        } else {
            inputStream = this.getClass().getResourceAsStream("/saperConfig.txt");
            if (inputStream == null) {
                throw new IOException("Nie znaleziono pliku: /saperConfig.txt");
            }

            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                try {
                    this.readSettings(reader, settings);
                } catch (Throwable var16) {
                    try {
                        reader.close();
                    } catch (Throwable var14) {
                        var16.addSuppressed(var14);
                    }

                    throw var16;
                }

                reader.close();
            } finally {
                inputStream.close();
            }
        }

        this.updateFields(settings);
    }

    private void readSettings(BufferedReader reader, Map<String, String> settings) throws IOException {
        String line;
        while((line = reader.readLine()) != null) {
            if (line.contains("=")) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        }

    }

    private void updateFields(Map<String, String> settings) {
        String value = (String)settings.get("player");
        if (value != null) {
            try {
                this.player = value;
            } catch (NumberFormatException var17) {
                System.err.println("Invalid difficulty value: " + value);
            }
        }

        value = (String)settings.get("difficulty");
        if (value != null) {
            try {
                this.difficulty = Integer.parseInt(value);
            } catch (NumberFormatException var16) {
                System.err.println("Invalid difficulty value: " + value);
            }
        }

        value = (String)settings.get("easyMines");
        if (value != null) {
            try {
                this.easyMines = Integer.parseInt(value);
            } catch (NumberFormatException var15) {
                System.err.println("Invalid easyMines value: " + value);
            }
        }

        value = (String)settings.get("mediumMines");
        if (value != null) {
            try {
                this.mediumMines = Integer.parseInt(value);
            } catch (NumberFormatException var14) {
                System.err.println("Invalid mediumMines value: " + value);
            }
        }

        value = (String)settings.get("hardMines");
        if (value != null) {
            try {
                this.hardMines = Integer.parseInt(value);
            } catch (NumberFormatException var13) {
                System.err.println("Invalid hardMines value: " + value);
            }
        }

        value = (String)settings.get("easyRows");
        if (value != null) {
            try {
                this.easyRows = Integer.parseInt(value);
            } catch (NumberFormatException var12) {
                System.err.println("Invalid easyRows value: " + value);
            }
        }

        value = (String)settings.get("mediumRows");
        if (value != null) {
            try {
                this.mediumRows = Integer.parseInt(value);
            } catch (NumberFormatException var11) {
                System.err.println("Invalid mediumRows value: " + value);
            }
        }

        value = (String)settings.get("hardRows");
        if (value != null) {
            try {
                this.hardRows = Integer.parseInt(value);
            } catch (NumberFormatException var10) {
                System.err.println("Invalid hardRows value: " + value);
            }
        }

        value = (String)settings.get("easyCols");
        if (value != null) {
            try {
                this.easyCols = Integer.parseInt(value);
            } catch (NumberFormatException var9) {
                System.err.println("Invalid easyCols value: " + value);
            }
        }

        value = (String)settings.get("mediumCols");
        if (value != null) {
            try {
                this.mediumCols = Integer.parseInt(value);
            } catch (NumberFormatException var8) {
                System.err.println("Invalid mediumCols value: " + value);
            }
        }

        value = (String)settings.get("hardCols");
        if (value != null) {
            try {
                this.hardCols = Integer.parseInt(value);
            } catch (NumberFormatException var7) {
                System.err.println("Invalid hardCols value: " + value);
            }
        }

        value = (String)settings.get("easyMaxNumber");
        if (value != null) {
            try {
                this.easyMaxNumber = Integer.parseInt(value);
            } catch (NumberFormatException var6) {
                System.err.println("Invalid easyMaxNumber value: " + value);
            }
        }

        value = (String)settings.get("mediumMaxNumber");
        if (value != null) {
            try {
                this.mediumMaxNumber = Integer.parseInt(value);
            } catch (NumberFormatException var5) {
                System.err.println("Invalid mediumMaxNumber value: " + value);
            }
        }

        value = (String)settings.get("hardMaxNumber");
        if (value != null) {
            try {
                this.hardMaxNumber = Integer.parseInt(value);
            } catch (NumberFormatException var4) {
                System.err.println("Invalid hardMaxNumber value: " + value);
            }
        }

    }

    public void saveSettings() throws IOException {
        File configDir = CONFIG_PATH.getParent().toFile();
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        try {
            BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH);

            try {
                writer.write("player=" + this.player);
                writer.newLine();
                writer.write("difficulty=" + this.difficulty);
                writer.newLine();
                writer.write("easyMines=" + this.easyMines);
                writer.newLine();
                writer.write("mediumMines=" + this.mediumMines);
                writer.newLine();
                writer.write("hardMines=" + this.hardMines);
                writer.newLine();
                writer.write("easyRows=" + this.easyRows);
                writer.newLine();
                writer.write("mediumRows=" + this.mediumRows);
                writer.newLine();
                writer.write("hardRows=" + this.hardRows);
                writer.newLine();
                writer.write("easyCols=" + this.easyCols);
                writer.newLine();
                writer.write("mediumCols=" + this.mediumCols);
                writer.newLine();
                writer.write("hardCols=" + this.hardCols);
                writer.newLine();
                writer.write("easyMaxNumber=" + this.easyMaxNumber);
                writer.newLine();
                writer.write("mediumMaxNumber=" + this.mediumMaxNumber);
                writer.newLine();
                writer.write("hardMaxNumber=" + this.hardMaxNumber);
                writer.newLine();
            } catch (Throwable var6) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (writer != null) {
                writer.close();
            }

        } catch (IOException var7) {
            IOException e = var7;
            throw new IOException("Błąd zapisywania pliku konfiguracyjnego: " + e.getMessage(), e);
        }
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;

        try {
            this.saveSettings();
        } catch (IOException var3) {
            IOException e = var3;
            System.err.println("Błąd zapisywania ustawień po zmianie difficulty: " + e.getMessage());
        }

    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return this.player;
    }

    public int getRows(int difficulty) {
        switch (difficulty) {
            case 0 -> {
                return this.easyRows;
            }
            case 1 -> {
                return this.mediumRows;
            }
            case 2 -> {
                return this.hardRows;
            }
            default -> {
                return this.mediumRows;
            }
        }
    }

    public int getCols(int difficulty) {
        switch (difficulty) {
            case 0 -> {
                return this.easyCols;
            }
            case 1 -> {
                return this.mediumCols;
            }
            case 2 -> {
                return this.hardCols;
            }
            default -> {
                return this.mediumCols;
            }
        }
    }

    public int getMines(int difficulty) {
        switch (difficulty) {
            case 0 -> {
                return this.easyMines;
            }
            case 1 -> {
                return this.mediumMines;
            }
            case 2 -> {
                return this.hardMines;
            }
            default -> {
                return this.mediumMines;
            }
        }
    }

    public int getMaxDisplayNumber(int difficulty) {
        switch (difficulty) {
            case 0 -> {
                return this.easyMaxNumber;
            }
            case 1 -> {
                return this.mediumMaxNumber;
            }
            case 2 -> {
                return this.hardMaxNumber;
            }
            default -> {
                return this.mediumMaxNumber;
            }
        }
    }

    public void setRows(int difficulty, int rows) {
        switch (difficulty) {
            case 0 -> this.easyRows = rows;
            case 1 -> this.mediumRows = rows;
            case 2 -> this.hardRows = rows;
        }

        try {
            this.saveSettings();
        } catch (IOException var4) {
            IOException e = var4;
            System.err.println("Błąd zapisywania ustawień po zmianie rows: " + e.getMessage());
        }

    }

    public void setCols(int difficulty, int cols) {
        switch (difficulty) {
            case 0 -> this.easyCols = cols;
            case 1 -> this.mediumCols = cols;
            case 2 -> this.hardCols = cols;
        }

        try {
            this.saveSettings();
        } catch (IOException var4) {
            IOException e = var4;
            System.err.println("Błąd zapisywania ustawień po zmianie cols: " + e.getMessage());
        }

    }

    public void setMines(int difficulty, int mines) {
        switch (difficulty) {
            case 0 -> this.easyMines = mines;
            case 1 -> this.mediumMines = mines;
            case 2 -> this.hardMines = mines;
        }

        try {
            this.saveSettings();
        } catch (IOException var4) {
            IOException e = var4;
            System.err.println("Błąd zapisywania ustawień po zmianie mines: " + e.getMessage());
        }

    }

    public void setMaxDisplayNumber(int difficulty, int maxNumber) {
        switch (difficulty) {
            case 0 -> this.easyMaxNumber = maxNumber;
            case 1 -> this.mediumMaxNumber = maxNumber;
            case 2 -> this.hardMaxNumber = maxNumber;
        }

        try {
            this.saveSettings();
        } catch (IOException var4) {
            IOException e = var4;
            System.err.println("Błąd zapisywania ustawień po zmianie maxDisplayNumber: " + e.getMessage());
        }

    }
}
