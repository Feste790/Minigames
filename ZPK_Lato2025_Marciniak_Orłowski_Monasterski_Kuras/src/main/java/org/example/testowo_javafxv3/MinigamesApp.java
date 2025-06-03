package org.example.testowo_javafxv3;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.animatedbackgroundcomp.AnimatedBackground;
import org.example.animatedbackgroundcomp.AnimationConfig;
import saper.SaperComp;
import tictactoe.TicTacToeComp;
import Sneake_Game.SnakeGame;

import java.io.IOException;

public class MinigamesApp extends Application {

    private Scene scene;
    private StackPane root;
    private Settings settings;
    private Scores scores;
    private SaperComp saperComp;
    private TicTacToeComp ticTacToeComp;
    private SnakeGame snakeGame;

    private AnimatedBackground animatedBackground;
    private AnimationConfig animationConfig;
    private MediaPlayer player;
    private String filePath = "config.txt";
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Font.loadFont(getClass().getResourceAsStream("/fonts/Minecraftia-Regular.ttf"), 16);
        scene = new Scene(new StackPane(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        animatedBackground = new AnimatedBackground();
        animationConfig = new AnimationConfig();
        animationConfig.setMusicFile("/sounds/PixelGlide3.mp3");

        animatedBackground.init();


        //ODTWARZANIE DO POPRAWY - STOPOWANIE, ZMIANA GLOSNOSCI
        Media media = new Media(getClass().getResource(animationConfig.getMusicFile()).toExternalForm());
        player = new MediaPlayer(media);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();

        createMainMenu();
        loadSettings();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Minigames");
        primaryStage.show();

    }

    private void createMainMenu(){
        root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(animatedBackground);

        VBox menuButtons = new VBox(10);
        menuButtons.setAlignment(Pos.CENTER);

        Button snakeButton = createButton("Snake", 150);
        Button saperButton = createButton("Saper", 150);
        Button ticTacToeButton = createButton("TicTacToe", 150);
        Button settingsButton = createButton("Settings", 150);

        Region spacer = new Region();
        spacer.setPrefHeight(40);

        Button scoresButton = createButton("Scores", 150);

        menuButtons.getChildren().addAll(snakeButton, saperButton, ticTacToeButton, settingsButton, spacer, scoresButton);
        root.getChildren().add(menuButtons);

        scene.setRoot(root);
    }

    private void showMainMenu() {
        loadSettings();
        if (saperComp != null) {
            saperComp.dispose();
        }
       // System.out.println(primaryStage);
        scene.setRoot(root);
    }

    private void loadSettings(){
        try {
            animationConfig.loadSettings("config.txt");
            animatedBackground.setConfig(animationConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Button createButton(String text, int width) {
        Button button = new Button(text);
        button.setMaxWidth(width);
        button.setOnAction(e -> {
            switch (text) {
                case "Snake" -> showSnake();
                case "Saper" -> showSaper();
                case "TicTacToe" -> showTicTacToe();
                case "Settings" -> showSettings();
                case "Scores" -> showScores();
            }
        });
        return button;
    }

    private void setVol(){
        if(settings.isVolOn() == false){
            player.pause();
        }else{
            player.play();
        }
        player.setVolume((float)settings.getVolMusic()/100);
    }

    private void showSnake() {
        snakeGame = new SnakeGame(this::showMainMenu);
    }

    private void showSaper() {
        saperComp = new SaperComp(this::showMainMenu);
    }

    private void showTicTacToe() {
        ticTacToeComp = new TicTacToeComp(this::showMainMenu);
    }

    private void showSettings() {
        settings = new Settings(this::showMainMenu, this::setVol);
        scene.setRoot(settings);
    }

    private void showScores() {
        scores = new Scores(this::showMainMenu);
        scene.setRoot(scores);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
