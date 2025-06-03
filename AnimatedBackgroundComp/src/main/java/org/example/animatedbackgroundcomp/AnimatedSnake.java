package org.example.animatedbackgroundcomp;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimatedSnake {
    private final int width;
    private final int height;
    private final boolean isSnakeVisible;
    private String snakeColor;
    private final int snakeLength;

    private Rectangle[] snakeSegments;

    public AnimatedSnake(int width, int height, boolean isSnakeVisible, String snakeColor, int snakeLength){
        this.width = width;
        this.height = height;
        this.isSnakeVisible = isSnakeVisible;
        this.snakeColor = snakeColor;
        this.snakeLength = snakeLength;
    }

    public void init(Pane root){
        this.snakeSegments = new Rectangle[snakeLength];
        for (int i = 0; i < snakeLength; i++) {
            snakeSegments[i] = new Rectangle(20, 20);
            snakeSegments[i].setFill(Color.web(snakeColor));
            snakeSegments[i].setVisible(isSnakeVisible);
        }

        Path snakePath = new Path();
        snakePath.getElements().add(new MoveTo(500, 300));
        snakePath.getElements().add(new LineTo(500, 400));
        snakePath.getElements().add(new LineTo(300, 400));
        snakePath.getElements().add(new LineTo(300, 500));
        snakePath.getElements().add(new LineTo(200, 500));
        snakePath.getElements().add(new LineTo(200, 300));
        snakePath.getElements().add(new LineTo(300, 300));
        snakePath.getElements().add(new LineTo(300, 100));
        snakePath.getElements().add(new LineTo(600, 100));
        snakePath.getElements().add(new LineTo(600, 300));
        snakePath.getElements().add(new LineTo(500, 300));



        for (int i = 0; i < snakeLength; i++) {
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.seconds(7));
            pathTransition.setPath(snakePath);
            pathTransition.setCycleCount(Animation.INDEFINITE);
            pathTransition.setInterpolator(Interpolator.LINEAR);
            pathTransition.setAutoReverse(false);
            pathTransition.setNode(snakeSegments[i]);
            pathTransition.setDelay(Duration.millis(i * 100));
            pathTransition.play();
        }


            root.getChildren().addAll(snakeSegments);

    }

    public void updateSnake(){

    }

    public Rectangle[] getSnakeSegments(){
        return snakeSegments;
    }

    public void setSnakeColor(String color){
        this.snakeColor = color;
    }
}
