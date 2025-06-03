package org.example.animatedbackgroundcomp;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Random;

public class AnimatedBackground extends Pane {
    private AnimationConfig config;
    private Random random = new Random();
    private AnimatedSnake snake;
    private Media sound;
    private MediaPlayer mediaPlayer;
    private Line verticalLine;
    private Line horizontalLine;

    public AnimatedBackground() {
        this.config = new AnimationConfig();
        setPrefSize(config.getWidth(), config.getHeight());
    }

    public void init() {
        setStyle("-fx-background-color: " + config.getBackgroundColor() + ";");
        addLines(this);

        this.snake = new AnimatedSnake(
                config.getWidth(),
                config.getHeight(),
                config.isSnakeVisible(),
                config.getSnakeColor(),
                config.getSnakeLength());
        snake.init(this);
        addCircleAndCross(this);
        addExplosives(this);
        addMusic();
    }

    private void addMusic(){
        String musicFile = "";
        try{
            musicFile = getClass().getResource(config.getMusicFile()).toExternalForm();
        }catch(Exception e){
            config.setMusicEnabled(false);
        }
        if(config.isMusicEnabled()){
            sound = new Media(musicFile);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(1000);
            mediaPlayer.play();
        }

    }

    private void addExplosives(Pane root){
        Timeline tickerBomb = new Timeline(new KeyFrame(Duration.millis(random.nextInt(config.getMinExploDelay(), config.getMaxExploDelay())), event -> {
            Circle bomb = new Circle(random.nextInt(30, (int) config.getWidth() - 30), random.nextInt(30, (int) config.getHeight() - 30), 10, Color.RED);
            if(config.isExplosionEnabled()) {
                root.getChildren().add(bomb);
                playExplosion(bomb, root);
            }
        }));
        tickerBomb.setCycleCount(Animation.INDEFINITE);
        tickerBomb.play();
    }

    private void addCircleAndCross(Pane root){
        Timeline ticker = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            // Losowy wybór: kółko lub krzyżyk
            if (random.nextBoolean()) {
                if (config.isCircleVisible()) {
                    addCircle(root);
                }
            } else {
                if (config.isCrossVisible()) {
                    addCross(root);
                }
            }
        }));
        ticker.setCycleCount(Animation.INDEFINITE);
        ticker.play();
    }

    private void addLines(Pane root){
        for (int x = 0; x <= config.getWidth(); x += 50) {
            verticalLine = new Line(x, 0, x, config.getHeight());
            verticalLine.setStroke(Color.web(config.getLineColor()));
            verticalLine.setStrokeWidth(1);
            if(config.isLineVisible()) {
                root.getChildren().add(verticalLine);
            }
        }
        for (int y = 0; y <= config.getHeight(); y += 50) {
            horizontalLine = new Line(0, y, config.getWidth(), y);
            horizontalLine.setStroke(Color.web(config.getLineColor()));
            horizontalLine.setStrokeWidth(1);
            if(config.isLineVisible()) {
                root.getChildren().add(horizontalLine);
            }
        }
    }

    private void addCircle(Pane root) {
        Circle circle = new Circle(config.getCircleSize(), Color.web(config.getCircleColor()));
        circle.setStroke(Color.web(config.getCircleColor()));
        circle.setStrokeWidth(2);
        circle.setFill(null); // Tylko obwódka
        circle.setCenterX(random.nextInt(config.getWidth()-40) + 20); // Losowa pozycja X (20-780)
        circle.setCenterY(random.nextInt(config.getHeight()-40) + 20); // Losowa pozycja Y (20-580)

        FadeTransition fade = new FadeTransition(Duration.millis(3000), circle);
        fade.setFromValue(1.0); // Pełna widoczność
        fade.setToValue(0.0);   // Zniknięcie
        fade.setOnFinished(e -> {
            root.getChildren().remove(circle);
        });
        fade.play();

        root.getChildren().add(circle);
    }

    private void addCross(Pane root) {
        double size = config.getCrossSize();
        double x = random.nextInt(config.getWidth()-40) + 20;
        double y = random.nextInt(config.getHeight()-40) + 20;

        Line line1 = new Line(x - size / 2, y - size / 2, x + size / 2, y + size / 2);
        Line line2 = new Line(x + size / 2, y - size / 2, x - size / 2, y + size / 2);
        line1.setStroke(Color.web(config.getCrossColor()));
        line2.setStroke(Color.web(config.getCrossColor()));
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);

        FadeTransition fade1 = new FadeTransition(Duration.millis(3000), line1);
        FadeTransition fade2 = new FadeTransition(Duration.millis(3000), line2);
        fade1.setFromValue(1.0);
        fade1.setToValue(0.0);
        fade2.setFromValue(1.0);
        fade2.setToValue(0.0);
        fade1.setOnFinished(e -> root.getChildren().remove(line1));
        fade2.setOnFinished(e -> root.getChildren().remove(line2));
        fade1.play();
        fade2.play();

        root.getChildren().addAll(line1, line2);
    }

    public static void playExplosion(Node node, Pane parentPane) {
        Duration duration = Duration.seconds(0.5);

        // powiększenie
        ScaleTransition scale = new ScaleTransition(duration, node);
        scale.setToX(4);
        scale.setToY(4);

        // znikanie
        FadeTransition fade = new FadeTransition(duration, node);
        fade.setToValue(0);

        // zmiana koloru, jeśli węzeł obsługuje fill
        FillTransition fill = null;
        if (node instanceof Shape) {
            fill = new FillTransition(duration, (Shape) node, Color.RED, Color.ORANGE);
        }

        // równoczesne przejścia
        ParallelTransition explosion;
        if (fill != null) {
            explosion = new ParallelTransition(scale, fade, fill);
        } else {
            explosion = new ParallelTransition(scale, fade);
        }
        explosion.setOnFinished(e -> parentPane.getChildren().remove(node));
        explosion.play();
    }

    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        // if (tickerBomb != null) tickerBomb.stop();
        //if (tickerCircleCross != null) tickerCircleCross.stop();
        //if (pathTransition != null) pathTransition.stop();
        getChildren().clear();
    }

    private void updateLineColor() {
        for (Node node : this.getChildren()) {
            if (node instanceof Line line && line.getStrokeWidth() == 1) {
                line.setStroke(Color.web(config.getLineColor()));
            }
        }
    }

    private void updateLineVisibility(){
        boolean visible = config.isLineVisible();
        for (Node node : this.lookupAll("#grid-line")) {
            node.setVisible(visible);
        }
    }

    private void updateSnakeColor() {
        if (snake.getSnakeSegments() != null && config.isSnakeVisible()) {
            for (Rectangle r : snake.getSnakeSegments()) {
                r.setFill(Color.web(config.getSnakeColor()));
            }
        }
    }

    private void updateSnakeVisibility() {
        if (snake.getSnakeSegments() != null) {
            for (Rectangle r : snake.getSnakeSegments()) {
                r.setVisible(config.isSnakeVisible());
            }
        }
    }

    private void updateBackgroundColor(){
        setStyle("-fx-background-color: " + config.getBackgroundColor() + ";");
    }

    public void updateSettings(){
        updateSnakeVisibility();
        updateLineVisibility();
        updateLineColor();
        updateSnakeColor();
        updateBackgroundColor();
    }

    public void setAnimatedSnakeColor(String color){
        snake.setSnakeColor(color);
    }

    public AnimationConfig getConfig(){
        return this.config;
    }

    public void setConfig(AnimationConfig config){
        this.config = config;
        updateSettings();
    }

}
