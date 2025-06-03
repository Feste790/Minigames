package org.example.testowo_javafxv3;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.animatedbackgroundcomp.AnimatedBackground;
import org.example.animatedbackgroundcomp.AnimationConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Scores extends Pane {

    private Runnable onExit;
    private AnimationConfig config;
    private String menuSnakeColor;
    private String menuBackColor;
    private String menuLineColor;
    private ScrollPane scrollPane;
    private VBox mainScoresBoxEasy;
    private VBox mainScoresBoxMediumPVP;
    private VBox mainScoresBoxHard;

    private static final String SAPER_SCORES_FILE_NAME = "saperScores.txt";
    private static final Path SAPER_SCORES_PATH = Paths.get(System.getProperty("user.home"), ".saper", SAPER_SCORES_FILE_NAME);
    private static final String SNAKE_SCORES_FILE_NAME = "snakeScores.txt";
    private static final Path SNAKE_SCORES_PATH = Paths.get(System.getProperty("user.home"), ".snake", SNAKE_SCORES_FILE_NAME);
    private static final String TICTACTOE_SCORES_FILE_NAME = "ticTacToeScores.txt";
    private static final Path TICTACTOE_SCORES_PATH = Paths.get(System.getProperty("user.home"), ".tictactoe", TICTACTOE_SCORES_FILE_NAME);

    public Scores(Runnable onExit) {
        this.onExit = onExit;
        config = new AnimationConfig();

        menuSnakeColor = config.getSnakeColor();
        menuBackColor = config.getBackgroundColor();
        menuLineColor = config.getLineColor();

        // Ładowanie wyników
        Map<String, List<Score>> allSaperScores = loadSaperScores();
        List<Score> easySaperScores = allSaperScores.getOrDefault("easy", new ArrayList<>());
        List<Score> mediumSaperScores = allSaperScores.getOrDefault("medium", new ArrayList<>());
        List<Score> hardSaperScores = allSaperScores.getOrDefault("hard", new ArrayList<>());

        Map<String, List<Score>> allSnakeScores = loadSnakeScores();
        List<Score> easySnakeScores = allSnakeScores.getOrDefault("easy", new ArrayList<>());
        List<Score> mediumSnakeScores = allSnakeScores.getOrDefault("medium", new ArrayList<>());
        List<Score> hardSnakeScores = allSnakeScores.getOrDefault("hard", new ArrayList<>());

        Map<String, List<Score>> allTicTacToeScores = loadTicTacToeScores();
        List<Score> easyTicTacToeScores = allTicTacToeScores.getOrDefault("easyai", new ArrayList<>());
        List<Score> hardTicTacToeScores = allTicTacToeScores.getOrDefault("hardai", new ArrayList<>());
        List<Score> pvpTicTacToeScores = allTicTacToeScores.getOrDefault("pvp", new ArrayList<>());

        // Ładowanie czcionki
        Font.loadFont(getClass().getResourceAsStream("/fonts/Minecraftia-Regular.ttf"), 16);

        // Utworzenie widoku wyników
        StackPane root = new StackPane();

        // Konfiguracja animowanego tła
        AnimatedBackground animatedBackground = new AnimatedBackground();
        AnimationConfig animationConfig = animatedBackground.getConfig();
        animationConfig.setSnakeVisible(false);
        animationConfig.setCircleSize(40);
        animationConfig.setCrossSize(60);
        animatedBackground.init();
        root.getChildren().add(animatedBackground);

        // BorderPane dla wyników
        BorderPane borderPane = new BorderPane();
        mainScoresBoxEasy = new VBox();
        mainScoresBoxEasy.setAlignment(Pos.CENTER);
        mainScoresBoxEasy.setPrefWidth(600);

        mainScoresBoxMediumPVP = new VBox();
        mainScoresBoxMediumPVP.setAlignment(Pos.CENTER);
        mainScoresBoxMediumPVP.setPrefWidth(600);

        mainScoresBoxHard = new VBox();
        mainScoresBoxHard.setAlignment(Pos.CENTER);
        mainScoresBoxHard.setPrefWidth(600);

        VBox leftScoresBox = new VBox();
        leftScoresBox.setMaxWidth(100);
        VBox rightScoresBox = new VBox();
        rightScoresBox.setMaxWidth(100);
        VBox topScoresBox = new VBox();
        topScoresBox.setPrefHeight(100);
        VBox bottomScoresBox = new VBox();
        bottomScoresBox.setPrefHeight(100);

        // EASY
        Label lSnakeScoresEasy = new Label("Snake Easy");
        lSnakeScoresEasy.getStyleClass().add("main-label");
        Label lSaperScoresEasy = new Label("Saper Easy");
        lSaperScoresEasy.getStyleClass().add("main-label");
        Label lTicTacToeScoresEasy = new Label("Tic-Tac-Toe Easy");
        lTicTacToeScoresEasy.getStyleClass().add("main-label");

        GridPane gridScoresSnakeEasy = createGridPane();
        for (int i = 0; i < Math.min(3, easySnakeScores.size()); i++) {
            Score score = easySnakeScores.get(i);
            gridScoresSnakeEasy.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresSnakeEasy.add(createButton(score.user), 1, i);
            gridScoresSnakeEasy.add(createButton(String.valueOf(score.score)), 2, i);
        }
        GridPane gridScoresSaperEasy = createGridPane();
        for (int i = 0; i < Math.min(3, easySaperScores.size()); i++) {
            Score score = easySaperScores.get(i);
            gridScoresSaperEasy.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresSaperEasy.add(createButton(score.user), 1, i);
            gridScoresSaperEasy.add(createButton(String.valueOf(score.score)), 2, i);
        }
        GridPane gridScoresTicTacToeEasy = createGridPane();
        for (int i = 0; i < Math.min(3, easyTicTacToeScores.size()); i++) {
            Score score = easyTicTacToeScores.get(i);
            gridScoresTicTacToeEasy.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresTicTacToeEasy.add(createButton(score.user), 1, i);
            gridScoresTicTacToeEasy.add(createButton(String.valueOf(score.score)), 2, i);
        }

        mainScoresBoxEasy.getChildren().addAll(
                lSnakeScoresEasy, gridScoresSnakeEasy,
                lSaperScoresEasy, gridScoresSaperEasy,
                lTicTacToeScoresEasy, gridScoresTicTacToeEasy
        );

        // MEDIUM / PVP
        Label lSnakeScoresMedium = new Label("Snake Medium");
        lSnakeScoresMedium.getStyleClass().add("main-label");
        Label lSaperScoresMedium = new Label("Saper Medium");
        lSaperScoresMedium.getStyleClass().add("main-label");
        Label lTicTacToeScoresPVP = new Label("Tic-Tac-Toe PVP");
        lTicTacToeScoresPVP.getStyleClass().add("main-label");

        GridPane gridScoresSnakeMedium = createGridPane();
        for (int i = 0; i < Math.min(3, mediumSnakeScores.size()); i++) {
            Score score = mediumSnakeScores.get(i);
            gridScoresSnakeMedium.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresSnakeMedium.add(createButton(score.user), 1, i);
            gridScoresSnakeMedium.add(createButton(String.valueOf(score.score)), 2, i);
        }
        GridPane gridScoresSaperMedium = createGridPane();
        for (int i = 0; i < Math.min(3, mediumSaperScores.size()); i++) {
            Score score = mediumSaperScores.get(i);
            gridScoresSaperMedium.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresSaperMedium.add(createButton(score.user), 1, i);
            gridScoresSaperMedium.add(createButton(String.valueOf(score.score)), 2, i);
        }
        GridPane gridScoresTicTacToePVP = createGridPane();
        for (int i = 0; i < Math.min(3, pvpTicTacToeScores.size()); i++) {
            Score score = pvpTicTacToeScores.get(i);
            gridScoresTicTacToePVP.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresTicTacToePVP.add(createButton(score.user), 1, i);
            gridScoresTicTacToePVP.add(createButton(String.valueOf(score.score)), 2, i);
        }

        mainScoresBoxMediumPVP.getChildren().addAll(
                lSnakeScoresMedium, gridScoresSnakeMedium,
                lSaperScoresMedium, gridScoresSaperMedium,
                lTicTacToeScoresPVP, gridScoresTicTacToePVP
        );

        // HARD
        Label lSnakeScoresHard = new Label("Snake Hard");
        lSnakeScoresHard.getStyleClass().add("main-label");
        Label lSaperScoresHard = new Label("Saper Hard");
        lSaperScoresHard.getStyleClass().add("main-label");
        Label lTicTacToeScoresHard = new Label("Tic-Tac-Toe Hard");
        lTicTacToeScoresHard.getStyleClass().add("main-label");

        GridPane gridScoresSnakeHard = createGridPane();
        for (int i = 0; i < Math.min(3, hardSnakeScores.size()); i++) {
            Score score = hardSnakeScores.get(i);
            gridScoresSnakeHard.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresSnakeHard.add(createButton(score.user), 1, i);
            gridScoresSnakeHard.add(createButton(String.valueOf(score.score)), 2, i);
        }
        GridPane gridScoresSaperHard = createGridPane();
        for (int i = 0; i < Math.min(3, hardSaperScores.size()); i++) {
            Score score = hardSaperScores.get(i);
            gridScoresSaperHard.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresSaperHard.add(createButton(score.user), 1, i);
            gridScoresSaperHard.add(createButton(String.valueOf(score.score)), 2, i);
        }
        GridPane gridScoresTicTacToeHard = createGridPane();
        for (int i = 0; i < Math.min(3, hardTicTacToeScores.size()); i++) {
            Score score = hardTicTacToeScores.get(i);
            gridScoresTicTacToeHard.add(createButton(String.valueOf(i + 1)), 0, i);
            gridScoresTicTacToeHard.add(createButton(score.user), 1, i);
            gridScoresTicTacToeHard.add(createButton(String.valueOf(score.score)), 2, i);
        }

        mainScoresBoxHard.getChildren().addAll(
                lSnakeScoresHard, gridScoresSnakeHard,
                lSaperScoresHard, gridScoresSaperHard,
                lTicTacToeScoresHard, gridScoresTicTacToeHard
        );

        // Przycisk powrotu
        Button bExitButton = new Button("Back");
        bExitButton.getStyleClass().add("button");
        bExitButton.setMaxWidth(300);
        bExitButton.setOnAction(e -> {
            if (onExit != null) {
                onExit.run();
            }
        });
        bottomScoresBox.setAlignment(Pos.CENTER);
        bottomScoresBox.getChildren().add(bExitButton);

        // Przyciski przełączania trudności
        GridPane gridEasyMediumHard = createGridPaneSame();
        Button easyButton = createButton("EASY");
        easyButton.setOnAction(e -> setEasyGrid());
        gridEasyMediumHard.add(easyButton, 0, 0);
        Button mediumPVPButton = createButton("MEDIUM / PVP");
        mediumPVPButton.setOnAction(e -> setMediumPVPGrid());
        gridEasyMediumHard.add(mediumPVPButton, 1, 0);
        Button hardButton = createButton("HARD");
        hardButton.setOnAction(e -> setHardGrid());
        gridEasyMediumHard.add(hardButton, 2, 0);
        gridEasyMediumHard.setAlignment(Pos.CENTER);
        topScoresBox.getChildren().add(gridEasyMediumHard);

        // ScrollPane
        scrollPane = new ScrollPane();
        scrollPane.setMaxSize(600, 400);
        scrollPane.setContent(mainScoresBoxEasy);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.getStyleClass().add("scroll-pane");

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(leftScoresBox);
        borderPane.setRight(rightScoresBox);
        borderPane.setTop(topScoresBox);
        borderPane.setBottom(bottomScoresBox);
        root.getChildren().add(borderPane);

        // Dodanie widoku do Scores (Pane)
        getChildren().add(root);
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("button");
        GridPane.setHgrow(button, Priority.ALWAYS);
        return button;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(10);
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(45);
        col2.setHgrow(Priority.ALWAYS);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(45);
        col3.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(col1, col2, col3);

        return gridPane;
    }

    private GridPane createGridPaneSame() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(33.33);
        col.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(col, col, col);

        return gridPane;
    }

    private void setEasyGrid() {
        scrollPane.setContent(mainScoresBoxEasy);
    }

    private void setMediumPVPGrid() {
        scrollPane.setContent(mainScoresBoxMediumPVP);
    }

    private void setHardGrid() {
        scrollPane.setContent(mainScoresBoxHard);
    }

    public AnimationConfig getConfig() {
        return config;
    }

    private Map<String, List<Score>> loadSaperScores() {
        Map<String, List<Score>> scoresMap = new HashMap<>();
        scoresMap.put("easy", new ArrayList<>());
        scoresMap.put("medium", new ArrayList<>());
        scoresMap.put("hard", new ArrayList<>());

        try {
            List<String> lines = Files.readAllLines(SAPER_SCORES_PATH);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String user = parts[0].trim();
                    String difficulty = parts[1].trim().toLowerCase();
                    int scoreValue = Integer.parseInt(parts[2].trim());
                    if (scoresMap.containsKey(difficulty)) {
                        scoresMap.get(difficulty).add(new Score(user, difficulty, scoreValue));
                    }
                }
            }

            // Sortuj wyniki malejąco dla Sapera (wyższy wynik = lepszy)
            for (List<Score> list : scoresMap.values()) {
                list.sort((s1, s2) -> Integer.compare(s2.score, s1.score));
            }
        } catch (IOException e) {
            System.err.println("Błąd wczytywania wyników Sapera: " + e.getMessage());
        }

        return scoresMap;
    }

    private Map<String, List<Score>> loadSnakeScores() {
        Map<String, List<Score>> scoresMap = new HashMap<>();
        scoresMap.put("easy", new ArrayList<>());
        scoresMap.put("medium", new ArrayList<>());
        scoresMap.put("hard", new ArrayList<>());

        try {
            List<String> lines = Files.readAllLines(SNAKE_SCORES_PATH);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String user = parts[0].trim();
                    String difficulty = parts[1].trim().toLowerCase();
                    int scoreValue = Integer.parseInt(parts[2].trim());
                    if (scoresMap.containsKey(difficulty)) {
                        scoresMap.get(difficulty).add(new Score(user, difficulty, scoreValue));
                    }
                }
            }

            // Sortuj wyniki malejąco dla Snake (wyższy wynik = lepszy)
            for (List<Score> list : scoresMap.values()) {
                list.sort((s1, s2) -> Integer.compare(s2.score, s1.score));
            }
        } catch (IOException e) {
            System.err.println("Błąd wczytywania wyników Snake: " + e.getMessage());
        }

        return scoresMap;
    }

    private Map<String, List<Score>> loadTicTacToeScores() {
        Map<String, List<Score>> scoresMap = new HashMap<>();
        scoresMap.put("easyai", new ArrayList<>());
        scoresMap.put("hardai", new ArrayList<>());
        scoresMap.put("pvp", new ArrayList<>());

        try {
            List<String> lines = Files.readAllLines(TICTACTOE_SCORES_PATH);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String user = parts[0].trim();
                    int difficultyValue = Integer.parseInt(parts[1].trim());
                    int scoreValue = Integer.parseInt(parts[2].trim());
                    String difficulty = switch (difficultyValue) {
                        case 0 -> "easyai";
                        case 1 -> "hardai";
                        case 2 -> "pvp";
                        default -> null;
                    };
                    if (difficulty != null && scoresMap.containsKey(difficulty)) {
                        scoresMap.get(difficulty).add(new Score(user, difficulty, scoreValue));
                    }
                }
            }

            // Sortuj wyniki malejąco dla Tic-Tac-Toe (wyższy wynik = więcej wygranych)
            for (List<Score> list : scoresMap.values()) {
                list.sort((s1, s2) -> Integer.compare(s2.score, s1.score));
            }
        } catch (IOException e) {
            System.err.println("Błąd wczytywania wyników Tic-Tac-Toe: " + e.getMessage());
        }

        return scoresMap;
    }

    private record Score(String user, String difficulty, int score) {}
}