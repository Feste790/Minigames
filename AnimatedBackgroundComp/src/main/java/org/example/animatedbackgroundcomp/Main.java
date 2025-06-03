package org.example.animatedbackgroundcomp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new StackPane(), 800, 600);
        AnimatedBackground animatedBackground = new AnimatedBackground();

        animatedBackground.init();

        AnimationConfig config = new AnimationConfig();
        config.loadSettings("config.txt");
        animatedBackground.setConfig(config);


       /* config.setCrossSize(100);
        animatedBackground.setConfig(config);
        config.setCircleSize(100);
        animatedBackground.setConfig(config);
        config.setSnakeColor(animatedBackground, "#ff0000");
        config.setSnakeVisible(animatedBackground, false);
        animatedBackground.setConfig(config);
        config.setSnakeVisible(animatedBackground, true);
        config.setLineColor("#808080");
        animatedBackground.updateLineColor();
        config.setLineColor("#548892");
        animatedBackground.updateLineColor();
        config.setLineVisible(false);
        animatedBackground.updateLineVisibility();
        animatedBackground.setConfig(config);

        config.saveSettings("config.txt");*/



        scene.setRoot(animatedBackground);
        stage.setScene(scene);
        stage.show();
    }
}
