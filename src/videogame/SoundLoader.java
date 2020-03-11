/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;


import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

/**
 *
 * @author Ernesto García
 */
public class SoundLoader extends Application {

    /**
     * to get a sound from the file path
     * @param path it is the path of the file
     * @return the <bold>MediaPlayer</bold> object
     */
    public static MediaPlayer loadSound(String path) {
        final JFXPanel fxPanel = new JFXPanel();
        String uri = SoundLoader.class.getResource(path).toString();
        Media media = new Media(uri);
        MediaPlayer player = new MediaPlayer(media);
        return player;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        launch();
    }
}
