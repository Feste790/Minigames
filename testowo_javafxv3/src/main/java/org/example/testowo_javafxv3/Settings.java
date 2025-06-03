package org.example.testowo_javafxv3;

import Sneake_Game.SnakeConfig;
import Sneake_Game.SnakeGame;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.animatedbackgroundcomp.AnimatedBackground;
import org.example.animatedbackgroundcomp.AnimationConfig;
import saper.SaperConfig;
import tictactoe.TicTacToeConfig;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Settings extends Pane {

    private int volMusic;
    private String menuSnakeColor;
    private String menuBackColor;
    private String menuLineColor;
    private Runnable onExit;
    private Runnable setVol;
    private AnimationConfig config;
    private SaperConfig saperConfig;
    private TicTacToeConfig ticTacToeConfig;
    private SnakeConfig snakeConfig;

    private TextField tPlayer1;
    private TextField tPlayer2;
    private String player1 = "Gracz_1";
    private String player2 = "Gracz_2";

    private String filePath = "config.txt";

    public Settings(Runnable onExit, Runnable setVol) {

        this.setVol = setVol;
        this.onExit = onExit;
        config = new AnimationConfig();
        saperConfig = new SaperConfig();
        ticTacToeConfig = new TicTacToeConfig();
        snakeConfig = new SnakeConfig();


        try {
            snakeConfig.loadSettings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Wczytywanie ustawień z plików konfiguracyjnych
        try {
            saperConfig.loadSettings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ticTacToeConfig.loadSettings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            config.loadSettings(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        volMusic = config.getVolMusic();
        menuSnakeColor = config.getSnakeColor();
        menuBackColor = config.getBackgroundColor();
        menuLineColor = config.getLineColor();

        // Ładowanie czcionki
        Font.loadFont(getClass().getResourceAsStream("/fonts/Minecraftia-Regular.ttf"), 16);

        // Utworzenie widoku ustawień
        StackPane root = new StackPane();

        // Konfiguracja animowanego tła
        AnimatedBackground animatedBackground = new AnimatedBackground();
        AnimationConfig animationConfig = animatedBackground.getConfig();
        animationConfig.setSnakeVisible(false);
        animationConfig.setCircleSize(40);
        animationConfig.setCrossSize(60);
        animatedBackground.init();
        root.getChildren().add(animatedBackground);

        // BorderPane dla ustawień
        BorderPane borderPane = new BorderPane();
        VBox mainSettingsBox = new VBox();
        mainSettingsBox.setAlignment(Pos.CENTER);
        mainSettingsBox.setPrefWidth(600);
        VBox leftSettingsBox = new VBox();
        leftSettingsBox.setPrefWidth(100);
        VBox rightSettingsBox = new VBox();
        rightSettingsBox.setPrefWidth(86);
        VBox topSettingsBox = new VBox();
        topSettingsBox.setPrefHeight(100);
        VBox bottomSettingsBox = new VBox();
        bottomSettingsBox.setPrefHeight(100);

        // Etykiety ustawień
        Label lSnakeSettings = new Label("Snake");
        lSnakeSettings.getStyleClass().add("main-label");
        Label lSaperSettings = new Label("Saper");
        lSaperSettings.getStyleClass().add("main-label");
        Label lTicTacToeSettings = new Label("TicTacToe");
        lTicTacToeSettings.getStyleClass().add("main-label");
        Label lMenuSettings = new Label("Menu");
        lMenuSettings.getStyleClass().add("main-label");

        // GridPane dla każdej sekcji ustawień
        GridPane gridSettingsSnake = createGridPane();
        GridPane gridSettingsSaper = createGridPane();
        GridPane gridSettingsTicTacToe = createGridPane();
        GridPane gridSettingsMenu = createGridPane();

        // Ustawienia dla Snake
        Button bSnakeColor = createButton("Snake color");
        bSnakeColor.setStyle(toTextCSS(colorToHex(snakeConfig.getSnakeColor())));
        bSnakeColor.setOnAction(e->changeSnakeColor(bSnakeColor));

        Button bSnakeDifficulty = createButton(getBSnakeDifficulty());
        bSnakeDifficulty.setOnAction(e->changeSnakeDifficulty(bSnakeDifficulty));

        Button bSnakeBackgroundColor = createButton("Background Color");
        bSnakeBackgroundColor.setStyle(toTextCSS(colorToHex(snakeConfig.getBackgroundColor())));
        bSnakeBackgroundColor.setOnAction(e->changeSnakeBackgroundColor(bSnakeBackgroundColor));

        Button bSnakeAppleColor= createButton("Apple color");
        bSnakeAppleColor.setStyle(toTextCSS(colorToHex(snakeConfig.getAppleColor())));
        bSnakeAppleColor.setOnAction(e->changeSnakeAppleColor(bSnakeAppleColor));

        gridSettingsSnake.add(bSnakeColor, 0, 0);
        gridSettingsSnake.add(bSnakeDifficulty, 1, 0);
        gridSettingsSnake.add(bSnakeBackgroundColor, 0, 1);
        gridSettingsSnake.add(bSnakeAppleColor, 1, 1);

        // Ustawienia dla Saper
        Button bSaperMines = createButton(getBSaperMines());
        Button bSaperMaxNumber = createButton(getBSaperMaxNumber());
        Button bSaperDimensions = createButton(getBSaperDimensions());

        Button bSaperDifficulty = createButton(getBSaperDifficulty());
        bSaperDifficulty.setOnAction(e->changeSaperDifficulty(bSaperDifficulty,bSaperDimensions,bSaperMines,bSaperMaxNumber));

        gridSettingsSaper.add(bSaperDifficulty, 0, 0);
        gridSettingsSaper.add(bSaperMines, 1, 0);
        gridSettingsSaper.add(bSaperMaxNumber, 0, 1);
        gridSettingsSaper.add(bSaperDimensions, 1, 1);

        // Ustawienia dla TicTacToe
        Button bTicTacToeGameMode = createButton(getBTicTacToeGameMode());
        bTicTacToeGameMode.setOnAction(e->ticTacToeChangeGameMode(bTicTacToeGameMode));

        Button bTicTacToeTextColor = createButton("TicTacToe Text Color");
        bTicTacToeTextColor.setStyle(toTextCSS(ticTacToeConfig.getTextColor()));
        bTicTacToeTextColor.setOnAction(e->ticTacToeTextColor(bTicTacToeTextColor));

        Button bTicTacToeTest1 = createButton("Cross");
        bTicTacToeTest1.setOnAction(e->openFileChooser(new Stage(), "cross"));

        Button bTicTacToeTest2 = createButton("Circle");
        bTicTacToeTest2.setOnAction(e->openFileChooser(new Stage(), "circle"));

        gridSettingsTicTacToe.add(bTicTacToeGameMode, 0, 0);
        gridSettingsTicTacToe.add(bTicTacToeTextColor, 1, 0);
        gridSettingsTicTacToe.add(bTicTacToeTest1, 0, 1);
        gridSettingsTicTacToe.add(bTicTacToeTest2, 1, 1);

        // Ustawienia dla Menu
        Button bMenuColor = createButton("SnakeColor");
        bMenuColor.setStyle(toTextCSS(menuSnakeColor));
        bMenuColor.setOnAction(e -> changeMenuSnakeColor(bMenuColor));

        Button bMenuSnakeOn = createButton(nameButtonSnakeVisibility());
        bMenuSnakeOn.setOnAction(e ->  changeSnakeVisibility(bMenuSnakeOn));

        Button bMenuBackColor = createButton("Background Color");
        bMenuBackColor.setStyle(toTextCSS(menuBackColor));
        bMenuBackColor.setOnAction(e -> changeMenuBackColor(bMenuBackColor));

        Button bMenuBackLines = createButton("Background Lines Color");
        bMenuBackLines.setStyle(toTextCSS(menuLineColor));
        bMenuBackLines.setOnAction(e -> changeMenuLineColor(bMenuBackLines));

        Button bMenuVolOn = createButton("Music ON");
        bMenuVolOn.setOnAction(e -> changeVolumeOn(bMenuVolOn));

        Slider slider = new Slider(0, 100, volMusic);
        slider.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(slider, Priority.ALWAYS);
        Label volLabel = new Label("Volume: " + volMusic);
        volLabel.getStyleClass().add("volume-label");
        volLabel.setMouseTransparent(true);
        StackPane volSliderPane = new StackPane();
        GridPane.setHgrow(volSliderPane, Priority.ALWAYS);
        volSliderPane.getChildren().addAll(slider, volLabel);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            config.setVolMusic(newValue.intValue());
            volMusic = config.getVolMusic();
            volLabel.setText("Volume: " + volMusic);
            setVol.run();
        });

        gridSettingsMenu.add(bMenuColor, 0, 0);
        gridSettingsMenu.add(bMenuSnakeOn, 1, 0);
        gridSettingsMenu.add(bMenuBackColor, 0, 1);
        gridSettingsMenu.add(bMenuBackLines, 1, 1);
        gridSettingsMenu.add(bMenuVolOn, 0, 2);
        gridSettingsMenu.add(volSliderPane, 1, 2);

        GridPane gridPlayers = createGridPane();

        Button bPlayer1 = createButton("Player 1");
        bPlayer1.setMaxWidth(200);
        gridPlayers.add(bPlayer1, 0, 0);

        Button bPlayer2 = createButton("Player 2");
        bPlayer2.setMaxWidth(200);
        gridPlayers.add(bPlayer2, 1, 0);

        tPlayer1 = createTextField(player1);
        tPlayer1.setMaxWidth(200);
        tPlayer1.setMinHeight(40);
        gridPlayers.add(tPlayer1, 0, 1);

        tPlayer2 = createTextField(player2);
        tPlayer2.setMaxWidth(200);
        tPlayer2.setMinHeight(40);
        gridPlayers.add(tPlayer2, 1, 1);

        gridPlayers.setAlignment(Pos.CENTER);
        topSettingsBox.getChildren().add(gridPlayers);


        // Przycisk powrotu
        Button bExitButton = new Button("Back");
        bExitButton.getStyleClass().add("button");
        bExitButton.setMaxWidth(300);
        bExitButton.setOnAction(e -> {
            player1 = tPlayer1.getText();
            player2 = tPlayer2.getText();

            snakeConfig.setPlayer(player1);
            saperConfig.setPlayer(player1);
            ticTacToeConfig.setPlayer1(player1);
            ticTacToeConfig.setPlayer2(player2);

            config.saveSettings("config.txt");
            try {
                snakeConfig.saveSettings();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                saperConfig.saveSettings();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ticTacToeConfig.saveSettings();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (onExit != null) {
                onExit.run();
               // setConfig.run();
            }
        });
        bottomSettingsBox.setAlignment(Pos.CENTER);
        bottomSettingsBox.getChildren().add(bExitButton);

        // Dodanie sekcji do głównego VBox
        mainSettingsBox.getChildren().addAll(
                lSnakeSettings, gridSettingsSnake,
                lSaperSettings, gridSettingsSaper,
                lTicTacToeSettings, gridSettingsTicTacToe,
                lMenuSettings, gridSettingsMenu
        );

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxSize(600, 400);
        scrollPane.setContent(mainSettingsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.getStyleClass().add("scroll-pane");

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(leftSettingsBox);
        borderPane.setRight(rightSettingsBox);
        borderPane.setTop(topSettingsBox);
        borderPane.setBottom(bottomSettingsBox);
        root.getChildren().add(borderPane);

        // Dodanie widoku do Settings (Pane)
        getChildren().add(root);
    }

    private void changeSnakeAppleColor(Button bSnakeAppleColor) {
        if(snakeConfig.getAppleColor().equals(Color.RED)){
            snakeConfig.setAppleColor(Color.CYAN);
        }else if(snakeConfig.getAppleColor().equals(Color.CYAN)) {
            snakeConfig.setAppleColor(Color.YELLOW);
        }else if(snakeConfig.getAppleColor().equals(Color.YELLOW)){
            snakeConfig.setAppleColor(Color.PINK);
        }else if(snakeConfig.getAppleColor().equals(Color.PINK)){
            snakeConfig.setAppleColor(Color.MAGENTA);
        }else if(snakeConfig.getAppleColor().equals(Color.MAGENTA)){
            snakeConfig.setAppleColor(Color.RED);
        }else{
            snakeConfig.setAppleColor(Color.RED);
        }
        bSnakeAppleColor.setStyle(toTextCSS(colorToHex(snakeConfig.getAppleColor())));
    }

    private void changeSnakeBackgroundColor(Button bSnakeBackgroundColor) {
        if(snakeConfig.getBackgroundColor().equals(Color.WHITE)){
            snakeConfig.setBackgroundColor(Color.LIGHT_GRAY);
        }else if(snakeConfig.getBackgroundColor().equals(Color.LIGHT_GRAY)) {
            snakeConfig.setBackgroundColor(Color.GRAY);
        }else if(snakeConfig.getBackgroundColor().equals(Color.GRAY)){
            snakeConfig.setBackgroundColor(Color.DARK_GRAY);
        }else if(snakeConfig.getBackgroundColor().equals(Color.DARK_GRAY)){
            snakeConfig.setBackgroundColor(Color.BLACK);
        }else if(snakeConfig.getBackgroundColor().equals(Color.BLACK)){
            snakeConfig.setBackgroundColor(Color.WHITE);
        }else{
            snakeConfig.setSnakeColor(Color.BLACK);
        }
        bSnakeBackgroundColor.setStyle(toTextCSS(colorToHex(snakeConfig.getBackgroundColor())));
    }

    private void changeSnakeColor(Button bSnakeColor) {
        if(snakeConfig.getSnakeColor().equals(Color.GREEN)){
            snakeConfig.setSnakeColor(Color.CYAN);
        }else if(snakeConfig.getSnakeColor().equals(Color.CYAN)) {
            snakeConfig.setSnakeColor(Color.YELLOW);
        }else if(snakeConfig.getSnakeColor().equals(Color.YELLOW)){
            snakeConfig.setSnakeColor(Color.PINK);
        }else if(snakeConfig.getSnakeColor().equals(Color.PINK)){
            snakeConfig.setSnakeColor(Color.GRAY);
        }else if(snakeConfig.getSnakeColor().equals(Color.GRAY)){
            snakeConfig.setSnakeColor(Color.GREEN);
        }else{
            snakeConfig.setSnakeColor(Color.GREEN);
        }
        bSnakeColor.setStyle(toTextCSS(colorToHex(snakeConfig.getSnakeColor())));
    }

    private String getBSnakeDifficulty() {
        switch (snakeConfig.getDifficulty()) {
            case 0:
                return "Difficulty: EASY";
            case 1:
                return "Difficulty: MEDIUM";
            default:
                return "Difficulty: HARD";
        }
    }

    private void changeSnakeDifficulty(Button bSnakeDifficulty){
        switch (bSnakeDifficulty.getText()){
            case "Difficulty: MEDIUM" -> {
                bSnakeDifficulty.setText("Difficulty: HARD");
                snakeConfig.setDifficulty(2);
            }
            case "Difficulty: HARD" -> {
                bSnakeDifficulty.setText("Difficulty: EASY");
                snakeConfig.setDifficulty(0);
            }
            default -> {
                bSnakeDifficulty.setText("Difficulty: MEDIUM");
                snakeConfig.setDifficulty(1);
            }
        }
    }

    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }

    private String getBTicTacToeTextColor() {
        return ticTacToeConfig.getTextColor();
    }

    private TextField createTextField(String player1) {
        TextField textField = new TextField(player1);
        textField.getStyleClass().add("textfield");
        return textField;
    }

    private void ticTacToeTextColor(Button bTicTacToeTextColor) {
        switch (ticTacToeConfig.getTextColor()){
            case "#000000" -> ticTacToeConfig.setTextColor("#00F000");
            case "#00F000" -> ticTacToeConfig.setTextColor("#0000F0");
            case "#0000F0" -> ticTacToeConfig.setTextColor("#00F0F0");
            case "#00F0F0" -> ticTacToeConfig.setTextColor("#F00000");
            default -> ticTacToeConfig.setTextColor("#000000");
        }
        bTicTacToeTextColor.setStyle(toTextCSS(ticTacToeConfig.getTextColor()));
    }

    private String getBTicTacToeDifficulty() {
        String difficulty;
        if(ticTacToeConfig.getDifficulty() == 0){
            difficulty = "Easy";
        }else{
            difficulty = "Hard";
        }
        return "Bot difficulty: " + difficulty;
    }

    private String getBTicTacToeGameMode() {
        return ticTacToeConfig.getGameMode();
    }

    private void ticTacToeChangeGameMode(Button bTicTacToeGameMode) {
        if(ticTacToeConfig.getGameMode() == "PVP"){
            ticTacToeConfig.setGameMode("EasyAI");
        }else if(ticTacToeConfig.getGameMode() == "EasyAI"){
            ticTacToeConfig.setGameMode("HardAI");
        }else {
            ticTacToeConfig.setGameMode("PVP");
        }
        try {
            ticTacToeConfig.saveSettings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bTicTacToeGameMode.setText(ticTacToeConfig.getGameMode());
    }

    private String getBSaperDimensions() {
        return "Dimensions: " + saperConfig.getCols(saperConfig.getDifficulty()) + " x " +
                saperConfig.getRows(saperConfig.getDifficulty());
    }

    private String getBSaperMaxNumber() {
        return "Max bombs around field: " + saperConfig.getMaxDisplayNumber(saperConfig.getDifficulty());
    }

    private String getBSaperMines() {
        return "Mines: " + saperConfig.getMines(saperConfig.getDifficulty());
    }

    private String getBSaperDifficulty() {
        switch(saperConfig.getDifficulty()){
            case 0: return "Difficulty: EASY";
            case 1: return "Difficulty: MEDIUM";
            default : return"Difficulty: HARD";
        }

    }

    private void changeSaperDifficulty(Button bSaperDifficulty, Button bSaperDimensions, Button bSaperMines, Button bSaperMaxNumber) {
        switch (bSaperDifficulty.getText()){
            case "Difficulty: MEDIUM" -> {
                bSaperDifficulty.setText("Difficulty: HARD");
                saperConfig.setDifficulty(2);
                bSaperMines.setText(getBSaperMines());
                bSaperDimensions.setText(getBSaperDimensions());
                bSaperMaxNumber.setText(getBSaperMaxNumber());
            }
            case "Difficulty: HARD" -> {
                bSaperDifficulty.setText("Difficulty: EASY");
                saperConfig.setDifficulty(0);
                bSaperMines.setText(getBSaperMines());
                bSaperDimensions.setText(getBSaperDimensions());
                bSaperMaxNumber.setText(getBSaperMaxNumber());
            }
            default -> {
                bSaperDifficulty.setText("Difficulty: MEDIUM");
                saperConfig.setDifficulty(1);
                bSaperMines.setText(getBSaperMines());
                bSaperDimensions.setText(getBSaperDimensions());
                bSaperMaxNumber.setText(getBSaperMaxNumber());
            }
        }
    }

    private String toTextCSS(String HexColor){
        return "-fx-text-fill: "+HexColor+";";
    }


    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);
        col2.setHalignment(HPos.LEFT);
        gridPane.getColumnConstraints().addAll(col1, col2);



        return gridPane;
    }


    private Button createButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("button");
        GridPane.setHgrow(button, Priority.ALWAYS);
        return button;
    }

    private void changeSnakeVisibility(Button bMenuSnakeOn){
        if(config.isSnakeVisible()){
            config.setSnakeVisible(false);
            bMenuSnakeOn.setText("Snake OFF");
        }else{
            config.setSnakeVisible(true);
            bMenuSnakeOn.setText("Snake ON");
        }
    }

    private String nameButtonSnakeVisibility(){
        if(config.isSnakeVisible() == false){
            return "Snake OFF";
        }else{
            return "Snake ON";
        }
    }

    public AnimationConfig getConfig(){
        return config;
    }

    private void changeVolumeOn(Button button){
        if(config.isMusicEnabled() == false) {
            //setVolOn(true);
            config.setMusicEnabled(true);
            button.setText("Music ON");
        }else{
            config.setMusicEnabled(false);
            button.setText("Music OFF");
        }
        setVol.run();
    }

    private void changeMenuSnakeColor(Button button) {
        switch (menuSnakeColor){
            case "#F00000" -> menuSnakeColor = "#00F000";
            case "#00F000" -> menuSnakeColor = "#0000F0";
            case "#0000F0" -> menuSnakeColor = "#00F0F0";
            case "#00F0F0" -> menuSnakeColor = "#FFFFFF";
            default -> menuSnakeColor = "#F00000";
        }
        button.setStyle(toTextCSS(menuSnakeColor));
        config.setSnakeColor(menuSnakeColor);
    }

    private void changeMenuBackColor(Button button) {
        switch (menuBackColor){
            case "#000000" -> menuBackColor = "#101010";
            case "#101010" -> menuBackColor = "#202020";
            case "#202020" -> menuBackColor = "#303030";
            case "#303030" -> menuBackColor = "#404040";
            default -> menuBackColor = "#000000";
        }
        button.setStyle(toTextCSS(menuBackColor));
        config.setBackgroundColor(menuBackColor);
    }

    private void changeMenuLineColor(Button button){
        switch (menuLineColor){
            case "#404040" -> menuLineColor = "#C0C0C0";
            case "#C0C0C0" -> menuLineColor = "#D0D0D0";
            case "#D0D0D0" -> menuLineColor = "#E0E0E0";
            case "#E0E0E0" -> menuLineColor = "#F0F0F0";
            default -> menuLineColor = "#404040";
        }
        button.setStyle(toTextCSS(menuLineColor));
        config.setLineColor(menuLineColor);
    }



    public String getMenuSnakeColor() {
        return menuSnakeColor;
    }

    public boolean isVolOn(){
        return config.isMusicEnabled();
    }

    public void setVolOn(boolean isVolOn){
        config.setMusicEnabled(isVolOn);
    }

    public int getVolMusic() {
        return volMusic;
    }

    public void setVolMusic(int volMusic) {
        this.volMusic = volMusic;
    }

    public String readSnakeColor(){
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String c;
            while((line = reader.readLine()) != null){
                if(line.startsWith("snakeColor=")){
                    String[] parts = line.split("=");
                    if(parts.length == 2){
                        switch(parts[1].trim()){
                            case "#800000" -> c = "text-red";
                            case "#FFFFFF" -> c = "text-white";
                            case "#000080" -> c = "text-blue";
                            case "#008080" -> c = "text-cyan";
                            default -> c = "text-green";
                         };
                        return c;
                    }
                }
            }
        }catch(IOException e){
            System.err.println("Read file ERROR: " + e.getMessage());
        }catch (NumberFormatException e){
            System.err.println("Parsing snakeColor ERROR: " + e.getMessage());
        }
        return "#000000";
    }

    private void openFileChooser(Stage stage, String s) {
        System.out.println("TEST");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz Plik");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Obrazy", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            Path filePath = selectedFile.toPath();
            System.out.println(currentDir.relativize(filePath));
            if(s == "cross") {
                ticTacToeConfig.setCrossPNG(String.valueOf(currentDir.relativize(filePath)));
            }else{
                ticTacToeConfig.setCirclePNG(String.valueOf(currentDir.relativize(filePath)));
            }
            try {
                ticTacToeConfig.saveSettings();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}