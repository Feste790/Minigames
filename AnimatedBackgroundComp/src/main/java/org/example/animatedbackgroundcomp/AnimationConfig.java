package org.example.animatedbackgroundcomp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class AnimationConfig {
    private static final String CONFIG_FILE_NAME = "config.txt";
    private static final Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".minigames", CONFIG_FILE_NAME);

    private int width = 800;
    private int height = 600;

    private boolean isMusicEnabled = true;
    private String musicFile = "/sounds/mainSoundtrack.mp3";
    private int volMusic = 50;

    private boolean isCrossVisible = true;
    private int crossSize = 30;
    private String crossColor = "#c8c8c8";

    private boolean isCircleVisible = true;
    private int circleSize = 20;
    private String circleColor = "#c8c8c8";

    private boolean isSnakeVisible = true;
    private String snakeColor = "#008000";
    private int snakeLength = 14;

    private boolean isBackgroundVisible = true;
    private String backgroundColor = "#1a1a1a";

    private boolean isLineVisible = true;
    private String lineColor = "#292929";

    private boolean isExplosionEnabled = true;
    private int minExploDelay = 1000;
    private int maxExploDelay = 3000;

    public AnimationConfig() {

    }

    public AnimationConfig(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        isMusicEnabled = musicEnabled;
    }

    public String getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(String musicFile) {
        this.musicFile = musicFile;
    }

    public int getVolMusic() {
        return volMusic;
    }

    public void setVolMusic(int volMusic) {
        if (volMusic < 0) {
            this.volMusic = 0;
        } else if (volMusic > 100) {
            this.volMusic = 100;
        } else {
            this.volMusic = volMusic;
        }
    }

    public boolean isCrossVisible() {
        return isCrossVisible;
    }

    public void setCrossVisible(boolean crossVisible) {
        isCrossVisible = crossVisible;
    }

    public int getCrossSize() {
        return crossSize;
    }

    public void setCrossSize(int crossSize) {
        this.crossSize = crossSize;
    }

    public String getCrossColor() {
        return crossColor;
    }

    public void setCrossColor(String crossColor) {
        this.crossColor = crossColor;
    }

    public boolean isCircleVisible() {
        return isCircleVisible;
    }

    public void setCircleVisible(boolean circleVisible) {
        isCircleVisible = circleVisible;
    }

    public int getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(int circleSize) {
        this.circleSize = circleSize;
    }

    public String getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(String circleColor) {
        this.circleColor = circleColor;
    }

    public boolean isSnakeVisible() {
        return isSnakeVisible;
    }

    public void setSnakeVisible(boolean snakeVisible) {
        isSnakeVisible = snakeVisible;
    }

    public String getSnakeColor() {
        return snakeColor;
    }

    public void setSnakeColor(String snakeColor) {
        this.snakeColor = snakeColor;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public void setSnakeLength(int snakeLength) {
        this.snakeLength = snakeLength;
    }

    public boolean isBackgroundVisible() {
        return isBackgroundVisible;
    }

    public void setBackgroundVisible(boolean backgroundVisible) {
        isBackgroundVisible = backgroundVisible;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isLineVisible() {
        return isLineVisible;
    }

    public void setLineVisible(boolean lineVisible) {
        isLineVisible = lineVisible;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public boolean isExplosionEnabled() {
        return isExplosionEnabled;
    }

    public void setExplosionEnabled(boolean explosionEnabled) {
        isExplosionEnabled = explosionEnabled;
    }

    public int getMinExploDelay() {
        return minExploDelay;
    }

    public void setMinExploDelay(int minExploDelay) {
        this.minExploDelay = minExploDelay;
    }

    public int getMaxExploDelay() {
        return maxExploDelay;
    }

    public void setMaxExploDelay(int maxExploDelay) {
        this.maxExploDelay = maxExploDelay;
    }

    public void saveSettings(String filePath) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("width=" + width);
            writer.newLine();
            writer.write("height=" + height);
            writer.newLine();
            writer.write("isMusicEnabled=" + isMusicEnabled);
            writer.newLine();
            writer.write("musicFile=" + musicFile);
            writer.newLine();
            writer.write("volMusic=" + volMusic);
            writer.newLine();
            writer.write("isCrossVisible=" + isCrossVisible);
            writer.newLine();
            writer.write("crosSize=" + crossSize);
            writer.newLine();
            writer.write("crossColor=" + crossColor);
            writer.newLine();
            writer.write("isCircleVisible=" + isCircleVisible);
            writer.newLine();
            writer.write("circleSize=" + circleSize);
            writer.newLine();
            writer.write("circleColor=" + circleColor);
            writer.newLine();
            writer.write("isSnakeVisible=" + isSnakeVisible);
            writer.newLine();
            writer.write("snakeColor=" + snakeColor);
            writer.newLine();
            writer.write("snakeLength=" + snakeLength);
            writer.newLine();
            writer.write("isBackgroundVisible=" + isBackgroundVisible);
            writer.newLine();
            writer.write("backgroundColor=" + backgroundColor);
            writer.newLine();
            writer.write("isLineVisible=" + isLineVisible);
            writer.newLine();
            writer.write("lineColor=" + lineColor);
            writer.newLine();
            writer.write("isExplosionEnabled=" + isExplosionEnabled);
            writer.newLine();
            writer.write("minExploDelay=" + minExploDelay);
            writer.newLine();
            writer.write("maxExploDelay=" + maxExploDelay);
            writer.newLine();

        } catch (IOException e) {
            System.err.println("Saving file ERROR:" + e.getMessage());
        }
    }

    public String readSetting(String filePath, String setting) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String prefix = setting + "=";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(prefix)) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        return parts[1].trim();
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filePath, e);
        }
        return null;
    }

    public void loadSettings(String filePath) throws IOException {
        String value;

        value = readSetting(filePath, "width");
        if (value != null) {
            try {
                this.width = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid width value: " + value);
            }
        }

        value = readSetting(filePath, "height");
        if (value != null) {
            try {
                this.height = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid height value: " + value);
            }
        }

        value = readSetting(filePath, "isMusicEnabled");
        if (value != null) {
            this.isMusicEnabled = Boolean.parseBoolean(value);
        }

        value = readSetting(filePath, "musicFile");
        if (value != null) {
            this.musicFile = value;
        }

        value = readSetting(filePath, "volMusic");
        if (value != null) {
            try {
                setVolMusic(Integer.parseInt(value)); // Używa settera z walidacją
            } catch (NumberFormatException e) {
                System.err.println("Invalid volMusic value: " + value);
            }
        }

        value = readSetting(filePath, "isCrossVisible");
        if (value != null) {
            this.isCrossVisible = Boolean.parseBoolean(value);
        }

        value = readSetting(filePath, "crossSize");
        if (value != null) {
            try {
                this.crossSize = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid crossSize value: " + value);
            }
        }

        value = readSetting(filePath, "crossColor");
        if (value != null) {
            this.crossColor = value;
        }

        value = readSetting(filePath, "isCircleVisible");
        if (value != null) {
            this.isCircleVisible = Boolean.parseBoolean(value);
        }

        value = readSetting(filePath, "circleSize");
        if (value != null) {
            try {
                this.circleSize = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid circleSize value: " + value);
            }
        }

        value = readSetting(filePath, "circleColor");
        if (value != null) {
            this.circleColor = value;
        }

        value = readSetting(filePath, "isSnakeVisible");
        if (value != null) {
            this.isSnakeVisible = Boolean.parseBoolean(value);
        }

        value = readSetting(filePath, "snakeColor");
        if (value != null) {
            this.snakeColor = value;
        }

        value = readSetting(filePath, "snakeLength");
        if (value != null) {
            try {
                this.snakeLength = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid snakeLength value: " + value);
            }
        }

        value = readSetting(filePath, "isBackgroundVisible");
        if (value != null) {
            this.isBackgroundVisible = Boolean.parseBoolean(value);
        }

        value = readSetting(filePath, "backgroundColor");
        if (value != null) {
            this.backgroundColor = value;
        }

        value = readSetting(filePath, "isLineVisible");
        if (value != null) {
            this.isLineVisible = Boolean.parseBoolean(value);
        }

        value = readSetting(filePath, "lineColor");
        if (value != null) {
            this.lineColor = value;
        }

        value = readSetting(filePath, "isExplosionEnabled");
        if (value != null) {
            this.isExplosionEnabled = Boolean.parseBoolean(value);
        }

        value = readSetting(filePath, "minExploDelay");
        if (value != null) {
            try {
                this.minExploDelay = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid minExploDelay value: " + value);
            }
        }

        value = readSetting(filePath, "maxExploDelay");
        if (value != null) {
            try {
                this.maxExploDelay = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid maxExploDelay value: " + value);
            }
        }
    }
}